package ui;

import dao.MonAnDAO;
import dao.MonAnNguyenLieuDAO;
import dao.NguyenLieuDAO;
import dao.ThuMucMonAnDAO;
import model.MonAn;
import model.MonAnNguyenLieu;
import model.NguyenLieu;
import model.Thumucmonan;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class MonAnPanel extends JPanel {
    private JTable table;
    private MonAnDAO monAnDAO;
    private NguyenLieuDAO nguyenLieuDAO;
    private ThuMucMonAnDAO thuMucMonAnDAO;
    private MonAnNguyenLieuDAO monAnNguyenLieuDAO;

    public MonAnPanel() {
        monAnDAO = new MonAnDAO(JDBCUtil.getConnection());
        nguyenLieuDAO = new NguyenLieuDAO(JDBCUtil.getConnection());
        thuMucMonAnDAO = new ThuMucMonAnDAO(JDBCUtil.getConnection());
        monAnNguyenLieuDAO = new MonAnNguyenLieuDAO(JDBCUtil.getConnection());

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "Danh sách món ăn", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), Color.DARK_GRAY));

        table = createStyledTable();
        reloadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnThem = createStyledButton("➕ Thêm món ăn");
        btnThem.addActionListener(e -> showFormThem());

        JButton btnXoa = createStyledButton("🗑 Xóa món");
        btnXoa.addActionListener(e -> xoaMonAn());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(245, 245, 245));
        btnPanel.add(btnThem);
        btnPanel.add(btnXoa);
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
        adjustColumnWidths();
    }

    private void xoaMonAn() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "❗ Vui lòng chọn món ăn cần xóa!", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa món ăn này không?\nTất cả nguyên liệu liên quan cũng sẽ bị xóa!",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        int monAnId = (int) table.getValueAt(row, 0);

        boolean xoaNL = monAnNguyenLieuDAO.xoaNguyenLieuTheoMonAn(monAnId);
        boolean xoaMon = monAnDAO.xoaMonAn(monAnId);

        if (xoaMon) {
            JOptionPane.showMessageDialog(this, "✅ Đã xóa món ăn và nguyên liệu liên quan!");
            reloadTable();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ========== showFormThem  ==========
    private void showFormThem() {
        JFrame f = new JFrame("➕ Thêm món ăn");
        f.setSize(800, 600);
        f.getContentPane().setBackground(new Color(240, 240, 240));
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout(10, 10));

        List<Thumucmonan> dsDanhMuc = thuMucMonAnDAO.getAllThuMuc();
        List<NguyenLieu> listNguyenLieu = nguyenLieuDAO.getAllNguyenLieu();
        Map<Integer, JTextField> mapSoLuong = new HashMap<>();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(5, 5, 5, 5);
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weightx = 1.0;

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "Thông tin món ăn", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), Color.DARK_GRAY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField tfTen = new JTextField();
        JTextField tfGia = new JTextField();
        JTextField tfMoTa = new JTextField();
        JLabel lblAnh = new JLabel("Chưa chọn ảnh");
        JLabel lblHinhAnh = new JLabel();
        JButton btnChonAnh = createStyledButton("📁 Chọn ảnh");
        final String[] hinhAnhFile = {""};

        btnChonAnh.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("src/image");
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                hinhAnhFile[0] = selected.getName();
                lblAnh.setText(hinhAnhFile[0]);
                ImageIcon icon = new ImageIcon("src/image/" + hinhAnhFile[0]);
                Image scaled = icon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
                lblHinhAnh.setIcon(new ImageIcon(scaled));
            }
        });

        JComboBox<String> cbDanhMuc = new JComboBox<>();
        Map<String, Integer> mapTenToId = new HashMap<>();
        for (Thumucmonan tm : dsDanhMuc) {
            cbDanhMuc.addItem(tm.getTenthumuc());
            mapTenToId.put(tm.getTenthumuc(), tm.getId());
        }

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

        formPanel.add(lblHinhAnh, gbcAt(gbc, 1, row++));

        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        mainPanel.add(formPanel, gbcMain);

        JPanel nguyenLieuPanel = new JPanel(new GridBagLayout());
        nguyenLieuPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "Nguyên liệu sử dụng", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), Color.DARK_GRAY));
        GridBagConstraints gbcNL = new GridBagConstraints();
        gbcNL.insets = new Insets(3, 10, 3, 10);
        gbcNL.fill = GridBagConstraints.HORIZONTAL;

        int rowNL = 0;
        for (NguyenLieu nl : listNguyenLieu) {
            JLabel lbl = createLabel(nl.getTenNguyenLieu() + " (" + nl.getDonViTinh() + "):");
            JTextField tf = new JTextField(10);
            mapSoLuong.put(nl.getId(), tf);

            gbcNL.gridx = 0; gbcNL.gridy = rowNL;
            nguyenLieuPanel.add(lbl, gbcNL);
            gbcNL.gridx = 1;
            nguyenLieuPanel.add(tf, gbcNL);
            rowNL++;
        }

        JScrollPane scrollPane = new JScrollPane(nguyenLieuPanel);
        gbcMain.gridy = 1;
        mainPanel.add(scrollPane, gbcMain);

        JButton btnLuu = createStyledButton("💾 Lưu");
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
                    int monAnId = mon.getId(); // lấy id món ăn vừa thêm

                    for (Map.Entry<Integer, JTextField> entry : mapSoLuong.entrySet()) {
                        try {
                            double soLuong = Double.parseDouble(entry.getValue().getText());
                            if (soLuong > 0) {
                                MonAnNguyenLieu mal = new MonAnNguyenLieu();
                                mal.setMonAnId(monAnId);
                                mal.setNguyenLieuId(entry.getKey());
                                mal.setSoLuongCan(soLuong);
                                monAnNguyenLieuDAO.themMonAnNguyenLieu(mal); // lưu vào DB
                            }
                        } catch (NumberFormatException ignored) {}
                    }

                    JOptionPane.showMessageDialog(f, "✅ Thêm món thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    reloadTable();
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(f, "❌ Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Lỗi dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 245, 245));
        bottom.add(btnLuu);

        f.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    // =========================== Các hàm phụ trợ ===========================
    private GridBagConstraints gbcAt(GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        return gbc;
    }

    private JTable createStyledTable() {
        JTable t = new JTable();
        JTableHeader header = t.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.DARK_GRAY);
        t.setRowHeight(25);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setShowGrid(true);
        t.setGridColor(new Color(200, 200, 200));
        return t;
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

    public JTable getTable() {
        return table;
    }

    private void adjustColumnWidths() {
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Tên
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Giá
    }
}
