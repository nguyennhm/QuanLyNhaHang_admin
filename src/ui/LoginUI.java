package ui;

import dao.TaiKhoanDAO;
import model.TaiKhoan;
import utils.HashUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JButton loginButton;
    private JButton registerButton;
    private JButton forgotPasswordButton;

    public LoginUI() {
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Nền xanh
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(66, 133, 244));
        backgroundPanel.setLayout(new GridBagLayout());

        // Khung trắng bo góc
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(300, 400));
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Thêm logo (thay bằng đường dẫn thực tế)
        JLabel logoLabel = new JLabel(new ImageIcon("path/to/logo.png"));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(logoLabel, gbc);

        // Tiêu đề
        JLabel titleLabel = new JLabel("Đăng nhập");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++;
        formPanel.add(titleLabel, gbc);

        // Email
        gbc.gridwidth = 1;
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

        // Checkbox hiện mật khẩu
        gbc.gridx = 1;
        gbc.gridy++;
        showPasswordCheckBox = new JCheckBox("Hiện mật khẩu");
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.addActionListener(e ->
                passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? (char) 0 : '•')
        );
        formPanel.add(showPasswordCheckBox, gbc);

        // Panel cho các nút hành động
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Nút đăng nhập
        loginButton = new JButton("Đăng nhập");
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(30, 90, 180)); // Xanh đậm hơn
        loginButton.setForeground(Color.DARK_GRAY); // Chữ trắng
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> dangNhap());
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(20, 70, 150)); // hover - đậm hơn nữa
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(30, 90, 180));
            }
        });
        buttonPanel.add(loginButton);


        // Nút đăng ký
        registerButton = new JButton("Đăng ký");
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setBackground(Color.BLUE);
        registerButton.setForeground(Color.DARK_GRAY);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> {
            dispose(); // đóng cửa sổ hiện tại trước
            new RegisterUI(); // mở cửa sổ mới
        });

        buttonPanel.add(registerButton);

        // Thêm panel nút vào form
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Nút quên mật khẩu
        forgotPasswordButton = new JButton("Quên mật khẩu?");
        forgotPasswordButton.setForeground(Color.BLUE);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.addActionListener(e -> {
            dispose(); // Đóng form đăng nhập nếu muốn
            new ForgotPasswordUI();
        });
        gbc.gridy++;
        formPanel.add(forgotPasswordButton, gbc);

        backgroundPanel.add(formPanel);
        add(backgroundPanel);

        setVisible(true);
    }

    private void dangNhap() {
        String email = emailField.getText().trim();
        String matKhau = new String(passwordField.getPassword());

        String hashed = HashUtil.sha256(matKhau);
        TaiKhoan tk = TaiKhoanDAO.kiemTraDangNhap(email, hashed);

        if (tk != null && tk.isTrangThai().equals("mo") && tk.getVaiTro().equals("admin")) {
            JOptionPane.showMessageDialog(this, "✅ Đăng nhập thành công!");
            dispose(); // Đóng hoàn toàn LoginUI
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true); // Mở MainFrame mới
            });
        } else {
            JOptionPane.showMessageDialog(this, "❌ Email hoặc mật khẩu sai!");
        }
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        new LoginUI();
    }
}