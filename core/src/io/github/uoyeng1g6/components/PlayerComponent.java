package io.github.uoyeng1g6.components;

import com.badlogic.ashley.core.Component;

/**
 * Component containing information about the player. There should only ever
 * be a single entity that has this component added to it.
 */
public class PlayerComponent implements Component {
    /**
     * Whether the player is currently trying to interact.
     */
    public boolean isInteracting = false;
    /**
     * KeyboardControls is a new variable for whether the player wants to play with
     * WASD controls or with arrow keys, set to arrow keys by default
     */
    public static boolean keyboardControls = true;

    /**
     * Function to get keyboard Controls, so that other classes in the program can access it
     * @return keyboardControls
     */
    public static boolean getKeyboardControls() {
        return keyboardControls;
    }
    /**
     * Function to change the value of keyboard controls
     * @param c the boolean value to change keyboardControls to
     */
    public static void setKeyboardControls(boolean c) {
        PlayerComponent.keyboardControls = c;
    }
    /**
     * username variable which the player enters at the start of the game and their score will
     * be stored with on the leaderboard at the end
     */
    public static String username = "";
    /**
     * username getter
     * @return username
     */
    public static String getUserName() {
        return username;
    }

    /**
     * username setter
     * @param n the string that username will be set to
     */
    public static void setUserName(String n) {
        PlayerComponent.username = n;
    }
}
