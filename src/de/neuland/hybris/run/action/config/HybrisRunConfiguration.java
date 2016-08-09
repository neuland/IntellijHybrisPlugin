package de.neuland.hybris.run.action.config;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.project.Project;
import de.neuland.hybris.util.application.ApplicationPersitanceUtil;
import de.neuland.hybris.run.action.ExtendedHybrisConfigurationSettingType;
import de.neuland.hybris.run.action.config.editor.HybrisRunConfigurationEditor;
import de.neuland.hybris.run.action.runner.HybrisRunProfileState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HybrisRunConfiguration extends RunConfigurationBase {

    private static final String HYBIRS_RUN_CONFIGURATION = "HybirsRunConfiguration";

    public HybrisRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory configurationFactory, @NotNull String name) {
        super(project, configurationFactory, name);
    }

    @NotNull
    @Override
    public HybrisRunConfigurationEditor getConfigurationEditor() {
        return new HybrisRunConfigurationEditor(this);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {

    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        String serverUrl = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HYBRIS_SERVER_URL.name() + HYBIRS_RUN_CONFIGURATION);
        String hacUser = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_USERNAME.name() + HYBIRS_RUN_CONFIGURATION);
        String hacPassword = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_PASSWORD.name() + HYBIRS_RUN_CONFIGURATION);
        String maxCount = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.MAX_COUNT.name() + HYBIRS_RUN_CONFIGURATION);
        String localISOCode = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.LOCALE_ISO_CODE.name() + HYBIRS_RUN_CONFIGURATION);
        String hybrisVersion = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HYBRIS_VERSION.name() + HYBIRS_RUN_CONFIGURATION);
        return new HybrisRunProfileState(getProject(),executionEnvironment, executor, serverUrl, hacUser, hacPassword, maxCount,localISOCode);
    }

    public String getRunConfigSuffix() {
        return HYBIRS_RUN_CONFIGURATION;
    }

}
