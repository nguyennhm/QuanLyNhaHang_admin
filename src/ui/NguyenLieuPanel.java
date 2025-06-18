package ui;

import dao.NguyenLieuDAO;
import model.NguyenLieu;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class NguyenLieuPanel extends JPanel {
    private JTable table;
    private NguyenLieuDAO dao;

    public NguyenLieuPanel() {
        dao = new NguyenLieuDAO(JDBCUtil.getConnection());
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Danh sách nguyên liệu"));

        table = createNonEditableTable();
        reloadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // === Nút thêm và xóa ===
        JButton btnThem = new JButton("➕ Thêm nguyên liệu");
        btnThem.addActionListener(e -> showFormThem());

        JButton btnXoa = new JButton("🗑 Xóa nguyên liệu");
        btnXoa.addActionListener(e -> xoaNguyenLieu());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnThem);
        btnPanel.add(btnXoa);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void reloadTable() {
        List<NguyenLieu> list = dao.getAllNguyenLieu();
        String[] cols = {"ID", "Tên", "Đơn vị", "Tồn kho", "Giá nhập"};
        Object[][] data = new Object[list.size()][cols.length];

        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (int i = 0; i < list.size(); i++) {
            NguyenLieu nl = list.get(i);
            data[i][0] = nl.getId();
            data[i][1] = nl.getTenNguyenLieu();
            data[i][2] = nl.getDonViTinh();
            data[i][3] = nl.getSoLuongTon();
            data[i][4] = vndFormat.format(nl.getGiaNhap());
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // khóa chỉnh sửa
            }
        };
        table.setModel(model);
    }

    private void showFormThem() {
        JFrame f = new JFrame("Thêm nguyên liệu");
        f.setSize(400, 300);
        f.setLocationRelativeTo(this);
        f.setLayout(new GridLayout(5, 2, 10, 10));

        JTextField tfTen = new JTextField();
        JComboBox<String> cbDonVi = new JComboBox<>(new String[]{"kg", "quả", "cái", "con", "chai", "lít"});
        JTextField tfSoLuong = new JTextField();
        JTextField tfGiaNhap = new JTextField();

        f.add(new JLabel("Tên nguyên liệu:")); f.add(tfTen);
        f.add(new JLabel("Đơn vị:")); f.add(cbDonVi);
        f.add(new JLabel("Số lượng:")); f.add(tfSoLuong);
        f.add(new JLabel("Giá nhập (VNĐ):")); f.add(tfGiaNhap);

        JButton btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                NguyenLieu nl = new NguyenLieu();
                nl.setTenNguyenLieu(tfTen.getText().trim());
                nl.setDonViTinh(cbDonVi.getSelectedItem().toString());
                nl.setSoLuongTon(Double.parseDouble(tfSoLuong.getText()));
                nl.setGiaNhap(Integer.parseInt(tfGiaNhap.getText()));
                dao.themNguyenLieu(nl);
                reloadTable();
                clearForm(tfTen, tfSoLuong, tfGiaNhap);
                f.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "❌ Lỗi nhập liệu: " + ex.getMessage());
            }
        });

        f.add(new JLabel()); f.add(btnLuu);
        f.setVisible(true);
    }

    private void xoaNguyenLieu() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn nguyên liệu cần xóa.");
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String ten = table.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa nguyên liệu \"" + ten + "\"?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = dao.xoaNguyenLieu(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Đã xóa nguyên liệu.");
                reloadTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Không thể xóa. Nguyên liệu có thể đang được sử dụng.");
            }
        }
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

    private void clearForm(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
}
