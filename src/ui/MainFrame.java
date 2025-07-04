package ui;

import dao.MonAnDAO;
import utils.JDBCUtil;
import utils.TrangThaiMonAnScheduler;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainFrame extends JFrame {
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Trang quản trị");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== HEADER (Thanh tiêu đề trên cùng) =====
        JLabel headerLabel = new JLabel("HỆ THỐNG QUẢN TRỊ", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(33, 64, 125));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setPreferredSize(new Dimension(getWidth(), 60));
        add(headerLabel, BorderLayout.NORTH);

        // ===== MENU BÊN TRÁI =====
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(10, 1, 0, 5));
        menuPanel.setPreferredSize(new Dimension(220, 0));
        menuPanel.setBackground(new Color(44, 62, 80));

        JButton btnTaiKhoan = createMenuButton("Quản lý tài khoản", "/user.png");
        JButton btnLuong = createMenuButton("Lương nhân viên", "/luong.jpg");
        JButton btnNhapSanPham = createMenuButton("Nhập sản phẩm", "/box.jpg");
        JButton btnThuMuc = createMenuButton("Danh sách thư mục", "/thumuc.jpg");
        JButton btnThongKe = createMenuButton("Thống kê", "/thumuc.jpg");
        JButton btnRanking = createMenuButton("Xếp hạng", "/ranking.png");
        JButton btnOrder = createMenuButton("Đơn hàng", "/order.png");

        menuPanel.add(btnThongKe);
        menuPanel.add(btnTaiKhoan);
        menuPanel.add(btnNhapSanPham);
        menuPanel.add(btnRanking);
        menuPanel.add(btnOrder);
        menuPanel.add(btnLuong);
        menuPanel.add(btnThuMuc);
        add(menuPanel, BorderLayout.WEST);

        // ===== CONTENT HIỂN THỊ =====
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        add(contentPanel, BorderLayout.CENTER);

        // ===== HIỂN THỊ MẶC ĐỊNH =====
        showPanel(new ThongKePanel());

        // ===== XỬ LÝ NHẤN MENU =====
        btnThongKe.addActionListener(e -> showPanel(new ThongKePanel()));
        btnTaiKhoan.addActionListener(e -> showPanel(new QuanLyTaiKhoanPanel()));
        btnNhapSanPham.addActionListener(e -> showPanel(new NhapSanPhamPanel()));
        btnRanking.addActionListener(e -> showPanel(new CapBacPanel()));
        btnOrder.addActionListener(e -> showPanel(new DonHangPanel()));
        btnLuong.addActionListener(e -> showPanel(new LuongPanel()));
        btnThuMuc.addActionListener(e -> showPanel(new ThumucMonAnPanel()));
        btnThongKe.addActionListener(e -> showPanel(new ThongKePanel()));

        // ===== KHỞI ĐỘNG TỰ ĐỘNG CẬP NHẬT TRẠNG THÁI MÓN ĂN =====
        MonAnDAO monAnDAO = new MonAnDAO(JDBCUtil.getConnection());
        TrangThaiMonAnScheduler scheduler = new TrangThaiMonAnScheduler(monAnDAO);
        scheduler.start();


        setVisible(true);
    }

    private JButton createMenuButton(String text, String iconPath) {
        ImageIcon icon = null;

        URL iconURL = getClass().getResource(iconPath);
        if (iconURL != null) {
            Image img = new ImageIcon(iconURL).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        } else {
            System.err.println("❌ Không tìm thấy icon: " + iconPath);
        }

        JButton button = new JButton(text, icon);
        button.setFocusPainted(false);
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(33, 150, 243));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });

        return button;
    }

    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
