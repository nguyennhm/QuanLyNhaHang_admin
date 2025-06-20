package ui;

import dao.LuongDAO;
import dao.NhanVienDAO;
import model.Luong;
import model.Nhanvien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private LuongDAO luongDAO;
    private NhanVienDAO nhanVienDAO;
    private Map<String, Integer> tenToIdMap = new HashMap<>();
    private Map<Integer, String> idToTenMap = new HashMap<>();
    private List<Luong> luongList;

    public LuongPanel() {
        luongDAO = new LuongDAO();
        nhanVienDAO = new NhanVienDAO(utils.JDBCUtil.getConnection());

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columns = {
                "Tên nhân viên", "Hệ số lương", "Điểm thưởng",
                "Lương cơ bản", "Tiền thưởng", "Tổng lương"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 30));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        resizeColumnWidth();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addButton = createStyledButton("➕ Thêm");
        JButton editButton = createStyledButton("✏️ Sửa");
        JButton deleteButton = createStyledButton("🗑️ Xóa");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();

        addButton.addActionListener(e -> showLuongDialog("Thêm lương", null));
        editButton.addActionListener(e -> editLuong());
        deleteButton.addActionListener(e -> deleteLuong());
    }

    private void resizeColumnWidth() {
        int[] widths = {180, 100, 100, 120, 120, 130};
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < widths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        tenToIdMap.clear();
        idToTenMap.clear();

        for (Nhanvien nv : nhanVienDAO.getAllNhanVien()) {
            tenToIdMap.put(nv.getTen(), nv.getId_nhanvien());
            idToTenMap.put(nv.getId_nhanvien(), nv.getTen());
        }

        luongList = luongDAO.selectAll();
        for (Luong luong : luongList) {
            String tenNV = idToTenMap.getOrDefault(luong.getIdNhanVien(), "Không rõ");

            float heSoLuong = luong.getHeSoLuong();
            int diemThuong = luong.getDiemThuong();
            float luongCoBan = heSoLuong * 8 * 30;
            float tienThuong = diemThuong * 10000f;
            float tongLuong = luongCoBan + tienThuong;

            tableModel.addRow(new Object[]{
                    tenNV,
                    heSoLuong,
                    diemThuong,
                    String.format("%.0f", luongCoBan),
                    String.format("%.0f", tienThuong),
                    String.format("%.0f", tongLuong)
            });
        }
    }

    private void showLuongDialog(String title, Luong existingLuong) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField heSoLuongField = new JTextField();
        JTextField diemThuongField = new JTextField();
        JComboBox<String> nhanVienCombo = new JComboBox<>(tenToIdMap.keySet().toArray(new String[0]));

        if (existingLuong != null) {
            heSoLuongField.setText(String.valueOf(existingLuong.getHeSoLuong()));
            diemThuongField.setText(String.valueOf(existingLuong.getDiemThuong()));
            nhanVienCombo.setSelectedItem(idToTenMap.get(existingLuong.getIdNhanVien()));
        }

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Hệ số lương:"), gbc);
        gbc.gridx = 1;
        dialog.add(heSoLuongField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Điểm thưởng:"), gbc);
        gbc.gridx = 1;
        dialog.add(diemThuongField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Nhân viên:"), gbc);
        gbc.gridx = 1;
        dialog.add(nhanVienCombo, gbc);

        JButton saveButton = new JButton("💾 Lưu");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            try {
                float heSoLuong = Float.parseFloat(heSoLuongField.getText());
                int diemThuong = Integer.parseInt(diemThuongField.getText());
                String tenNV = (String) nhanVienCombo.getSelectedItem();
                int idNhanVien = tenToIdMap.get(tenNV);

                if (heSoLuong <= 0 || diemThuong < 0) {
                    JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ!");
                    return;
                }

                boolean success;
                if (existingLuong == null) {
                    Luong luong = new Luong(0, heSoLuong, diemThuong, idNhanVien, tenNV);
                    success = luongDAO.insert(luong);
                } else {
                    existingLuong.setHeSoLuong(heSoLuong);
                    existingLuong.setDiemThuong(diemThuong);
                    existingLuong.setIdNhanVien(idNhanVien);
                    existingLuong.setTenNhanVien(tenNV);
                    success = luongDAO.update(existingLuong);
                }

                if (success) {
                    loadData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lưu thất bại!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đúng định dạng số!");
            }
        });

        dialog.setVisible(true);
    }

    private void editLuong() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!");
            return;
        }

        Luong luong = luongList.get(selectedRow);
        showLuongDialog("Sửa lương", luong);
    }

    private void deleteLuong() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = luongList.get(selectedRow).getId();
            if (luongDAO.delete(id)) {
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        return button;
    }
}
