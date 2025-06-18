package ui;

import dao.MonAnDAO;
import dao.NguyenLieuDAO;
import dao.ThuMucMonAnDAO;
import model.MonAn;
import model.MonAnNguyenLieu;
import model.NguyenLieu;
import model.Thumucmonan;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class MonAnPanel extends JPanel {
    private JTable table;
    private MonAnDAO monAnDAO;
    private NguyenLieuDAO nguyenLieuDAO;
    private ThuMucMonAnDAO thuMucMonAnDAO;

    public MonAnPanel() {
        monAnDAO = new MonAnDAO(JDBCUtil.getConnection());
        nguyenLieuDAO = new NguyenLieuDAO(JDBCUtil.getConnection());
        thuMucMonAnDAO = new ThuMucMonAnDAO(JDBCUtil.getConnection());

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Danh sách món ăn"));

        table = createNonEditableTable();
        reloadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnThem = new JButton("➕ Thêm món ăn");
        btnThem.addActionListener(e -> showFormThem());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnThem);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void reloadTable() {
        List<MonAn> list = monAnDAO.getAllMonAn();
        Map<Integer, String> mapDanhMuc = thuMucMonAnDAO.getMapDanhMuc();

        String[] cols = {"ID", "Tên", "Giá", "Trạng thái", "Danh mục"};
        Object[][] data = new Object[list.size()][cols.length];
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (int i = 0; i < list.size(); i++) {
            MonAn ma = list.get(i);
            data[i][0] = ma.getId();
            data[i][1] = ma.getTenMon();
            data[i][2] = vndFormat.format(ma.getGia());
            data[i][3] = ma.getTrangThai();
            data[i][4] = mapDanhMuc.getOrDefault(ma.getId_thumuc(), "Không rõ");
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
    }

    private void showFormThem() {
        JFrame f = new JFrame("➕ Thêm món ăn");
        f.setSize(700, 600);
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout(10, 10));

        List<Thumucmonan> dsDanhMuc = thuMucMonAnDAO.getAllThuMuc();
        List<NguyenLieu> listNguyenLieu = nguyenLieuDAO.getAllNguyenLieu();
        Map<Integer, JTextField> mapSoLuong = new HashMap<>();

        // === FORM món ăn ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin món ăn"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField tfTen = new JTextField();
        JTextField tfGia = new JTextField();
        JTextField tfMoTa = new JTextField();
        JLabel lblAnh = new JLabel("Chưa chọn ảnh");
        JButton btnChonAnh = new JButton("📁 Chọn ảnh");
        final String[] hinhAnhFile = {""};

        btnChonAnh.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("./images");
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                hinhAnhFile[0] = chooser.getSelectedFile().getName();
                lblAnh.setText(hinhAnhFile[0]);
            }
        });

        JComboBox<String> cbDanhMuc = new JComboBox<>();
        Map<String, Integer> mapTenToId = new HashMap<>();
        for (Thumucmonan tm : dsDanhMuc) {
            cbDanhMuc.addItem(tm.getTenthumuc());
            mapTenToId.put(tm.getTenthumuc(), tm.getId());
        }

        int row = 0;
        formPanel.add(new JLabel("Tên món:"), gbcAt(gbc, 0, row));
        formPanel.add(tfTen, gbcAt(gbc, 1, row++));

        formPanel.add(new JLabel("Giá:"), gbcAt(gbc, 0, row));
        formPanel.add(tfGia, gbcAt(gbc, 1, row++));

        formPanel.add(new JLabel("Mô tả:"), gbcAt(gbc, 0, row));
        formPanel.add(tfMoTa, gbcAt(gbc, 1, row++));

        formPanel.add(new JLabel("Danh mục:"), gbcAt(gbc, 0, row));
        formPanel.add(cbDanhMuc, gbcAt(gbc, 1, row++));

        formPanel.add(btnChonAnh, gbcAt(gbc, 0, row));
        formPanel.add(lblAnh, gbcAt(gbc, 1, row++));

        // === DANH SÁCH NGUYÊN LIỆU ===
        JPanel nguyenLieuPanel = new JPanel(new GridBagLayout());
        nguyenLieuPanel.setBorder(BorderFactory.createTitledBorder("Nguyên liệu sử dụng"));
        GridBagConstraints gbcNL = new GridBagConstraints();
        gbcNL.insets = new Insets(3, 10, 3, 10);
        gbcNL.fill = GridBagConstraints.HORIZONTAL;

        int rowNL = 0;
        for (NguyenLieu nl : listNguyenLieu) {
            JLabel lbl = new JLabel(nl.getTenNguyenLieu() + " (" + nl.getDonViTinh() + "):");
            JTextField tf = new JTextField(10);
            mapSoLuong.put(nl.getId(), tf);

            gbcNL.gridx = 0; gbcNL.gridy = rowNL; gbcNL.weightx = 0.5;
            nguyenLieuPanel.add(lbl, gbcNL);
            gbcNL.gridx = 1; gbcNL.weightx = 0.5;
            nguyenLieuPanel.add(tf, gbcNL);
            rowNL++;
        }

        JScrollPane scrollPane = new JScrollPane(nguyenLieuPanel);

        // === NÚT LƯU ===
        JButton btnLuu = new JButton("💾 Lưu");
        btnLuu.setPreferredSize(new Dimension(120, 35));
        btnLuu.addActionListener(e -> {
            try {
                MonAn mon = new MonAn();
                mon.setTenMon(tfTen.getText());
                mon.setGia(Double.parseDouble(tfGia.getText()));
                mon.setMoTa(tfMoTa.getText());
                mon.setHinhAnh(hinhAnhFile[0]);
                mon.setTrangThai("còn");

                String tenDanhMuc = (String) cbDanhMuc.getSelectedItem();
                int idThuMuc = mapTenToId.getOrDefault(tenDanhMuc, -1);
                mon.setId_thumuc(idThuMuc);

                boolean ok = monAnDAO.themMonAn(mon);
                if (ok) {
                    for (Map.Entry<Integer, JTextField> entry : mapSoLuong.entrySet()) {
                        try {
                            int soLuong = Integer.parseInt(entry.getValue().getText());
                            if (soLuong > 0) {
                                MonAnNguyenLieu mal = new MonAnNguyenLieu();
                                mal.setMonAnId(mon.getId());
                                mal.setNguyenLieuId(entry.getKey());
                                mal.setSoLuongCan(soLuong);
                                // TODO: Thêm dòng này nếu có DAO: monAnDAO.themNguyenLieu(mal);
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                    JOptionPane.showMessageDialog(f, "✅ Thêm món thành công!");
                    reloadTable();
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(f, "❌ Thêm thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Lỗi dữ liệu: " + ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnLuu);

        f.add(formPanel, BorderLayout.NORTH);
        f.add(scrollPane, BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    private GridBagConstraints gbcAt(GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        return gbc;
    }

    private JTable createNonEditableTable() {
        JTable t = new JTable();
        JTableHeader header = t.getTableHeader();
        header.setReorderingAllowed(false);
        return t;
    }

    public JTable getTable() {
        return table;
    }
}
