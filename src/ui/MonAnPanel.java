package ui;

import dao.MonAnDAO;
import dao.NguyenLieuDAO;
import model.MonAn;
import model.MonAnNguyenLieu;
import model.NguyenLieu;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MonAnPanel extends JPanel {
    private JTable table;
    private MonAnDAO monAnDAO;
    private NguyenLieuDAO nguyenLieuDAO;

    public MonAnPanel() {
        monAnDAO = new MonAnDAO(JDBCUtil.getConnection());
        nguyenLieuDAO = new NguyenLieuDAO(JDBCUtil.getConnection());

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
        String[] cols = {"ID", "Tên", "Giá", "Trạng thái"};
        Object[][] data = new Object[list.size()][cols.length];

        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (int i = 0; i < list.size(); i++) {
            MonAn ma = list.get(i);
            data[i][0] = ma.getId();
            data[i][1] = ma.getTenMon();
            data[i][2] = vndFormat.format(ma.getGia());
            data[i][3] = ma.getTrangThai();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
    }

    private void showFormThem() {
        JFrame f = new JFrame("Thêm món ăn");
        f.setSize(600, 500);
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField tfTen = new JTextField();
        JTextField tfGia = new JTextField();
        JTextField tfMoTa = new JTextField();
        JLabel lblAnh = new JLabel("Chưa chọn ảnh");
        JButton btnChonAnh = new JButton("Chọn ảnh");
        final String[] hinhAnhFile = {""};

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

        JPanel nguyenLieuPanel = new JPanel();
        nguyenLieuPanel.setLayout(new BoxLayout(nguyenLieuPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(nguyenLieuPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Nguyên liệu"));

        List<NguyenLieu> list = nguyenLieuDAO.getAllNguyenLieu();
        Map<Integer, JTextField> mapSoLuong = new HashMap<>();
        for (NguyenLieu nl : list) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.add(new JLabel(nl.getTenNguyenLieu() + ": "));
            JTextField tf = new JTextField(5);
            mapSoLuong.put(nl.getId(), tf);
            row.add(tf);
            nguyenLieuPanel.add(row);
        }

        JButton btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                MonAn mon = new MonAn();
                mon.setTenMon(tfTen.getText());
                mon.setGia(Double.parseDouble(tfGia.getText()));
                mon.setMoTa(tfMoTa.getText());
                mon.setHinhAnh(hinhAnhFile[0]);
                mon.setTrangThai("còn");

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
                                // TODO: thêm vào DAO nếu cần
                            }
                        } catch (Exception ignored) {}
                    }
                    reloadTable();
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(f, "Thêm thất bại!");
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