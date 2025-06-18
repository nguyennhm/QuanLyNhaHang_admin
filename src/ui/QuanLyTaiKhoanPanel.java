package ui;

import javax.swing.*;
import java.awt.*;

public class QuanLyTaiKhoanPanel extends JPanel {
    public QuanLyTaiKhoanPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("🧑‍💼 Quản lý tài khoản", SwingConstants.CENTER), BorderLayout.CENTER);
        // TODO: Bảng danh sách tài khoản, form chỉnh sửa...
    }
}
