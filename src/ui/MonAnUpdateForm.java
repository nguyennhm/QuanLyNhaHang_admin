package ui;

import dao.MonAnDAO;
import dao.MonAnNguyenLieuDAO;
import dao.NguyenLieuDAO;
import model.MonAn;
import model.NguyenLieu;
import utils.JDBCUtil;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonAnUpdateForm extends JFrame {
    public MonAnUpdateForm(MonAn monAn, Runnable onSuccess) {
        setTitle("Cập nhật món ăn");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        MonAnDAO monAnDAO = new MonAnDAO(JDBCUtil.getConnection());
        NguyenLieuDAO nguyenLieuDAO = new NguyenLieuDAO(JDBCUtil.getConnection());
        MonAnNguyenLieuDAO malDAO = new MonAnNguyenLieuDAO(JDBCUtil.getConnection());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField tfTen = new JTextField(monAn.getTenMon());
        JTextField tfGia = new JTextField(String.valueOf(monAn.getGia()));
        JTextField tfMoTa = new JTextField(monAn.getMoTa());
        JLabel lblAnh = new JLabel(monAn.getHinhAnh());
        JButton btnChonAnh = new JButton("Chọn ảnh");
        final String[] hinhAnhFile = {monAn.getHinhAnh()};

        btnChonAnh.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("./images");
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                hinhAnhFile[0] = chooser.getSelectedFile().getName();
                lblAnh.setText(hinhAnhFile[0]);
            }
        });

        formPanel.add(new JLabel("Tên món:")); formPanel.add(tfTen);
        formPanel.add(new JLabel("Giá:")); formPanel.add(tfGia);
        formPanel.add(new JLabel("Mô tả:")); formPanel.add(tfMoTa);
        formPanel.add(btnChonAnh); formPanel.add(lblAnh);

        // Nguyên liệu
        JPanel nguyenLieuPanel = new JPanel();
        nguyenLieuPanel.setLayout(new BoxLayout(nguyenLieuPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(nguyenLieuPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Nguyên liệu"));

        List<NguyenLieu> list = nguyenLieuDAO.getAllNguyenLieu();
        Map<Integer, Double> mapSoLuongDaCo = malDAO.getNguyenLieuMapByMonAnId(monAn.getId());

        Map<Integer, JTextField> mapTextFields = new HashMap<>();

        for (NguyenLieu nl : list) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.add(new JLabel(nl.getTenNguyenLieu() + " (" + nl.getDonViTinh() + "): "));
            JTextField tf = new JTextField(5);
            tf.setText(String.valueOf(mapSoLuongDaCo.getOrDefault(nl.getId(), 0.0)));
            mapTextFields.put(nl.getId(), tf);
            row.add(tf);
            nguyenLieuPanel.add(row);
        }

        JButton btnLuu = new JButton("Cập nhật");
        btnLuu.addActionListener(e -> {
            try {
                monAn.setTenMon(tfTen.getText());
                monAn.setGia(Double.parseDouble(tfGia.getText()));
                monAn.setMoTa(tfMoTa.getText());
                monAn.setHinhAnh(hinhAnhFile[0]);
                monAn.setTrangThai("con");

                boolean ok = monAnDAO.capNhatMonAn(monAn);
                if (ok) {
                    for (Map.Entry<Integer, JTextField> entry : mapTextFields.entrySet()) {
                        int idNL = entry.getKey();
                        double soLuong = Double.parseDouble(entry.getValue().getText());
                        if (!mapSoLuongDaCo.containsKey(idNL) && soLuong > 0) {
                            malDAO.themMonAnNguyenLieu(monAn.getId(), idNL, soLuong);
                        } else {
                            malDAO.capNhatSoLuong(monAn.getId(), idNL, soLuong);
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    if (onSuccess != null) onSuccess.run();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnLuu);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setVisible(true);
    }
}
