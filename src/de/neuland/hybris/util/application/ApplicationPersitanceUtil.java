package de.neuland.hybris.util.application;

import com.intellij.ide.util.PropertiesComponent;

public class ApplicationPersitanceUtil {

    public static String getApplicationSetting(String key) {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        return propertiesComponent.getValue(key);
    }

    public static void saveApplicationSetting(String key, String value) {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        propertiesComponent.setValue(key, value);
    }

    public static void removeApplicationSetting(String key) {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        propertiesComponent.unsetValue(key);
    }

}
