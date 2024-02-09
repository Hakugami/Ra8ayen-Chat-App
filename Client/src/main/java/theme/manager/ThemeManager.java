package theme.manager;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static String currentTheme = "Minimalist";
    private static final List<Scene> scenes = new ArrayList<>();

    public static void registerScene(Scene scene) {
        scenes.add(scene);
    }

    public static void changeThemeForAllScenes(String newThemeName) {
        for (Scene scene : scenes) {
            changeThemeForScene(scene, newThemeName);
        }
    }

    public static void changeThemeForScene(Scene scene, String newThemeName) {
        List<String> stylesheets = scene.getStylesheets();

        for (int i = 0; i < stylesheets.size(); i++) {
            String stylesheet = stylesheets.get(i);

            // Extract the theme name from the stylesheet path
            String oldThemeName = stylesheet.substring(stylesheet.lastIndexOf("/") + 1, stylesheet.lastIndexOf("."));

            if (oldThemeName.equals(currentTheme)) {
                // Remove the old stylesheet
                stylesheets.remove(i);

                // Replace the old theme name with the new theme name in the path
                String newStylesheet = stylesheet.replace(oldThemeName, newThemeName);

                // Add the new stylesheet
                stylesheets.add(i, newStylesheet);

                // Update the current theme
                currentTheme = newThemeName;

                break;
            }
        }
    }
}