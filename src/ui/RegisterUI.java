package ui;

import dao.TaiKhoanDAO;
import model.TaiKhoan;
import utils.HashUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterUI extends JFrame {
    private JTextField hoTenField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JCheckBox showPasswordCheckBox;
    private JButton registerButton, backButton;

    public RegisterUI() {
        setTitle("Đăng ký tài khoản");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(66, 133, 244));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(300, 400));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề
        JLabel titleLabel = new JLabel("Tạo tài khoản");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Họ tên
        gbc.gridy++;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1;
        hoTenField = new JTextField(15);
        formPanel.add(hoTenField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);

        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        // Nhập lại mật khẩu
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Nhập lại mật khẩu:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        formPanel.add(confirmPasswordField, gbc);

        // Checkbox hiện mật khẩu
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        showPasswordCheckBox = new JCheckBox("Hiện mật khẩu");
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.addActionListener(e -> {
            char echo = showPasswordCheckBox.isSelected() ? (char) 0 : '•';
            passwordField.setEchoChar(echo);
            confirmPasswordField.setEchoChar(echo);
        });
        formPanel.add(showPasswordCheckBox, gbc);

        // Panel cho hai nút nằm ngang
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Nút đăng ký
        registerButton = new JButton("Đăng ký");
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setBackground(Color.BLUE);
        registerButton.setForeground(Color.DARK_GRAY);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> dangKy()); // Gọi hàm xử lý
        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(20, 70, 150));
            }

            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(30, 90, 180));
            }
        });
        buttonPanel.add(registerButton);

        // Nút quay lại đăng nhập
        backButton = new JButton("Quay lại");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setForeground(Color.DARK_GRAY);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new LoginUI();
            dispose();
        });
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(220, 220, 220));
            }

            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(240, 240, 240));
            }
        });
        buttonPanel.add(backButton);

        // Thêm panel nút vào form
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        backgroundPanel.add(formPanel);
        add(backgroundPanel);
        setVisible(true);
    }

    private void dangKy() {
        String hoTen = hoTenField.getText().trim();
        String email = emailField.getText().trim();
        String matKhau = new String(passwordField.getPassword());
        String xacNhan = new String(confirmPasswordField.getPassword());

        if (hoTen.isEmpty() || email.isEmpty() || matKhau.isEmpty() || xacNhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        String gmailRegex = "^[\\w.+\\-]+@gmail\\.com$";
        if (!email.matches(gmailRegex)) {
            JOptionPane.showMessageDialog(this, "Email phải có định dạng @gmail.com hợp lệ!");
            return;
        }

        if (!matKhau.equals(xacNhan)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không khớp!");
            return;
        }

        if (TaiKhoanDAO.kiemTraEmailTonTai(email)) {
            JOptionPane.showMessageDialog(this, "Email đã tồn tại!");
            return;
        }

        String hashed = HashUtil.sha256(matKhau);
        TaiKhoan taiKhoan = new TaiKhoan(email, hashed, "admin", "mo");
        boolean thanhCong = TaiKhoanDAO.dangKyTaiKhoan(taiKhoan);

        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "✅ Đăng ký thành công! Vui lòng đăng nhập.");
            new LoginUI();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Đăng ký thất bại!");
        }
    }

    public static void main(String[] args) {
        new RegisterUI();
    }
}
