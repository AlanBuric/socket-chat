package com.alanburic.socketchat.client;

import com.alanburic.socketchat.client.frames.ClientFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        SwingUtilities.invokeLater(ClientFrame::new);
    }
}