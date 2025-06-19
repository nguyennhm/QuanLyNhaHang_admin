package ui;

import dao.ThuMucMonAnDAO;
import model.Thumucmonan;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ThumucMonAnPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfTen;
    private JButton btnAdd, btnUpdate, btnDelete;
    private ThuMucMonAnDAO dao;

    public ThumucMonAnPanel() {
        dao = new ThuMucMonAnDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Title Panel =====
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(245, 245, 245));
        JLabel titleLabel = new JLabel("Quản lý Thư Mục Món Ăn");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 120, 215));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // ===== Table Panel =====
        model = new DefaultTableModel(new Object[]{"ID", "Tên thư mục"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(Color.DARK_GRAY);
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 200, 200));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(200, 200, 200))
        ));
        add(scrollPane, BorderLayout.CENTER);

        // ===== Input Panel =====
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "Thao tác",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                Color.DARK_GRAY
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        inputPanel.add(createLabel("Tên thư mục:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 2;
        tfTen = new JTextField(20);
        tfTen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfTen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        inputPanel.add(tfTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        btnAdd = createStyledButton("➕ Thêm", new Color(46, 204, 113));
        inputPanel.add(btnAdd, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        btnUpdate = createStyledButton("✏️ Sửa", new Color(52, 152, 219));
        inputPanel.add(btnUpdate, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        btnDelete = createStyledButton("🗑 Xóa", new Color(231, 76, 60));
        inputPanel.add(btnDelete, gbc);

        add(inputPanel, BorderLayout.SOUTH);

        loadData();

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                tfTen.setText(model.getValueAt(row, 1).toString());
            }
        });

        btnAdd.addActionListener(e -> {
            String name = tfTen.getText().trim();
            if (!name.isEmpty()) {
                Thumucmonan thuMuc = new Thumucmonan();
                thuMuc.setTenthumuc(name);
                if (dao.insert(thuMuc)) {
                    tfTen.setText("");
                    loadData();
                    JOptionPane.showMessageDialog(this, "✅ Thêm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Thêm không thành công.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠ Vui lòng nhập tên thư mục.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                String name = tfTen.getText().trim();
                if (!name.isEmpty()) {
                    Thumucmonan thuMuc = new Thumucmonan();
                    thuMuc.setId(id);
                    thuMuc.setTenthumuc(name);
                    if (dao.update(thuMuc)) {
                        tfTen.setText("");
                        loadData();
                        JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Cập nhật không thành công.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "⚠ Tên thư mục không được để trống.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn một dòng để sửa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa thư mục này?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) model.getValueAt(row, 0);
                    if (dao.delete(id)) {
                        tfTen.setText("");
                        loadData();
                        JOptionPane.showMessageDialog(this, "✅ Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Xóa không thành công.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn một dòng để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        List<Thumucmonan> list = dao.getAllThuMuc();
        for (Thumucmonan t : list) {
            model.addRow(new Object[]{t.getId(), t.getTenthumuc()});
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }
}