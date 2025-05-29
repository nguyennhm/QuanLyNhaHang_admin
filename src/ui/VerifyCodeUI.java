package ui;

import dao.TaiKhoanDAO;
import service.MaXacNhanManager;

import javax.swing.*;
import java.awt.*;

public class VerifyCodeUI extends JFrame {
    private JTextField codeField;
    private JPasswordField newPasswordField;
    private JButton confirmButton;
    private String email;

    public VerifyCodeUI(String email) {
        this.email = email;
        setTitle("Xác thực mã");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        add(new JLabel("Nhập mã xác nhận đã gửi về email:"));
        codeField = new JTextField();
        add(codeField);

        add(new JLabel("Nhập mật khẩu mới:"));
        newPasswordField = new JPasswordField();
        add(newPasswordField);

        confirmButton = new JButton("Xác nhận");
        confirmButton.addActionListener(e -> xacNhan());
        add(confirmButton);

        setVisible(true);
    }

    private void xacNhan() {
        String code = codeField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();

        if (code.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        if (MaXacNhanManager.kiemTraMa(email, code)) {
            TaiKhoanDAO.capNhatMatKhau(email, newPassword);
            MaXacNhanManager.xoaMa(email);
            JOptionPane.showMessageDialog(this, "✅ Mật khẩu đã được cập nhật.");
            new LoginUI();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Mã xác nhận không đúng.");
        }
    }
}
