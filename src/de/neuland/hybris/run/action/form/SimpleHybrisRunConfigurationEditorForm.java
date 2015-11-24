package de.neuland.hybris.run.action.form;

import javax.swing.*;
import java.awt.*;

public class SimpleHybrisRunConfigurationEditorForm extends JPanel {
    protected GridBagLayout gridBagLayout;
    private JTextField hybrisServerURLInput;
    private JTextField HACUsernameInput;
    private JPasswordField HACPasswordInput;

    public SimpleHybrisRunConfigurationEditorForm() {
        gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        JLabel hybrisServerURLLabel = new JLabel("Hybris Server URL");
        GridBagConstraints gbc_hybrisServerURLLabel = new GridBagConstraints();
        gbc_hybrisServerURLLabel.insets = new Insets(0, 0, 5, 5);
        gbc_hybrisServerURLLabel.anchor = GridBagConstraints.EAST;
        gbc_hybrisServerURLLabel.gridx = 0;
        gbc_hybrisServerURLLabel.gridy = 0;
        add(hybrisServerURLLabel, gbc_hybrisServerURLLabel);

        hybrisServerURLInput = new JTextField();
        hybrisServerURLLabel.setLabelFor(hybrisServerURLInput);
        GridBagConstraints gbc_hybrisServerURLInput = new GridBagConstraints();
        gbc_hybrisServerURLInput.insets = new Insets(0, 0, 5, 0);
        gbc_hybrisServerURLInput.fill = GridBagConstraints.HORIZONTAL;
        gbc_hybrisServerURLInput.gridx = 1;
        gbc_hybrisServerURLInput.gridy = 0;
        add(hybrisServerURLInput, gbc_hybrisServerURLInput);
        hybrisServerURLInput.setColumns(10);

        JSeparator separator = new JSeparator();
        GridBagConstraints gbc_separator = new GridBagConstraints();
        gbc_separator.insets = new Insets(0, 0, 5, 5);
        gbc_separator.gridx = 0;
        gbc_separator.gridy = 1;
        add(separator, gbc_separator);

        JSeparator separator_1 = new JSeparator();
        GridBagConstraints gbc_separator_1 = new GridBagConstraints();
        gbc_separator_1.insets = new Insets(0, 0, 5, 0);
        gbc_separator_1.gridx = 1;
        gbc_separator_1.gridy = 1;
        add(separator_1, gbc_separator_1);

        JLabel HACUsernameLabel = new JLabel("HAC Username");
        GridBagConstraints gbc_HACUsernameLabel = new GridBagConstraints();
        gbc_HACUsernameLabel.insets = new Insets(0, 0, 5, 5);
        gbc_HACUsernameLabel.anchor = GridBagConstraints.EAST;
        gbc_HACUsernameLabel.gridx = 0;
        gbc_HACUsernameLabel.gridy = 2;
        add(HACUsernameLabel, gbc_HACUsernameLabel);

        HACUsernameInput = new JTextField();
        HACUsernameLabel.setLabelFor(HACUsernameInput);
        GridBagConstraints gbc_HACUsernameInput = new GridBagConstraints();
        gbc_HACUsernameInput.insets = new Insets(0, 0, 5, 0);
        gbc_HACUsernameInput.fill = GridBagConstraints.HORIZONTAL;
        gbc_HACUsernameInput.gridx = 1;
        gbc_HACUsernameInput.gridy = 2;
        add(HACUsernameInput, gbc_HACUsernameInput);
        HACUsernameInput.setColumns(10);

        JLabel HACPasswordLabel = new JLabel("HAC Password");
        GridBagConstraints gbc_HACPasswordLabel = new GridBagConstraints();
        gbc_HACPasswordLabel.anchor = GridBagConstraints.EAST;
        gbc_HACPasswordLabel.insets = new Insets(0, 0, 0, 5);
        gbc_HACPasswordLabel.gridx = 0;
        gbc_HACPasswordLabel.gridy = 3;
        add(HACPasswordLabel, gbc_HACPasswordLabel);

        HACPasswordInput = new JPasswordField();
        HACPasswordLabel.setLabelFor(HACPasswordInput);
        GridBagConstraints gbc_HACPasswordInput = new GridBagConstraints();
        gbc_HACPasswordInput.fill = GridBagConstraints.HORIZONTAL;
        gbc_HACPasswordInput.gridx = 1;
        gbc_HACPasswordInput.gridy = 3;
        add(HACPasswordInput, gbc_HACPasswordInput);

    }

    public JTextField getHybrisServerURLInput() {
        return hybrisServerURLInput;
    }

    public JTextField getHACUsernameInput() {
        return HACUsernameInput;
    }

    public JPasswordField getHACPasswordInput() {
        return HACPasswordInput;
    }

}
