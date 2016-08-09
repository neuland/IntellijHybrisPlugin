package de.neuland.hybris.run.action.form;

import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;
import java.awt.*;

public class ExtendedHybrisRunConfigurationEditorForm extends SimpleHybrisRunConfigurationEditorForm {

    private static final String[] LOCALE_ISO_CODES = {"de", "fr", "en", "at", "ch", "nl", "no", "pl"};
    private static final String[] HYBRIS_VERSIONS = new String[]{"< 5.0", ">= 5.0"};
    private JTextField maxCountField;
    private JComboBox localeIsoCodeBox;
    private JComboBox hybrisVersionComboBox;

    public ExtendedHybrisRunConfigurationEditorForm() {
        super();
        createSeperator();
        createHeadline();
        createMaxCountInput();
        createLocaleIsoCodeComboBox();
        createHybrisVersionComboBox();
    }

    private void createHeadline() {
        JLabel maxCountLable = new JLabel("FlexibleSearch parameters");
        GridBagConstraints bcdMacCountLable = new GridBagConstraints();
        bcdMacCountLable.insets = new Insets(0, 0, 5, 0);
        bcdMacCountLable.anchor = GridBagConstraints.WEST;
        bcdMacCountLable.gridx = 1;
        bcdMacCountLable.gridy = 5;
        add(maxCountLable, bcdMacCountLable);
    }

    private void createMaxCountInput() {
        JLabel maxCountLable = new JLabel("max count");
        GridBagConstraints bcdMacCountLable = new GridBagConstraints();
        bcdMacCountLable.insets = new Insets(0, 0, 5, 5);
        bcdMacCountLable.anchor = GridBagConstraints.EAST;
        bcdMacCountLable.gridx = 0;
        bcdMacCountLable.gridy = 6;
        add(maxCountLable, bcdMacCountLable);

        maxCountField = new JTextField();
        maxCountLable.setLabelFor(maxCountField);
        GridBagConstraints gbcMaxCountInput = new GridBagConstraints();
        gbcMaxCountInput.insets = new Insets(0, 0, 5, 0);
        gbcMaxCountInput.fill = GridBagConstraints.HORIZONTAL;
        gbcMaxCountInput.gridx = 1;
        gbcMaxCountInput.gridy = 6;
        add(maxCountField, gbcMaxCountInput);
        maxCountField.setColumns(10);
    }

    private void createLocaleIsoCodeComboBox() {
        JLabel localeIsoCodeLable = new JLabel("locale ISO-Code");
        GridBagConstraints bcdMacCountLable = new GridBagConstraints();
        bcdMacCountLable.insets = new Insets(0, 0, 5, 5);
        bcdMacCountLable.anchor = GridBagConstraints.EAST;
        bcdMacCountLable.gridx = 0;
        bcdMacCountLable.gridy = 7;
        add(localeIsoCodeLable, bcdMacCountLable);

        localeIsoCodeBox = new ComboBox(LOCALE_ISO_CODES);
        localeIsoCodeLable.setLabelFor(localeIsoCodeBox);
        GridBagConstraints gbcLocaleIsoCode = new GridBagConstraints();
        gbcLocaleIsoCode.insets = new Insets(0, 0, 5, 0);
        gbcLocaleIsoCode.fill = GridBagConstraints.HORIZONTAL;
        gbcLocaleIsoCode.gridx = 1;
        gbcLocaleIsoCode.gridy = 7;
        add(localeIsoCodeBox, gbcLocaleIsoCode);
    }

    private void createHybrisVersionComboBox() {
        JLabel hybrisVersionLable = new JLabel("HybrisVesion");
        GridBagConstraints bcdMacCountLable = new GridBagConstraints();
        bcdMacCountLable.insets = new Insets(0, 0, 5, 5);
        bcdMacCountLable.anchor = GridBagConstraints.EAST;
        bcdMacCountLable.gridx = 0;
        bcdMacCountLable.gridy = 8;
        add(hybrisVersionLable, bcdMacCountLable);

        hybrisVersionComboBox = new ComboBox(HYBRIS_VERSIONS);
        hybrisVersionLable.setLabelFor(hybrisVersionComboBox);
        GridBagConstraints gbcHybrisVersion = new GridBagConstraints();
        gbcHybrisVersion.insets = new Insets(0, 0, 5, 0);
        gbcHybrisVersion.fill = GridBagConstraints.HORIZONTAL;
        gbcHybrisVersion.gridx = 1;
        gbcHybrisVersion.gridy = 8;
        add(hybrisVersionComboBox, gbcHybrisVersion);
    }

    private void createSeperator() {
        JSeparator separator1 = new JSeparator();
        GridBagConstraints gbcSeparator1 = new GridBagConstraints();
        gbcSeparator1.insets = new Insets(0, 0, 5, 5);
        gbcSeparator1.gridx = 0;
        gbcSeparator1.gridy = 4;
        add(separator1, gbcSeparator1);

        JSeparator separator2 = new JSeparator();
        GridBagConstraints gbcSeparator2 = new GridBagConstraints();
        gbcSeparator2.insets = new Insets(0, 0, 5, 0);
        gbcSeparator2.gridx = 1;
        gbcSeparator2.gridy = 4;
        add(separator2, gbcSeparator2);
    }

    public void selectLocaleIsoCodeIfExist(String isoCode) {
        for(int i = 0; i < LOCALE_ISO_CODES.length; i++) {
            if(LOCALE_ISO_CODES[i].equals(isoCode)) {
                localeIsoCodeBox.setSelectedIndex(i);
            }
        }
    }

    public void selectHybrisVersionIfExist(String hybrisVersion) {
        for(int i = 0; i < HYBRIS_VERSIONS.length; i++) {
            if(HYBRIS_VERSIONS[i].equals(hybrisVersion)) {
                hybrisVersionComboBox.setSelectedIndex(i);
            }
        }
    }

    public JTextField getMaxCountField() {
        return maxCountField;
    }

    public JComboBox getLocaleIsoCodeBox() {
        return localeIsoCodeBox;
    }

    public JComboBox getHybrisVersionComboBox() {
        return hybrisVersionComboBox;
    }
}
