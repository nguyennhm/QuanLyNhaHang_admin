package ui;

import dao.NguyenLieuDAO;
import model.NguyenLieu;
import utils.JDBCUtil;

import javax.swing.*;
import java.awt.*;

public class NguyenLieuUpdateForm extends JFrame {
    public NguyenLieuUpdateForm(NguyenLieu nguyenLieu, Runnable onUpdateSuccess) {
        setTitle("Cập nhật nguyên liệu");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        NguyenLieuDAO dao = new NguyenLieuDAO(JDBCUtil.getConnection());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField tfTen = new JTextField(nguyenLieu.getTenNguyenLieu());
        JComboBox<String> cbDonVi = new JComboBox<>(new String[]{"kg", "quả", "cái", "con", "chai"});
        cbDonVi.setSelectedItem(nguyenLieu.getDonViTinh());
        JTextField tfSoLuong = new JTextField(String.valueOf(nguyenLieu.getSoLuongTon()));
        JTextField tfGiaNhap = new JTextField(String.valueOf((int) nguyenLieu.getGiaNhap()));

        formPanel.add(new JLabel("Tên nguyên liệu:")); formPanel.add(tfTen);
        formPanel.add(new JLabel("Đơn vị tính:")); formPanel.add(cbDonVi);
        formPanel.add(new JLabel("Số lượng tồn:")); formPanel.add(tfSoLuong);
        formPanel.add(new JLabel("Giá nhập:")); formPanel.add(tfGiaNhap);

        JButton btnLuu = new JButton("Lưu cập nhật");
        btnLuu.addActionListener(e -> {
            try {
                nguyenLieu.setTenNguyenLieu(tfTen.getText());
                nguyenLieu.setDonViTinh(cbDonVi.getSelectedItem().toString());
                nguyenLieu.setSoLuongTon(Integer.parseInt(tfSoLuong.getText()));
                nguyenLieu.setGiaNhap(Integer.parseInt(tfGiaNhap.getText()));

                boolean ok = dao.capNhatNguyenLieu(nguyenLieu);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!");
                    onUpdateSuccess.run();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Cập nhật thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnLuu);

        add(formPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setVisible(true);
    }
}
