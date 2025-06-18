package ui;

import dao.ChiTietOrderDAO;
import model.ChiTietOrder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        JLabel lblTitle = new JLabel("Chi tiết đơn hàng #" + orderId, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        tableChiTiet = new JTable();
        loadChiTietOrder(orderId);
        add(new JScrollPane(tableChiTiet), BorderLayout.CENTER);

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
    }
}
