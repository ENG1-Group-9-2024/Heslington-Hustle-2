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
     * variable for whether the player wants to play with WASD controls or
     * with arrow keys, set to arrow keys by default
     */
    public static boolean keyboardControls = true;

    public static boolean getKeyboardControls() {
        return keyboardControls;
    }

    public static void setKeyboardControls(boolean c) {
        PlayerComponent.keyboardControls = c;
    }

    public static String username = "";

    public static String getUserName() {
        return username;
    }

    public static void setUserName(String n) {
        PlayerComponent.username = n;
    }
}
