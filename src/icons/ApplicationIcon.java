package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class ApplicationIcon {
    private static Icon load(String path) {
        return IconLoader.getIcon(path, ApplicationIcon.class);
    }

    public static final Icon HYBRIS = load("/icons/hybris.png");

    public static final Icon HYBRIS_SMALL = load("/icons/hybrisSmall.png");

    public static final Icon HYBRIS_CONSOLE = load("/icons/myConsole.png");
}
