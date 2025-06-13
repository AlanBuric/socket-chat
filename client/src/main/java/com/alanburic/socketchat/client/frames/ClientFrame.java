package com.alanburic.socketchat.client.frames;

import com.alanburic.socketchat.client.panels.ChatPanel;
import com.alanburic.socketchat.client.panels.LoadingPanel;
import com.alanburic.socketchat.client.panels.AddressInputPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientFrame extends JFrame {

    private boolean inChat = false;

    public ClientFrame() {
        setTitle("Broadcast chat client");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (inChat) {
                    showInput();
                    inChat = false;
                } else {
                    dispose();
                    System.exit(0);
                }
            }
        });

        showInput();
        setVisible(true);
    }

    public void showInput() {
        inChat = false;
        setContentPane(new AddressInputPanel(this));
        revalidate();
        repaint();
    }

    private void showLoading() {
        inChat = false;
        setContentPane(new LoadingPanel());
        revalidate();
        repaint();
    }

    public void connectToServer(@NotNull String address, int port) {
        showLoading();

        SwingUtilities.invokeLater(() -> {
            inChat = true;
            setContentPane(new ChatPanel(this, address, port));
            revalidate();
            repaint();
        });
    }
}
