package bd.july2024.audio;

import bd.july2024.world.GroundType;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

import java.util.Random;

/**
 * UI, footstep, ambience and music playback. Every file under assets/audio is synthesised by
 * tools/synth_audio.py (original, see docs/ASSET_REGISTER.md); playback failures are swallowed so a
 * machine without audio still runs the game.
 */
public class AudioController {

    private static final int FOOTSTEP_VARIANTS = 3;

    private final AssetManager assetManager;
    private final Node rootNode;
    private final Random random = new Random();
    private boolean audioAvailable = true;

    private AudioNode clickNode;
    private AudioNode confirmNode;
    private AudioNode backNode;
    private AudioNode notifyNode;
    private AudioNode warningNode;
    private AudioNode objectiveNode;
    private AudioNode bellNode;
    private final AudioNode[] grassSteps = new AudioNode[FOOTSTEP_VARIANTS];
    private final AudioNode[] pavingSteps = new AudioNode[FOOTSTEP_VARIANTS];
    private final AudioNode[] asphaltSteps = new AudioNode[FOOTSTEP_VARIANTS];
    private AudioNode ambienceNode;
    private AudioNode musicNode;
    private float ambienceVolume = 0.55f;
    private float musicVolume = 0.5f;

    public AudioController(AssetManager assetManager, Node rootNode) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        try {
            clickNode = createAudio("audio/ui_click.wav", 0.5f);
            confirmNode = createAudio("audio/ui_confirm.wav", 0.6f);
            backNode = createAudio("audio/ui_back.wav", 0.5f);
            notifyNode = createAudio("audio/ui_notify.wav", 0.6f);
            warningNode = createAudio("audio/ui_warning.wav", 0.6f);
            objectiveNode = createAudio("audio/objective.wav", 0.8f);
            bellNode = createAudio("audio/bell.wav", 0.6f);
            for (int i = 0; i < FOOTSTEP_VARIANTS; i++) {
                grassSteps[i] = createAudio("audio/footstep_grass_" + (i + 1) + ".wav", 0.35f);
                pavingSteps[i] = createAudio("audio/footstep_paving_" + (i + 1) + ".wav", 0.4f);
                asphaltSteps[i] = createAudio("audio/footstep_asphalt_" + (i + 1) + ".wav", 0.38f);
            }
        } catch (Throwable t) {
            audioAvailable = false;
            System.err.println("[AudioController] Audio unavailable: " + t);
        }
    }

    private AudioNode createAudio(String path, float volume) {
        try {
            AudioNode node = new AudioNode(assetManager, path, DataType.Buffer);
            node.setPositional(false);
            node.setLooping(false);
            node.setVolume(volume);
            if (rootNode != null) rootNode.attachChild(node);
            return node;
        } catch (Throwable t) {
            System.err.println("[AudioController] Missing or unreadable " + path + ": " + t.getMessage());
            return null;
        }
    }

    private AudioNode createLoop(String path, float volume) {
        try {
            AudioNode node = new AudioNode(assetManager, path, DataType.Stream);
            node.setPositional(false);
            node.setLooping(true);
            node.setVolume(volume);
            if (rootNode != null) rootNode.attachChild(node);
            return node;
        } catch (Throwable t) {
            System.err.println("[AudioController] Missing or unreadable " + path + ": " + t.getMessage());
            return null;
        }
    }

    private void playInstance(AudioNode n) {
        if (audioAvailable && n != null) {
            try { n.playInstance(); } catch (Throwable ignored) {}
        }
    }

    public void playClick() { playInstance(clickNode); }
    public void playConfirm() { playInstance(confirmNode); }
    public void playBack() { playInstance(backNode); }
    public void playNotify() { playInstance(notifyNode); }
    public void playWarning() { playInstance(warningNode); }
    public void playObjective() { playInstance(objectiveNode); }
    public void playBell() { playInstance(bellNode); }

    /** Kept for callers that do not know the surface. */
    public void playFootstep() { playFootstep(GroundType.PAVING); }

    public void playFootstep(GroundType ground) {
        AudioNode[] set = ground == GroundType.GRASS ? grassSteps : (ground == GroundType.ASPHALT ? asphaltSteps : pavingSteps);
        AudioNode n = set[random.nextInt(FOOTSTEP_VARIANTS)];
        if (n != null) {
            try { n.setPitch(0.94f + random.nextFloat() * 0.12f); } catch (Throwable ignored) {}
        }
        playInstance(n);
    }

    /** Starts the campus ambience loop (idempotent). */
    public void startAmbience() {
        if (!audioAvailable) return;
        if (ambienceNode == null) ambienceNode = createLoop("audio/ambience_campus.wav", ambienceVolume);
        if (ambienceNode != null && ambienceNode.getStatus() != com.jme3.audio.AudioSource.Status.Playing) {
            try { ambienceNode.play(); } catch (Throwable ignored) {}
        }
    }

    /** Ambience level: 1 in play, lower under the front-end menus. */
    public void setAmbienceLevel(float level) {
        if (ambienceNode != null) {
            try { ambienceNode.setVolume(ambienceVolume * Math.max(0f, Math.min(1f, level))); } catch (Throwable ignored) {}
        }
    }

    public void startTitleMusic() {
        if (!audioAvailable) return;
        if (musicNode == null) musicNode = createLoop("audio/music_title.wav", musicVolume);
        if (musicNode != null && musicNode.getStatus() != com.jme3.audio.AudioSource.Status.Playing) {
            try { musicNode.play(); } catch (Throwable ignored) {}
        }
    }

    public void stopMusic() {
        if (musicNode != null) {
            try { musicNode.stop(); } catch (Throwable ignored) {}
        }
    }

    public boolean isAvailable() {
        return audioAvailable;
    }
}
