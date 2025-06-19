package ui;

import dao.MonAnDAO;
import dao.MonAnNguyenLieuDAO;
import dao.NguyenLieuDAO;
import dao.ThuMucMonAnDAO;
import model.MonAn;
import model.NguyenLieu;
import model.Thumucmonan;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonAnUpdateForm extends JFrame {
    public MonAnUpdateForm(MonAn monAn, Runnable onSuccess) {
        setTitle("✏️ Cập nhật món ăn");
        setSize(700, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(10, 10));

        MonAnDAO monAnDAO = new MonAnDAO(JDBCUtil.getConnection());
        NguyenLieuDAO nguyenLieuDAO = new NguyenLieuDAO(JDBCUtil.getConnection());
        MonAnNguyenLieuDAO malDAO = new MonAnNguyenLieuDAO(JDBCUtil.getConnection());
        ThuMucMonAnDAO thuMucMonAnDAO = new ThuMucMonAnDAO(JDBCUtil.getConnection());

        // ======= Form nhập thông tin =======
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)), "Thông tin món ăn", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), Color.DARK_GRAY));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField tfTen = new JTextField(monAn.getTenMon());
        JTextField tfGia = new JTextField(String.valueOf(monAn.getGia()));
        JTextField tfMoTa = new JTextField(monAn.getMoTa());

        // Ảnh
        JLabel lblAnh = new JLabel(monAn.getHinhAnh());
        JButton btnChonAnh = createStyledButton("📁 Chọn ảnh");
        final String[] hinhAnhFile = {monAn.getHinhAnh()};
        btnChonAnh.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("./images");
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                hinhAnhFile[0] = chooser.getSelectedFile().getName();
                lblAnh.setText(hinhAnhFile[0]);
            }
        });

        // Danh mục
        List<Thumucmonan> danhMucList = thuMucMonAnDAO.getAllThuMuc();
        Map<String, Integer> tenToIdMap = new HashMap<>();
        JComboBox<String> cbDanhMuc = new JComboBox<>();
        for (Thumucmonan tm : danhMucList) {
            cbDanhMuc.addItem(tm.getTenthumuc());
            tenToIdMap.put(tm.getTenthumuc(), tm.getId());
        }
        cbDanhMuc.setSelectedItem(
                danhMucList.stream().filter(dm -> dm.getId() == monAn.getId_thumuc())
                        .map(Thumucmonan::getTenthumuc).findFirst().orElse(null)
        );

        // ===== Thêm các dòng vào form =====
        int row = 0;
        formPanel.add(createLabel("Tên món:"), gbcAt(gbc, 0, row));
        formPanel.add(tfTen, gbcAt(gbc, 1, row++));

        formPanel.add(createLabel("Giá:"), gbcAt(gbc, 0, row));
        formPanel.add(tfGia, gbcAt(gbc, 1, row++));

        formPanel.add(createLabel("Mô tả:"), gbcAt(gbc, 0, row));
        formPanel.add(tfMoTa, gbcAt(gbc, 1, row++));

        formPanel.add(createLabel("Danh mục:"), gbcAt(gbc, 0, row));
        formPanel.add(cbDanhMuc, gbcAt(gbc, 1, row++));

        formPanel.add(btnChonAnh, gbcAt(gbc, 0, row));
        formPanel.add(lblAnh, gbcAt(gbc, 1, row++));

        // ====== Nguyên liệu ======
        JPanel nguyenLieuPanel = new JPanel(new GridBagLayout());
        nguyenLieuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)), "Danh sách nguyên liệu", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), Color.DARK_GRAY));
        GridBagConstraints gbcNL = new GridBagConstraints();
        gbcNL.insets = new Insets(5, 10, 5, 10);
        gbcNL.anchor = GridBagConstraints.WEST;
        gbcNL.fill = GridBagConstraints.HORIZONTAL;
        JScrollPane scrollPane = new JScrollPane(nguyenLieuPanel);

        List<NguyenLieu> list = nguyenLieuDAO.getAllNguyenLieu();
        Map<Integer, Double> mapSoLuongDaCo = malDAO.getNguyenLieuMapByMonAnId(monAn.getId());
        Map<Integer, JTextField> mapTextFields = new HashMap<>();

        int rowNL = 0;
        for (NguyenLieu nl : list) {
            JLabel lbl = createLabel(nl.getTenNguyenLieu() + " (" + nl.getDonViTinh() + "):");
            JTextField tf = new JTextField(10);
            tf.setText(String.valueOf(mapSoLuongDaCo.getOrDefault(nl.getId(), 0.0)));
            mapTextFields.put(nl.getId(), tf);

            gbcNL.gridx = 0;
            gbcNL.gridy = rowNL;
            gbcNL.weightx = 0.6;
            nguyenLieuPanel.add(lbl, gbcNL);

            gbcNL.gridx = 1;
            gbcNL.weightx = 0.4;
            nguyenLieuPanel.add(tf, gbcNL);

            rowNL++;
        }

        // ======= Nút lưu =======
        JButton btnLuu = createStyledButton("💾 Cập nhật");
        btnLuu.setPreferredSize(new Dimension(120, 35));
        btnLuu.addActionListener(e -> {
            try {
                monAn.setTenMon(tfTen.getText());
                monAn.setGia(Double.parseDouble(tfGia.getText()));
                monAn.setMoTa(tfMoTa.getText());
                monAn.setHinhAnh(hinhAnhFile[0]);
                monAn.setTrangThai("con");

                String selectedDanhMuc = (String) cbDanhMuc.getSelectedItem();
                if (selectedDanhMuc != null) {
                    monAn.setId_thumuc(tenToIdMap.get(selectedDanhMuc));
                }

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
                    JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    if (onSuccess != null) onSuccess.run();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(btnLuu);

        // ====== Thêm vào khung chính ======
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private GridBagConstraints gbcAt(GridBagConstraints gbc, int x, int y) {
        GridBagConstraints newGbc = (GridBagConstraints) gbc.clone();
        newGbc.gridx = x;
        newGbc.gridy = y;
        return newGbc;
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

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }
}