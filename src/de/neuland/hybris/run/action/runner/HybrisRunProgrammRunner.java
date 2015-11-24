package de.neuland.hybris.run.action.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.GenericProgramRunner;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import de.neuland.hybris.http.HTTPRequestManager;
import de.neuland.hybris.http.HybrisHTTPRequest;
import de.neuland.hybris.util.application.ApplicationPersitanceUtil;
import de.neuland.hybris.run.action.ExtendedHybrisConfigurationSettingType;
import de.neuland.hybris.run.action.config.HybrisRunConfiguration;
import de.neuland.hybris.util.execution.ExecuteBeanshellConsole;
import de.neuland.hybris.util.execution.ExecuteFlexsearchConsole;
import de.neuland.hybris.util.execution.ExecuteGroovyConsole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HybrisRunProgrammRunner extends GenericProgramRunner {

    private static final String DEFAULT_MAX_COUNT = "200";
    private static final String DEFAULT_LOCALE_ISO_CODE = "de";
    private static final String GROOVY = "Groovy";

    @Nullable
    @Override
    protected RunContentDescriptor doExecute(@NotNull Project project, @NotNull RunProfileState runProfileState, @Nullable RunContentDescriptor runContentDescriptor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if(editor == null) {
            Messages.showMessageDialog(project, "Editor is null.", "Error", Messages.getErrorIcon());
            return  null;
        }

        setUserDataInHTTPRequestForExecute(executionEnvironment.getRunProfile());
        setAdditionalFlexsearchParameterInHybrisHttpRequest(executionEnvironment.getRunProfile());

        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if(psiFile != null) {
            if (psiFile.getFileType().getName().equals(GROOVY)) {
                ExecuteGroovyConsole.getInstance().execute(psiFile.getText(), project);
                return null;
            }
            if (psiFile.getName().endsWith(".flex")) {
                ExecuteFlexsearchConsole.getInstance().execute(psiFile.getText(), project);
                return null;
            }
            if (psiFile.getName().endsWith(".flexsearch")) {
                ExecuteFlexsearchConsole.getInstance().execute(psiFile.getText(), project);
                return null;
            }
            if (psiFile.getName().endsWith(".bsh")) {
                ExecuteBeanshellConsole.getInstance().execute(psiFile.getText(), project);
                return null;
            }
            if (psiFile.getName().endsWith(".beanshell")) {
                ExecuteBeanshellConsole.getInstance().execute(psiFile.getText(), project);
                return null;
            }
            Messages.showMessageDialog(project, "Filetype not recognized\r\nPlease use for:\r\n-Groovy: .groovy\r\n-Beanshell: .bsh or .beanshell\r\n-FlexibleSearch: .flex or .flexsearch", "Error", Messages.getErrorIcon());
            return null;
        }
        Messages.showMessageDialog(project, "Can not read file\nPlease focus the editor", "Error", Messages.getErrorIcon());
        return null;
    }

    private void setUserDataInHTTPRequestForExecute(RunProfile runProfile) {
        HybrisRunConfiguration runConfiguration = (HybrisRunConfiguration) runProfile;
        HTTPRequestManager httpRequestManager = HTTPRequestManager.getInstance();
        HybrisHTTPRequest hybrisHttpRequest = HybrisHTTPRequest.getInstance();

        String configurationName = runConfiguration.getName();
        String configServerURL = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HYBRIS_SERVER_URL.name() + "-" + configurationName);
        String configUsername = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_USERNAME.name() + "-" + configurationName);
        String configPassword = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_PASSWORD.name() + "-" + configurationName);

        if(!httpRequestManager.isUserDataSet() || !httpRequestManager.isUserDataEqual(configUsername, configPassword)) {
            httpRequestManager.setUsername(configUsername);
            httpRequestManager.setPassword(configPassword);
        }
        if(!hybrisHttpRequest.isHybrisServerURLSet() || !hybrisHttpRequest.isHybrisServerURLEqual(configServerURL)) {
            hybrisHttpRequest.setServerURL(configServerURL);
        }
    }

    private void setAdditionalFlexsearchParameterInHybrisHttpRequest(RunProfile runProfile) {
        HybrisRunConfiguration runConfiguration = (HybrisRunConfiguration) runProfile;
        HybrisHTTPRequest hybrisHttpRequest = HybrisHTTPRequest.getInstance();
        String configurationName = runConfiguration.getName();

        String maxCount = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.MAX_COUNT.name() + "-" + configurationName);
        String localeIsoCode = ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.LOCALE_ISO_CODE.name() + "-" + configurationName);

        hybrisHttpRequest.setMaxCount((maxCount != null && !maxCount.equals("")) ? maxCount : DEFAULT_MAX_COUNT);
        hybrisHttpRequest.setLocaleISOCode((localeIsoCode != null && !localeIsoCode.equals("")) ? localeIsoCode : DEFAULT_LOCALE_ISO_CODE);
        hybrisHttpRequest.setUsername(ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_USERNAME.name() + "-" + configurationName));
    }

    @NotNull
    @Override
    public String getRunnerId() {
        return "HybrisConsoleExecution";
    }

    @Override
    public boolean canRun(@NotNull String s, @NotNull RunProfile runProfile) {
        return DefaultRunExecutor.EXECUTOR_ID.equals(s) && runProfile instanceof HybrisRunConfiguration;
    }
}
