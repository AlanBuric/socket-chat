package com.alanburic.socketchat.client.panels;

import com.alanburic.socketchat.client.frames.ClientFrame;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import static com.alanburic.socketchat.client.utils.SwingCommons.FONT;
import static com.alanburic.socketchat.client.utils.SwingCommons.PADDING;

public class ChatPanel extends JPanel {

    private final JTextArea chatArea;
    private final JTextField inputField;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatPanel(@NotNull ClientFrame controller, @NotNull String address, int port) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(PADDING);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(FONT);

        var chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        chatScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        var infoLabel = new JLabel("Type 'quit' or 'exit' or close this window to disconnect.");
        infoLabel.setFont(FONT);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputField = new JTextField();
        inputField.setFont(FONT);

        var sendButton = new JButton("Send");
        sendButton.setFont(FONT);

        inputField.addActionListener(e -> sendMessage());
        sendButton.addActionListener(e -> sendMessage());

        var sendButtonPanel = new JPanel(new BorderLayout());
        sendButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        sendButtonPanel.add(sendButton, BorderLayout.CENTER);

        var inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButtonPanel, BorderLayout.EAST);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, inputPanel.getPreferredSize().height));
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(chatScrollPane);
        add(Box.createVerticalStrut(10));
        add(infoLabel);
        add(Box.createVerticalStrut(10));
        add(inputPanel);

        setVisible(true);
        connectToServer(controller, address, port);
    }

    private void disconnect() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();

        disconnect();
    }

    private void connectToServer(@NotNull ClientFrame controller, @NotNull String address, int port) {
        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            SwingUtilities.invokeLater(inputField::requestFocusInWindow);

            new Thread(() -> listenForMessages(controller)).start();
        } catch (UnknownHostException exception) {
            SwingUtilities.invokeLater(() -> {
                controller.showInput();
                JOptionPane.showMessageDialog(this, "Host %s:%d not found.".formatted(address, port), "Error",
                        JOptionPane.ERROR_MESSAGE);
            });
        } catch (Exception exception) {
            exception.printStackTrace();

            SwingUtilities.invokeLater(() -> {
                controller.showInput();
                JOptionPane.showMessageDialog(this,
                        "An error occurred while connecting to host %s:%d: %s".formatted(address, port,
                                exception.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    private void listenForMessages(@NotNull ClientFrame controller) {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                chatArea.append(line + "\n");
            }
        } catch (SocketException exception) {
            exception.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                controller.showInput();
                JOptionPane.showMessageDialog(this, "Connection to the server was lost.", "Note",
                        JOptionPane.INFORMATION_MESSAGE);
            });
        } catch (Exception exception) {
            exception.printStackTrace();

            SwingUtilities.invokeLater(() -> {
                controller.showInput();
                JOptionPane.showMessageDialog(this,
                        "An error occurred while reading messages: %s".formatted(exception.getMessage()), "Error",
                        JOptionPane.ERROR_MESSAGE);
            });
        }

        disconnect();
    }

    private void sendMessage() {
        final var msg = inputField.getText().trim();

        if (!msg.isEmpty() && out != null) {
            if (msg.equalsIgnoreCase("exit") || msg.equalsIgnoreCase("quit")) {
                disconnect();
                return;
            }

            out.println(msg);
            inputField.setText("");
        }
    }
}