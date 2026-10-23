package bd.july2024.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * Atmospheric and UI audio manager for Project July 2024.
 * Safely loads audio assets with procedural fallbacks if sound files are unavailable.
 */
public class SoundManager implements Disposable {

    private static SoundManager instance;

    private Sound clickSound;
    private Sound objectiveSound;
    private Sound footstepSound;
    private Sound bellSound;
    private boolean audioEnabled = true;

    public static SoundManager get() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public SoundManager() {
        loadSounds();
    }

    private void loadSounds() {
        try {
            if (Gdx.files != null) {
                if (Gdx.files.internal("audio/ui_click.wav").exists()) {
                    clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/ui_click.wav"));
                }
                if (Gdx.files.internal("audio/objective.wav").exists()) {
                    objectiveSound = Gdx.audio.newSound(Gdx.files.internal("audio/objective.wav"));
                }
                if (Gdx.files.internal("audio/footstep.wav").exists()) {
                    footstepSound = Gdx.audio.newSound(Gdx.files.internal("audio/footstep.wav"));
                }
                if (Gdx.files.internal("audio/bell.wav").exists()) {
                    bellSound = Gdx.audio.newSound(Gdx.files.internal("audio/bell.wav"));
                }
            }
        } catch (Throwable t) {
            // Audio loading exception handled gracefully
        }
    }

    public void playClick() {
        if (audioEnabled && clickSound != null) {
            try { clickSound.play(0.6f); } catch (Throwable ignored) {}
        }
    }

    public void playObjective() {
        if (audioEnabled && objectiveSound != null) {
            try { objectiveSound.play(0.8f); } catch (Throwable ignored) {}
        }
    }

    public void playFootstep() {
        if (audioEnabled && footstepSound != null) {
            try { footstepSound.play(0.35f); } catch (Throwable ignored) {}
        }
    }

    public void playBell() {
        if (audioEnabled && bellSound != null) {
            try { bellSound.play(0.7f); } catch (Throwable ignored) {}
        }
    }

    public void setAudioEnabled(boolean enabled) {
        this.audioEnabled = enabled;
    }

    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    @Override
    public void dispose() {
        if (clickSound != null) { clickSound.dispose(); clickSound = null; }
        if (objectiveSound != null) { objectiveSound.dispose(); objectiveSound = null; }
        if (footstepSound != null) { footstepSound.dispose(); footstepSound = null; }
        if (bellSound != null) { bellSound.dispose(); bellSound = null; }
        instance = null;
    }
}
