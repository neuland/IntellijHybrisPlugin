package de.neuland.hybris.run.action.config.editor;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import de.neuland.hybris.http.HTTPRequestManager;
import de.neuland.hybris.http.HybrisHTTPRequest;
import de.neuland.hybris.util.application.ApplicationPersitanceUtil;
import de.neuland.hybris.run.action.ExtendedHybrisConfigurationSettingType;
import de.neuland.hybris.run.action.form.ExtendedHybrisRunConfigurationEditorForm;
import de.neuland.hybris.run.action.config.HybrisRunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class HybrisRunConfigurationEditor extends SettingsEditor<HybrisRunConfiguration> {

    private final ExtendedHybrisRunConfigurationEditorForm myRootComponent;

    public HybrisRunConfigurationEditor(HybrisRunConfiguration project) {
        myRootComponent = createEditorPanel();
        setOldValuesInInputFields(project);
    }

    @Override
    protected void resetEditorFrom(HybrisRunConfiguration hybrisRunConfiguration) {
        //Wert aus Instanzen löschen
        HybrisHTTPRequest.getInstance().setServerURL(null);
        HTTPRequestManager.getInstance().setUsername(null);
        HTTPRequestManager.getInstance().setPassword(null);

        HybrisHTTPRequest.getInstance().setUsername(null);
        HybrisHTTPRequest.getInstance().setMaxCount(null);
        HybrisHTTPRequest.getInstance().setLocaleISOCode(null);

        //Werte aus Programmspeicher löschen
        ApplicationPersitanceUtil.removeApplicationSetting(ExtendedHybrisConfigurationSettingType.HYBRIS_SERVER_URL.name() + "-" + hybrisRunConfiguration.getName());
        ApplicationPersitanceUtil.removeApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_USERNAME.name() + "-" + hybrisRunConfiguration.getName());
        ApplicationPersitanceUtil.removeApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_PASSWORD.name() + "-" + hybrisRunConfiguration.getName());
        ApplicationPersitanceUtil.removeApplicationSetting(ExtendedHybrisConfigurationSettingType.MAX_COUNT.name() + "-" + hybrisRunConfiguration.getName());
        ApplicationPersitanceUtil.removeApplicationSetting(ExtendedHybrisConfigurationSettingType.LOCALE_ISO_CODE.name() + "-" + hybrisRunConfiguration.getName());
    }

    @Override
    protected void applyEditorTo(HybrisRunConfiguration hybrisRunConfiguration) throws ConfigurationException {
        //Wert in Instanzen schrieben
        HybrisHTTPRequest.getInstance().setServerURL(myRootComponent.getHybrisServerURLInput().getText());
        HTTPRequestManager.getInstance().setUsername(myRootComponent.getHACUsernameInput().getText());
        HTTPRequestManager.getInstance().setPassword(String.valueOf(myRootComponent.getHACPasswordInput().getPassword()));

        HybrisHTTPRequest.getInstance().setUsername(myRootComponent.getHACUsernameInput().getText());
        HybrisHTTPRequest.getInstance().setMaxCount(myRootComponent.getMaxCountField().getText());
        HybrisHTTPRequest.getInstance().setLocaleISOCode((String) myRootComponent.getLocaleIsoCodeBox().getSelectedItem());

        //Wert in Programmspeicher ablegen
        ApplicationPersitanceUtil.saveApplicationSetting(ExtendedHybrisConfigurationSettingType.HYBRIS_SERVER_URL.name() + "-" + hybrisRunConfiguration.getName(), myRootComponent.getHybrisServerURLInput().getText());
        ApplicationPersitanceUtil.saveApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_USERNAME.name() + "-" + hybrisRunConfiguration.getName(), myRootComponent.getHACUsernameInput().getText());
        ApplicationPersitanceUtil.saveApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_PASSWORD.name() + "-" + hybrisRunConfiguration.getName(), String.valueOf(myRootComponent.getHACPasswordInput().getPassword()));
        ApplicationPersitanceUtil.saveApplicationSetting(ExtendedHybrisConfigurationSettingType.MAX_COUNT.name() + "-" + hybrisRunConfiguration.getName(), myRootComponent.getMaxCountField().getText());
        ApplicationPersitanceUtil.saveApplicationSetting(ExtendedHybrisConfigurationSettingType.LOCALE_ISO_CODE.name() + "-" + hybrisRunConfiguration.getName(), (String) myRootComponent.getLocaleIsoCodeBox().getSelectedItem());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myRootComponent;
    }


    private ExtendedHybrisRunConfigurationEditorForm createEditorPanel() {
        return new ExtendedHybrisRunConfigurationEditorForm();
    }

    private void setOldValuesInInputFields(HybrisRunConfiguration hybrisRunConfiguration) {
        myRootComponent.getHybrisServerURLInput().setText(ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HYBRIS_SERVER_URL.name() + "-" + hybrisRunConfiguration.getName()));
        myRootComponent.getHACUsernameInput().setText(ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_USERNAME.name() + "-" + hybrisRunConfiguration.getName()));
        myRootComponent.getHACPasswordInput().setText(ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.HAC_PASSWORD.name() + "-" + hybrisRunConfiguration.getName()));
        myRootComponent.getMaxCountField().setText(ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.MAX_COUNT.name() + "-" + hybrisRunConfiguration.getName()));
        myRootComponent.selectLocaleIsoCodeIfExist(ApplicationPersitanceUtil.getApplicationSetting(ExtendedHybrisConfigurationSettingType.LOCALE_ISO_CODE.name() + "-" + hybrisRunConfiguration.getName()));
    }
}
