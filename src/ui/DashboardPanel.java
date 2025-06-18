package ui;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Chào mừng đến với Dashboard 👋", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
