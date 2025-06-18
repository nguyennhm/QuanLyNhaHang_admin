package ui;

import dao.TaiKhoanDAO;
import service.MaXacNhanManager;
import utils.HashUtil;

import javax.swing.*;
import java.awt.*;

public class VerifyCodeUI extends JFrame {
    private JTextField codeField;
    private JPasswordField newPasswordField;
    private JButton confirmButton, backButton;
    private String email;

    public VerifyCodeUI(String email) {
        this.email = email;
        setTitle("Xác nhận mã và đổi mật khẩu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Nền xanh
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(66, 133, 244));

        // Form trắng ở giữa
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(320, 240));
        formPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề
        JLabel titleLabel = new JLabel("Xác nhận và đổi mật khẩu");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Mã xác nhận
        gbc.gridy++;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Mã xác nhận:"), gbc);
        gbc.gridx = 1;
        codeField = new JTextField(15);
        formPanel.add(codeField, gbc);

        // Mật khẩu mới
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Mật khẩu mới:"), gbc);
        gbc.gridx = 1;
        newPasswordField = new JPasswordField(15);
        formPanel.add(newPasswordField, gbc);

        // Panel chứa nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        confirmButton = new JButton("Xác nhận");
        confirmButton.setPreferredSize(new Dimension(120, 40));
        confirmButton.setBackground(new Color(30, 90, 180));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.addActionListener(e -> xacNhan());
        buttonPanel.add(confirmButton);

        backButton = new JButton("Quay lại");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(new Color(240, 240, 240));
        backButton.setForeground(Color.BLACK);
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new ForgotPasswordUI();
            dispose();
        });
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        backgroundPanel.add(formPanel);
        add(backgroundPanel);
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
            String hashedPassword = HashUtil.sha256(newPassword);
            TaiKhoanDAO.capNhatMatKhau(email, hashedPassword);
            MaXacNhanManager.xoaMa(email);
            JOptionPane.showMessageDialog(this, "✅ Mật khẩu đã được cập nhật.");
            new LoginUI();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Mã xác nhận không đúng.");
        }
    }

    public static void main(String[] args) {
        new VerifyCodeUI("emailtest@example.com");
    }
}
