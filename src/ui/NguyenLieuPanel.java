package ui;

import dao.NguyenLieuDAO;
import model.NguyenLieu;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)), "Danh sách nguyên liệu", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), Color.DARK_GRAY));

        table = createStyledTable();
        reloadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // === Nút thêm và xóa ===
        JButton btnThem = createStyledButton("➕ Thêm nguyên liệu");
        btnThem.addActionListener(e -> showFormThem());

        JButton btnXoa = createStyledButton("🗑 Xóa nguyên liệu");
        btnXoa.addActionListener(e -> xoaNguyenLieu());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(245, 245, 245));
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
                return false;
            }
        };
        table.setModel(model);
        adjustColumnWidths();
    }

    private void showFormThem() {
        JFrame f = new JFrame("Thêm nguyên liệu");
        f.setSize(400, 300);
        f.setLocationRelativeTo(this);
        f.getContentPane().setBackground(new Color(240, 240, 240));
        f.setLayout(new GridLayout(5, 2, 10, 10));

        JTextField tfTen = new JTextField();
        JComboBox<String> cbDonVi = new JComboBox<>(new String[]{"kg", "quả", "cái", "con", "chai", "lít"});
        JTextField tfSoLuong = new JTextField();
        JTextField tfGiaNhap = new JTextField();

        f.add(createLabel("Tên nguyên liệu:"));
        f.add(tfTen);
        f.add(createLabel("Đơn vị:"));
        f.add(cbDonVi);
        f.add(createLabel("Số lượng:"));
        f.add(tfSoLuong);
        f.add(createLabel("Giá nhập (VNĐ):"));
        f.add(tfGiaNhap);

        JButton btnLuu = createStyledButton("Lưu");
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
                JOptionPane.showMessageDialog(f, "❌ Lỗi nhập liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        f.add(new JLabel());
        f.add(btnLuu);
        f.setVisible(true);
    }

    private void xoaNguyenLieu() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn nguyên liệu cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String ten = table.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa nguyên liệu \"" + ten + "\"?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = dao.xoaNguyenLieu(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Đã xóa nguyên liệu.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                reloadTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Không thể xóa. Nguyên liệu có thể đang được sử dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    private void clearForm(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void adjustColumnWidths() {
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Tên
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Giá nhập
    }
}