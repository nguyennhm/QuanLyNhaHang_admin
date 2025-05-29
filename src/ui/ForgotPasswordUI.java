package ui;

import dao.TaiKhoanDAO;
import service.MaXacNhanManager;
import utils.EmailSender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class ForgotPasswordUI extends JFrame {
    private JTextField emailField;
    private JButton sendRequestButton;
    private JButton backButton;

    public ForgotPasswordUI() {
        setTitle("Quên mật khẩu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Nền xanh
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(66, 133, 244));

        // Khung trắng bo góc
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(300, 200));
        formPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề
        JLabel titleLabel = new JLabel("Quên mật khẩu");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Email
        gbc.gridwidth = 1;
        gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);

        // Panel cho các nút hành động
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Nút gửi yêu cầu
        sendRequestButton = new JButton("Gửi yêu cầu");
        sendRequestButton.setPreferredSize(new Dimension(120, 40));
        sendRequestButton.setBackground(new Color(30, 90, 180)); // Xanh đậm
        sendRequestButton.setForeground(Color.DARK_GRAY);
        sendRequestButton.setFocusPainted(false);
        sendRequestButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendRequestButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendRequestButton.addActionListener(e -> sendRequest());
        sendRequestButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sendRequestButton.setBackground(new Color(20, 70, 150)); // Hover xanh đậm hơn
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sendRequestButton.setBackground(new Color(30, 90, 180));
            }
        });
        buttonPanel.add(sendRequestButton);

        // Nút quay lại
        backButton = new JButton("Quay lại");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(new Color(240, 240, 240)); // Xám nhạt
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new LoginUI();
            dispose();
        });
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(220, 220, 220)); // Hover xám đậm hơn
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(240, 240, 240));
            }
        });
        buttonPanel.add(backButton);

        // Thêm panel nút vào form
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        backgroundPanel.add(formPanel);
        add(backgroundPanel);

        setVisible(true);
    }

    private void sendRequest() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email!");
            return;
        }

        boolean emailExists = TaiKhoanDAO.kiemTraEmailTonTai(email);
        if (emailExists) {
            // Tạo mã xác nhận
            String code = String.format("%06d", new Random().nextInt(999999));

            // Lưu mã tạm thời - có thể lưu trong biến static/class singleton hoặc DB
            MaXacNhanManager.luuMa(email, code);

            try {
                EmailSender.sendEmail(email, "Mã xác nhận khôi phục mật khẩu",
                        "Mã xác nhận của bạn là: " + code);
                JOptionPane.showMessageDialog(this, "✅ Mã xác nhận đã được gửi đến email của bạn.");
                new VerifyCodeUI(email);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi gửi email: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "❌ Email không tồn tại!");
        }
    }

    public static void main(String[] args) {
        new ForgotPasswordUI();
    }
}