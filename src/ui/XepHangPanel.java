package ui;

import model.XepHangKhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class XepHangPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<XepHangKhachHang> rankList;

    public XepHangPanel() {
        rankList = new ArrayList<>();
        initializeData(); // Initial sample data

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Quản lý cấp bậc khách hàng"));

        table = createNonEditableTable();
        reloadTable();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnThem = new JButton("➕ Thêm cấp bậc");
        btnThem.addActionListener(e -> showAddForm());

        JButton btnSua = new JButton("✏️ Sửa cấp bậc");
        btnSua.addActionListener(e -> showEditForm());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void initializeData() {
        // Sample initial data
        rankList.add(new XepHangKhachHang(1, "Thường", 0, 0.0f));
        rankList.add(new XepHangKhachHang(2, "Bạc", 100, 5.0f));
        rankList.add(new XepHangKhachHang(3, "Vàng", 500, 10.0f));
        rankList.add(new XepHangKhachHang(4, "Kim cương", 1000, 15.0f));
    }

    private JTable createNonEditableTable() {
        String[] cols = {"ID", "Cấp bậc", "Điều kiện điểm", "Ưu đãi (%)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable t = new JTable(tableModel);
        JTableHeader header = t.getTableHeader();
        header.setReorderingAllowed(false);
        return t;
    }

    private void reloadTable() {
        tableModel.setRowCount(0); // Clear existing rows
        for (XepHangKhachHang rank : rankList) {
            Object[] row = {rank.getId(), rank.getCapBac(), rank.getdieuKienDiem(), rank.getUuDai()};
            tableModel.addRow(row);
        }
    }

    private void showAddForm() {
        JFrame f = new JFrame("Thêm cấp bậc");
        f.setSize(300, 200);
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField tfCapBac = new JTextField();
        JTextField tfDieuKienDiem = new JTextField("0");
        JTextField tfUuDai = new JTextField("0.0");

        formPanel.add(new JLabel("Cấp bậc:")); formPanel.add(tfCapBac);
        formPanel.add(new JLabel("Điều kiện điểm:")); formPanel.add(tfDieuKienDiem);
        formPanel.add(new JLabel("Ưu đãi (%):")); formPanel.add(tfUuDai);

        JButton btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                String capBac = tfCapBac.getText().trim();
                int dieuKienDiem = Integer.parseInt(tfDieuKienDiem.getText().trim());
                float uuDai = Float.parseFloat(tfUuDai.getText().trim());
                int newId = rankList.stream().mapToInt(XepHangKhachHang::getId).max().orElse(0) + 1;

                XepHangKhachHang newRank = new XepHangKhachHang(newId, capBac, dieuKienDiem, uuDai);
                rankList.add(newRank);
                reloadTable();
                f.dispose();
                JOptionPane.showMessageDialog(this, "Thêm cấp bậc thành công!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(f, "Điều kiện điểm và ưu đãi phải là số hợp lệ!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Lỗi: " + ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnLuu);

        f.add(formPanel, BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    private void showEditForm() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn cấp bậc để sửa.");
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        XepHangKhachHang rank = rankList.stream().filter(r -> r.getId() == id).findFirst().get();

        JFrame f = new JFrame("Sửa cấp bậc");
        f.setSize(300, 200);
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField tfCapBac = new JTextField(rank.getCapBac());
        JTextField tfDieuKienDiem = new JTextField(String.valueOf(rank.getdieuKienDiem()));
        JTextField tfUuDai = new JTextField(String.valueOf(rank.getUuDai()));

        formPanel.add(new JLabel("Cấp bậc:")); formPanel.add(tfCapBac);
        formPanel.add(new JLabel("Điều kiện điểm:")); formPanel.add(tfDieuKienDiem);
        formPanel.add(new JLabel("Ưu đãi (%):")); formPanel.add(tfUuDai);

        JButton btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                rank.setCapBac(tfCapBac.getText().trim());
                rank.setdieuKienDiem(Integer.parseInt(tfDieuKienDiem.getText().trim()));
                rank.setUuDai(Float.parseFloat(tfUuDai.getText().trim()));
                reloadTable();
                f.dispose();
                JOptionPane.showMessageDialog(this, "Sửa cấp bậc thành công!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(f, "Điều kiện điểm và ưu đãi phải là số hợp lệ!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Lỗi: " + ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnLuu);

        f.add(formPanel, BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    private JTable getTable() {
        return table;
    }
}