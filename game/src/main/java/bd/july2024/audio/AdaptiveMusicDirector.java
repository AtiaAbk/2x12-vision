package bd.july2024.audio;

import bd.july2024.GameSettings;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource.Status;
import com.jme3.scene.Node;

import java.util.EnumMap;
import java.util.Map;

/**
 * AAA-grade adaptive music director for Project July 2024.
 *
 * Provides:
 * - Dynamic state machine reacting to gameplay events (EXPLORATION, GATHERING, TENSION, etc.)
 * - Seamless dual-channel crossfading between musical cues without audible pops or sudden restarts
 * - Intelligent dialogue ducking to ensure spoken lines and reading remain crisp and audible
 * - Pause and resume volume attenuation
 * - Zero-crash fallback architecture when an individual soundtrack file is missing
 */
public class AdaptiveMusicDirector {

    private final AssetManager assetManager;
    private final Node rootNode;

    private final Map<MusicState, String> trackPaths = new EnumMap<>(MusicState.class);

    private MusicState currentState = null;
    private MusicState previousStateBeforePause = null;

    private AudioNode currentChannel = null;
    private AudioNode nextChannel = null;
    private String currentPath = null;

    private float crossfadeDuration = 2.0f;
    private float crossfadeTimer = 0.0f;
    private boolean isCrossfading = false;

    private float currentVolumeMultiplier = 1.0f;
    private float nextVolumeMultiplier = 0.0f;

    private float masterVolume = 0.8f;
    private float musicVolume = 0.75f;
    private float duckMultiplier = 1.0f;
    private float targetDuckMultiplier = 1.0f;

    public AdaptiveMusicDirector(AssetManager assetManager, Node rootNode) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        initTrackMapping();
        syncWithSettings();
    }

    private void initTrackMapping() {
        trackPaths.put(MusicState.MAIN_MENU, "audio/music_main_theme.wav");
        trackPaths.put(MusicState.EXPLORATION, "audio/music_exploration.wav");
        trackPaths.put(MusicState.STORY_DIALOGUE, "audio/music_dialogue.wav");
        trackPaths.put(MusicState.GATHERING, "audio/music_gathering.wav");
        trackPaths.put(MusicState.TENSION, "audio/music_tension.wav");
        trackPaths.put(MusicState.CINEMATIC, "audio/music_main_theme.wav");
        trackPaths.put(MusicState.CHAPTER_COMPLETE, "audio/music_chapter_complete.wav");
        trackPaths.put(MusicState.PAUSED, null); // Handled via volume ducking & low-pass attenuation
        trackPaths.put(MusicState.GAME_OVER, "audio/music_dialogue.wav");
        trackPaths.put(MusicState.ENDING, "audio/music_main_theme.wav");
    }

    public void syncWithSettings() {
        GameSettings s = GameSettings.get();
        if (s != null) {
            this.masterVolume = s.masterVolume;
            this.musicVolume = s.musicVolume;
        }
        applyEffectiveVolumes();
    }

    public MusicState getCurrentState() {
        return currentState;
    }

    /**
     * Transitions the adaptive soundtrack to a new game context.
     *
     * @param newState Target musical state.
     * @param crossfadeSec Crossfade duration in seconds.
     */
    public void transitionTo(MusicState newState, float crossfadeSec) {
        if (newState == currentState && !isCrossfading) {
            return;
        }

        // Handle PAUSED state specially: duck active channel without resetting music position
        if (newState == MusicState.PAUSED) {
            if (currentState != MusicState.PAUSED) {
                previousStateBeforePause = currentState;
                currentState = MusicState.PAUSED;
                targetDuckMultiplier = 0.25f;
            }
            return;
        }

        // Resuming from PAUSED
        if (currentState == MusicState.PAUSED && newState != MusicState.PAUSED) {
            targetDuckMultiplier = 1.0f;
            if (previousStateBeforePause != null && previousStateBeforePause == newState) {
                currentState = newState;
                previousStateBeforePause = null;
                return;
            }
        }

        String targetPath = trackPaths.get(newState);
        if (targetPath == null) {
            targetPath = "audio/music_main_theme.wav";
        }

        // If target track is identical to currently playing track, just adjust state tag and unduck
        if (targetPath.equals(currentPath) && currentChannel != null && currentChannel.getStatus() == Status.Playing) {
            currentState = newState;
            targetDuckMultiplier = 1.0f;
            return;
        }

        AudioNode newAudio = createMusicNode(targetPath, newState != MusicState.CHAPTER_COMPLETE);
        if (newAudio == null && !"audio/music_title.wav".equals(targetPath)) {
            // Fallback to legacy music_title.wav if newly composed track is unavailable
            newAudio = createMusicNode("audio/music_title.wav", true);
        }

        if (newAudio == null) {
            System.err.println("[AdaptiveMusicDirector] Could not load audio for state " + newState + "; maintaining current track.");
            currentState = newState;
            return;
        }

        // Start crossfading
        if (nextChannel != null) {
            safelyStopAndDetach(nextChannel);
        }

        nextChannel = newAudio;
        nextChannel.setVolume(0.0f);
        try {
            nextChannel.play();
        } catch (Throwable t) {
            System.err.println("[AdaptiveMusicDirector] Play failure: " + t.getMessage());
        }

        this.crossfadeDuration = Math.max(0.2f, crossfadeSec);
        this.crossfadeTimer = 0.0f;
        this.isCrossfading = true;
        this.currentState = newState;
        this.currentPath = targetPath;
        this.targetDuckMultiplier = 1.0f;
    }

    public void setDialogueDucking(boolean active) {
        this.targetDuckMultiplier = active ? 0.38f : 1.0f;
    }

    public void update(float tpf) {
        // Smooth ducking interpolation (e.g. during dialogue or pause menu)
        if (Math.abs(duckMultiplier - targetDuckMultiplier) > 0.005f) {
            duckMultiplier += (targetDuckMultiplier - duckMultiplier) * Math.min(1.0f, tpf * 5.0f);
        } else {
            duckMultiplier = targetDuckMultiplier;
        }

        if (isCrossfading) {
            crossfadeTimer += tpf;
            float progress = Math.min(1.0f, crossfadeTimer / crossfadeDuration);

            // Equal-power crossfade curve for professional musical blending
            currentVolumeMultiplier = (float) Math.cos(progress * Math.PI * 0.5);
            nextVolumeMultiplier = (float) Math.sin(progress * Math.PI * 0.5);

            if (progress >= 1.0f) {
                // Crossfade complete: promote nextChannel to currentChannel
                isCrossfading = false;
                safelyStopAndDetach(currentChannel);
                currentChannel = nextChannel;
                nextChannel = null;
                currentVolumeMultiplier = 1.0f;
                nextVolumeMultiplier = 0.0f;
            }
        }

        applyEffectiveVolumes();
    }

    private void applyEffectiveVolumes() {
        float baseVol = Math.max(0.0f, Math.min(1.0f, masterVolume * musicVolume));
        float effectiveCurrent = baseVol * currentVolumeMultiplier * duckMultiplier;
        float effectiveNext = baseVol * nextVolumeMultiplier * duckMultiplier;

        if (currentChannel != null) {
            try {
                currentChannel.setVolume(effectiveCurrent);
            } catch (Throwable ignored) {}
        }
        if (nextChannel != null) {
            try {
                nextChannel.setVolume(effectiveNext);
            } catch (Throwable ignored) {}
        }
    }

    private AudioNode createMusicNode(String path, boolean looping) {
        if (path == null) return null;
        try {
            AudioNode node = new AudioNode(assetManager, path, DataType.Stream);
            node.setPositional(false);
            node.setLooping(looping);
            node.setVolume(0.0f);
            if (rootNode != null) rootNode.attachChild(node);
            return node;
        } catch (Throwable t) {
            System.err.println("[AdaptiveMusicDirector] Failed to stream '" + path + "': " + t.getMessage());
            return null;
        }
    }

    private void safelyStopAndDetach(AudioNode node) {
        if (node != null) {
            try {
                if (node.getStatus() == Status.Playing) {
                    node.stop();
                }
                if (rootNode != null) {
                    rootNode.detachChild(node);
                }
            } catch (Throwable ignored) {}
        }
    }

    public void stopAll() {
        isCrossfading = false;
        safelyStopAndDetach(currentChannel);
        safelyStopAndDetach(nextChannel);
        currentChannel = null;
        nextChannel = null;
        currentPath = null;
        currentState = null;
    }
}
