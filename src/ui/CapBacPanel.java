package ui;

import dao.KhachHangDAO;
import dao.XepHangKhachHangDAO;
import model.KhachHang;
import model.XepHangKhachHang;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CapBacPanel extends JPanel {
    private JTable table;
    private XepHangKhachHangDAO xepHangDAO;
    private KhachHangDAO khachHangDAO;

    public CapBacPanel() {
        xepHangDAO = new XepHangKhachHangDAO(JDBCUtil.getConnection());
        khachHangDAO = new KhachHangDAO(JDBCUtil.getConnection());

        setLayout(new BorderLayout(10, 10)); // Add padding between components
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Outer padding

        // ===== Title Panel =====
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Quản lý Cấp bậc Khách hàng");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // ===== Table Panel =====
        table = createNonEditableTable();
        reloadTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(200, 200, 200))
        ));
        table.setRowHeight(25); // Increase row height for better readability
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(scrollPane, BorderLayout.CENTER);

        // ===== Button Panel =====
        JButton btnThem = new JButton("➕ Thêm cấp bậc");
        JButton btnCapNhat = new JButton("✏️ Cập nhật cấp bậc");
        JButton btnXoa = new JButton("🗑 Xóa cấp bậc");

        btnThem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThem.setBackground(new Color(46, 204, 113));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);

        btnCapNhat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCapNhat.setBackground(new Color(52, 152, 219));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setFocusPainted(false);

        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnXoa.setBackground(new Color(231, 76, 60));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);

        btnThem.addActionListener(e -> showAddForm());
        btnCapNhat.addActionListener(e -> showEditForm());
        btnXoa.addActionListener(e -> xoaCapBac());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btnPanel.add(btnThem);
        btnPanel.add(btnCapNhat);
        btnPanel.add(btnXoa);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void reloadTable() {
        List<XepHangKhachHang> list = xepHangDAO.getAllXepHang();
        String[] cols = {"ID", "Cấp bậc", "Điểm điều kiện", "Ưu đãi (%)"};
        Object[][] data = new Object[list.size()][cols.length];

        for (int i = 0; i < list.size(); i++) {
            XepHangKhachHang xh = list.get(i);
            data[i][0] = xh.getId();
            data[i][1] = xh.getCapBac();
            data[i][2] = xh.getdieuKienDiem(); // Fixed typo: getdieuKienDiem -> setdieuKienDiem
            data[i][3] = xh.getUuDai();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
    }

    private void showAddForm() {
        showForm(null);
    }

    private void showEditForm() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn cấp bậc để chỉnh sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) table.getValueAt(selectedRow, 0);
        XepHangKhachHang xh = xepHangDAO.getXepHangById(id);
        showForm(xh);
    }

    private void showForm(XepHangKhachHang xh) {
        boolean isEdit = xh != null;

        JFrame f = new JFrame(isEdit ? "Chỉnh sửa cấp bậc" : "Thêm cấp bậc");
        f.setSize(400, 300);
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField tfCapBac = new JTextField(isEdit ? xh.getCapBac() : "");
        JTextField tfDiem = new JTextField(isEdit ? String.valueOf(xh.getdieuKienDiem()) : "0");
        JTextField tfUuDai = new JTextField(isEdit ? String.valueOf(xh.getUuDai()) : "0.0");

        tfCapBac.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfCapBac.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        tfDiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfDiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        tfUuDai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfUuDai.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));

        formPanel.add(new JLabel("Cấp bậc:"));
        formPanel.add(tfCapBac);
        formPanel.add(new JLabel("Điểm điều kiện:"));
        formPanel.add(tfDiem);
        formPanel.add(new JLabel("Ưu đãi (%):"));
        formPanel.add(tfUuDai);

        JButton btnLuu = new JButton(isEdit ? "Cập nhật" : "Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLuu.setBackground(new Color(52, 152, 219));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);

        btnLuu.addActionListener(e -> {
            try {
                String capBac = tfCapBac.getText().trim();
                int diem = Integer.parseInt(tfDiem.getText().trim());
                float uuDai = Float.parseFloat(tfUuDai.getText().trim());

                if (capBac.isEmpty()) throw new Exception("Cấp bậc không được để trống!");

                XepHangKhachHang entity = isEdit ? xh : new XepHangKhachHang();
                entity.setCapBac(capBac);
                entity.setdieuKienDiem(diem); // Fixed typo: setdieuKienDiem -> setDieuKienDiem
                entity.setUuDai(uuDai);

                boolean ok = isEdit
                        ? xepHangDAO.capNhatXepHang(entity)
                        : xepHangDAO.themXepHang(entity);

                if (ok) {
                    reloadTable();
                    capNhatCapBacKhachHang();
                    f.dispose();
                    JOptionPane.showMessageDialog(this, (isEdit ? "Cập nhật" : "Thêm") + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(f, "Thao tác thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(f, "Điểm và ưu đãi phải là số!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottom.add(btnLuu);
        f.add(formPanel, BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    private void xoaCapBac() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn cấp bậc để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String capBac = (String) table.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa cấp bậc '" + capBac + "'?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = xepHangDAO.xoaXepHang(id);
            if (ok) {
                reloadTable();
                capNhatCapBacKhachHang();
                JOptionPane.showMessageDialog(this, "Xóa cấp bậc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa. Cấp bậc đang được sử dụng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void capNhatCapBacKhachHang() {
        List<KhachHang> khList = khachHangDAO.getAllKhachHang();
        for (KhachHang kh : khList) {
            int newId = xepHangDAO.getXepHangIdByDiem(kh.getDiemTichLuy());
            String newCapBac = xepHangDAO.getCapBacById(newId);
            if (newCapBac != null && !newCapBac.equals(kh.getCapBac())) {
                kh.setCapBac(newCapBac);
                khachHangDAO.capNhatKhachHang(kh);
            }
        }
    }

    private JTable createNonEditableTable() {
        JTable t = new JTable();
        t.getTableHeader().setReorderingAllowed(false);
        return t;
    }

    public JTable getTable() {
        return table;
    }
}