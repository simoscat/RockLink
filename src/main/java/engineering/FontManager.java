package engineering;

import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.List;

public final class FontManager {

    private static final List<String> FONT_RESOURCES = List.of(
            "/fonts/Anton-Regular.ttf",
            "/fonts/Rockout.otf",
            "/fonts/JetBrainsMonoNL-Regular.ttf",
            "/fonts/JetBrainsMonoNL-Bold.ttf"
    );

    private FontManager() {}

    public static void loadEmbeddedFonts() {
        for (String resource : FONT_RESOURCES) {
            try (InputStream fontStream = FontManager.class.getResourceAsStream(resource)) {
                Font.loadFont(fontStream, 0);
            } catch (Exception e) {
                throw new IllegalStateException("Impossibile caricare il font: " + resource, e);
            }
        }
    }
}