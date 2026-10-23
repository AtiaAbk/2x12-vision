package bd.july2024.audio;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

/**
 * Atmospheric & UI Audio Controller for Project July 2024.
 * Safely plays audio feedback for interactions, clicks, footsteps, and objective milestones.
 */
public class AudioController {

    private AudioNode clickNode;
    private AudioNode objectiveNode;
    private AudioNode footstepNode;
    private AudioNode bellNode;
    private boolean audioAvailable = true;

    public AudioController(AssetManager assetManager, Node rootNode) {
        try {
            clickNode = createAudio(assetManager, "audio/ui_click.wav", 0.6f);
            objectiveNode = createAudio(assetManager, "audio/objective.wav", 0.85f);
            footstepNode = createAudio(assetManager, "audio/footstep.wav", 0.35f);
            bellNode = createAudio(assetManager, "audio/bell.wav", 0.7f);

            if (rootNode != null) {
                if (clickNode != null) rootNode.attachChild(clickNode);
                if (objectiveNode != null) rootNode.attachChild(objectiveNode);
                if (footstepNode != null) rootNode.attachChild(footstepNode);
                if (bellNode != null) rootNode.attachChild(bellNode);
            }
        } catch (Throwable t) {
            audioAvailable = false;
        }
    }

    private AudioNode createAudio(AssetManager assetManager, String path, float volume) {
        try {
            AudioNode node = new AudioNode(assetManager, path, DataType.Buffer);
            node.setPositional(false);
            node.setLooping(false);
            node.setVolume(volume);
            return node;
        } catch (Throwable t) {
            return null;
        }
    }

    public void playClick() {
        if (audioAvailable && clickNode != null) {
            try { clickNode.playInstance(); } catch (Throwable ignored) {}
        }
    }

    public void playObjective() {
        if (audioAvailable && objectiveNode != null) {
            try { objectiveNode.playInstance(); } catch (Throwable ignored) {}
        }
    }

    public void playFootstep() {
        if (audioAvailable && footstepNode != null) {
            try { footstepNode.playInstance(); } catch (Throwable ignored) {}
        }
    }

    public void playBell() {
        if (audioAvailable && bellNode != null) {
            try { bellNode.playInstance(); } catch (Throwable ignored) {}
        }
    }
}
