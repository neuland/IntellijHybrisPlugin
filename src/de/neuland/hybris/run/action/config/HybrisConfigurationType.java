package de.neuland.hybris.run.action.config;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import icons.ApplicationIcon;
import org.jetbrains.annotations.NotNull;

public class HybrisConfigurationType extends ConfigurationTypeBase {

    public HybrisConfigurationType() {
        super("ConsoleScriptInHybrisRunner", "Console script in hybris", "Runs a Groovy, Beanshell or Flexsearch in hybris", ApplicationIcon.HYBRIS);
        addFactory(new ConfigurationFactory(this) {
            @Override
            public RunConfiguration createTemplateConfiguration(Project project) {
                return new HybrisRunConfiguration(project, this, "HybrisRunner");
            }

            @Override
            public boolean isConfigurationSingletonByDefault() {
                return true;
            }

            @Override
            public boolean canConfigurationBeSingleton() {
                return false;
            }
        });
    }

    @NotNull
    public static HybrisConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(HybrisConfigurationType.class);
    }
}
