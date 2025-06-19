package ui;

import dao.ChiTietHoaDonDAO;
import model.ChiTietHoaDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChiTietHoaDonFrame extends JFrame {
    private JTable table;
    private int hoaDonId;

    public ChiTietHoaDonFrame(int hoaDonId) {
        this.hoaDonId = hoaDonId;
        setTitle("Chi tiết hóa đơn #" + hoaDonId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // ===== Title Panel with Gradient Background =====
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(70, 130, 180), 0, getHeight(), new Color(100, 149, 237));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(255, 255, 255));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel titleLabel = new JLabel("Chi tiết hóa đơn #" + hoaDonId);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // ===== Table Panel with Styled Border =====
        table = new JTable();
        loadData();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true)
        ));
        table.setRowHeight(30); // Slightly larger for better readability
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(Color.DARK_GRAY);
        table.getTableHeader().setReorderingAllowed(false);

        // Auto resize columns to fit content
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 200, 200));

        add(scrollPane, BorderLayout.CENTER);

        // ===== Button Panel with Styled Button =====
        JButton btnPrint = new JButton("🖨 In hóa đơn");
        btnPrint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnPrint.setBackground(new Color(0, 120, 215));
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setFocusPainted(false);
        btnPrint.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 100, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        btnPrint.addActionListener(e -> printInvoice());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(btnPrint);
        add(bottomPanel, BorderLayout.SOUTH);

        // Adjust frame size dynamically
        adjustFrameSize();
    }

    private void loadData() {
        ChiTietHoaDonDAO dao = new ChiTietHoaDonDAO();
        List<ChiTietHoaDon> list = dao.getByHoaDonId(hoaDonId);

        String[] columnNames = {"Tên món ăn", "Số lượng", "Thành tiền (VNĐ)"};
        Object[][] data = new Object[list.size()][columnNames.length];

        for (int i = 0; i < list.size(); i++) {
            ChiTietHoaDon cthd = list.get(i);
            data[i][0] = cthd.getTenMonAn();
            data[i][1] = cthd.getSoLuong();
            data[i][2] = String.format("%,d", cthd.getTongTien());
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        // Ensure table updates its preferred size after setting the model
        table.revalidate();
        table.repaint();

        // Handle empty data case
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu cho hóa đơn #" + hoaDonId, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void printInvoice() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = sdf.format(new Date()); // Current date and time: 19/06/2025 18:34
        MessageFormat header = new MessageFormat("HÓA ĐƠN #" + hoaDonId + " - Ngày in: " + currentDateTime);
        MessageFormat footer = new MessageFormat("Trang {0} - Cảm ơn quý khách!");

        try {
            boolean printed = table.print(JTable.PrintMode.FIT_WIDTH, header, footer, true, null, true);
            if (!printed) {
                JOptionPane.showMessageDialog(this, "In bị hủy bởi người dùng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "In hóa đơn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi in: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adjustFrameSize() {
        int rowHeight = table.getRowHeight();
        int rowCount = table.getRowCount();
        int headerHeight = table.getTableHeader().getPreferredSize().height;
        int tableHeight = rowHeight * rowCount + headerHeight + 50; // Adjusted extra space

        int tableWidth = 0;
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            tableWidth += columnModel.getColumn(i).getWidth();
        }
        tableWidth += 30; // Minimal padding for borders and scroll bar

        int minWidth = 400; // Increased minimum width for better visibility
        int minHeight = 250; // Increased minimum height
        int maxWidth = 700; // Increased max width for larger datasets
        int maxHeight = 600; // Increased max height

        int finalWidth = Math.max(minWidth, Math.min(tableWidth, maxWidth));
        int finalHeight = Math.max(minHeight, Math.min(tableHeight, maxHeight));

        setSize(finalWidth, finalHeight);
        setLocationRelativeTo(null); // Center the frame
    }
}