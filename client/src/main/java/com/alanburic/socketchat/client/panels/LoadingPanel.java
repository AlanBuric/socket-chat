package com.alanburic.socketchat.client.panels;

import javax.swing.*;
import java.awt.*;

import static com.alanburic.socketchat.client.utils.SwingCommons.FONT;

public class LoadingPanel extends JPanel {

    public LoadingPanel() {
        setLayout(new BorderLayout());

        var label = new JLabel("Connecting...", SwingConstants.CENTER);
        label.setFont(FONT);

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        add(label, BorderLayout.CENTER);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        setCursor(Cursor.getDefaultCursor());
    }
}