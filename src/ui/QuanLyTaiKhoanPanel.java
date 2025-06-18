package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class QuanLyTaiKhoanPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private NhanVienPanel nhanVienPanel;
    private KhachHangPanel khachHangPanel;

    public QuanLyTaiKhoanPanel() {
        setLayout(new BorderLayout());

        // Panel chứa các nút chuyển đổi
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnNhanVien = new JButton("Nhân viên");
        JButton btnKhachHang = new JButton("Khách hàng");
        switchPanel.add(btnNhanVien);
        switchPanel.add(btnKhachHang);
        add(switchPanel, BorderLayout.NORTH);

        // Tạo các panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        nhanVienPanel = new NhanVienPanel();
        khachHangPanel = new KhachHangPanel();

        cardPanel.add(nhanVienPanel, "nhanvien");
        cardPanel.add(khachHangPanel, "khachhang");
        add(cardPanel, BorderLayout.CENTER);

        // Xử lý nút chuyển panel
        btnNhanVien.addActionListener(e -> {
            nhanVienPanel.reloadTable();
            cardLayout.show(cardPanel, "nhanvien");
        });

        btnKhachHang.addActionListener(e -> {
            khachHangPanel.reloadTable();
            cardLayout.show(cardPanel, "khachhang");
        });

        cardLayout.show(cardPanel, "nhanvien");

        // Sự kiện click bảng nhân viên
        nhanVienPanel.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = nhanVienPanel.getTable().getSelectedRow();
                    if (row != -1) {
                        Object value = nhanVienPanel.getTable().getValueAt(row, 0);
                        if (value instanceof Number) {
                            int nhanVienId = ((Number) value).intValue();
                            nhanVienPanel.showDetailForm(nhanVienId);
                        }
                    }
                }
            }
        });

        // Sự kiện click bảng khách hàng
        khachHangPanel.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = khachHangPanel.getTable().getSelectedRow();
                    if (row != -1) {
                        Object value = khachHangPanel.getTable().getValueAt(row, 0);
                        if (value instanceof Number) {
                            int khachHangId = ((Number) value).intValue();
                            khachHangPanel.showDetailForm(khachHangId);
                        }
                    }
                }
            }
        });
    }
}