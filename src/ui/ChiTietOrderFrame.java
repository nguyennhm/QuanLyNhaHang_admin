package ui;

import dao.ChiTietOrderDAO;
import model.ChiTietOrder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ChiTietOrderFrame extends JFrame {
    private JTable tableChiTiet;

    public ChiTietOrderFrame(int orderId) {
        setTitle("Chi tiết đơn hàng #" + orderId);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        JLabel lblTitle = new JLabel("Chi tiết đơn hàng #" + orderId, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 120, 215));
        lblTitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 0),
                BorderFactory.createLineBorder(new Color(200, 200, 200))
        ));
        add(lblTitle, BorderLayout.NORTH);

        tableChiTiet = createStyledTable();
        loadChiTietOrder(orderId);
        add(new JScrollPane(tableChiTiet), BorderLayout.CENTER);

        JButton btnClose = createStyledButton("Đóng");
        btnClose.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(btnClose);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadChiTietOrder(int orderId) {
        ChiTietOrderDAO dao = new ChiTietOrderDAO();
        List<ChiTietOrder> list = dao.getChiTietByOrderId(orderId);
        String[] cols = {"ID", "Tên món ăn", "Số lượng", "Trạng thái"};
        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            ChiTietOrder ct = list.get(i);
            data[i][0] = ct.getId();
            data[i][1] = ct.getTenMon();
            data[i][2] = ct.getSoLuong();
            data[i][3] = ct.getTrangThai();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableChiTiet.setModel(model);
        tableChiTiet.getTableHeader().setReorderingAllowed(false);
        adjustColumnWidths();
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

    private void adjustColumnWidths() {
        tableChiTiet.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tableChiTiet.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên món ăn
    }
}