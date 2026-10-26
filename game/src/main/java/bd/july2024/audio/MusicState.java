package bd.july2024.audio;

/**
 * State machine representing musical context across Project July 2024.
 */
public enum MusicState {
    /** Front-end title screen, main menu, chapter select, credits. */
    MAIN_MENU,

    /** Peaceful daytime exploration across the Dhaka University campus slice. */
    EXPLORATION,

    /** Intimate character conversations and story dialogue (Rafi, Tanvir, Nusrat). */
    STORY_DIALOGUE,

    /** Students assembling and leaflet distribution, rising collective momentum. */
    GATHERING,

    /** Imminent confrontation, rally assembly, high tension and resolve. */
    TENSION,

    /** High-impact historical narrative cinematic or focal sequence. */
    CINEMATIC,

    /** Chapter completion fanfare and emotional resolution. */
    CHAPTER_COMPLETE,

    /** In-game pause menu (ducked background with atmospheric presence). */
    PAUSED,

    /** Mission failure / reset aftermath. */
    GAME_OVER,

    /** Final credits and epilogue resolution theme. */
    ENDING
}
