package com.alanburic.socketchat.client.panels;

import com.alanburic.socketchat.client.frames.ClientFrame;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.alanburic.socketchat.client.utils.SwingCommons.FONT;
import static com.alanburic.socketchat.client.utils.SwingCommons.PADDING;

public class AddressInputPanel extends JPanel {

    public AddressInputPanel(@NotNull ClientFrame controller) {
        setLayout(new BorderLayout());
        setBorder(PADDING);

        var inputPanel = new JPanel(new GridBagLayout());
        var addressLabel = new JLabel("Server address:");
        var addressField = new JTextField();
        var portLabel = new JLabel("Server port:");
        var portField = new JTextField();

        List.of(addressLabel, addressField, portLabel, portField).forEach(component -> component.setFont(FONT));

        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.20;
        inputPanel.add(addressLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.80;
        inputPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.20;
        inputPanel.add(portLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.80;
        inputPanel.add(portField, gbc);

        var title = new JLabel("Establish a connection", SwingConstants.CENTER);
        title.setFont(FONT);

        add(title, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createConnectButton(controller, addressField, portField), BorderLayout.SOUTH);

        addressField.requestFocusInWindow();
    }

    private @NotNull JButton createConnectButton(@NotNull ClientFrame controller, JTextField addressField,
                                                 JTextField portField) {
        var connectButton = new JButton("Connect");
        connectButton.setFont(FONT);

        connectButton.addActionListener(e -> {
            final var address = addressField.getText().trim();
            final var portText = portField.getText().trim();

            if (address.isEmpty() || portText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both address and port.", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int port;

            try {
                port = Integer.parseUnsignedInt(portText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid port number.", "Input error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            controller.connectToServer(address, port);
        });

        return connectButton;
    }
}