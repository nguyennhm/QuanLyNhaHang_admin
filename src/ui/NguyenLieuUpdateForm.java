package ui;

import dao.NguyenLieuDAO;
import model.NguyenLieu;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class NguyenLieuUpdateForm extends JFrame {
    public NguyenLieuUpdateForm(NguyenLieu nguyenLieu, Runnable onUpdateSuccess) {
        setTitle("✏️ Cập nhật nguyên liệu");
        setSize(400, 300);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(10, 10));

        NguyenLieuDAO dao = new NguyenLieuDAO(JDBCUtil.getConnection());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)), "Thông tin nguyên liệu", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), Color.DARK_GRAY));
        JTextField tfTen = new JTextField(nguyenLieu.getTenNguyenLieu());
        JComboBox<String> cbDonVi = new JComboBox<>(new String[]{"kg", "quả", "cái", "con", "chai"});
        cbDonVi.setSelectedItem(nguyenLieu.getDonViTinh());
        JTextField tfSoLuong = new JTextField(String.valueOf(nguyenLieu.getSoLuongTon()));
        JTextField tfGiaNhap = new JTextField(String.valueOf((int) nguyenLieu.getGiaNhap()));

        formPanel.add(createLabel("Tên nguyên liệu:"), null);
        formPanel.add(tfTen);
        formPanel.add(createLabel("Đơn vị tính:"), null);
        formPanel.add(cbDonVi);
        formPanel.add(createLabel("Số lượng tồn:"), null);
        formPanel.add(tfSoLuong);
        formPanel.add(createLabel("Giá nhập:"), null);
        formPanel.add(tfGiaNhap);

        JButton btnLuu = createStyledButton("💾 Lưu cập nhật");
        btnLuu.addActionListener(e -> {
            try {
                nguyenLieu.setTenNguyenLieu(tfTen.getText().trim());
                nguyenLieu.setDonViTinh(cbDonVi.getSelectedItem().toString());
                nguyenLieu.setSoLuongTon(Double.parseDouble(tfSoLuong.getText()));
                nguyenLieu.setGiaNhap(Integer.parseInt(tfGiaNhap.getText()));

                boolean ok = dao.capNhatNguyenLieu(nguyenLieu);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    onUpdateSuccess.run();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 245, 245));
        bottom.add(btnLuu);

        add(formPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 100, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }
}