package ui;

import dao.ThongKeDAO;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import utils.ExcelExporter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ThongKePanel extends JPanel {
    private ThongKeDAO dao;
    private JPanel mainPanel;

    public ThongKePanel() {
        dao = new ThongKeDAO();
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("📊 Thống Kê Hoạt Động");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(44, 62, 80));

        JButton exportBtn = new JButton("📥 Xuất Excel");
        exportBtn.setFocusPainted(false);
        exportBtn.setBackground(new Color(41, 128, 185));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        exportBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        exportBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("ThongKeNhaHang.xls"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".xls")) {
                    filePath += ".xls";
                }

                ExcelExporter.exportToExcel(filePath);

                JOptionPane.showMessageDialog(this, "✅ Đã xuất Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                // Cập nhật lại nội dung thống kê sau khi xuất
                remove(mainPanel);
                add(createMainPanel(), BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        header.add(title, BorderLayout.WEST);
        header.add(exportBtn, BorderLayout.EAST);
        return header;
    }

    private JPanel createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createSummaryPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        centerPanel.setBackground(Color.WHITE);

        JPanel chartsPanel = new JPanel();
        chartsPanel.setLayout(new BoxLayout(chartsPanel, BoxLayout.Y_AXIS));
        chartsPanel.setBackground(Color.WHITE);
        chartsPanel.add(createRevenueChartPanel());
        chartsPanel.add(Box.createVerticalStrut(15));
        chartsPanel.add(createExpenseBreakdownPanel());

        centerPanel.add(chartsPanel);
        centerPanel.add(createTopProductsPanel());

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createSummaryPanel() {
        JPanel summary = new JPanel(new GridLayout(2, 3, 15, 15));
        summary.setBackground(Color.WHITE);

        double thu = dao.getTongThu();
        double chi = dao.getTongChi();
        int soDon = dao.getSoLuongDonHang();
        int soLuongSP = dao.getTongSanPhamBanDuoc();
        double loiNhuan = thu - chi;
        double tiLeLoiNhuan = thu > 0 ? (loiNhuan / thu * 100) : 0;

        summary.add(createCard("💰 Tổng thu", formatVND(thu), new Color(39, 174, 96)));
        summary.add(createCard("📉 Tổng chi", formatVND(chi), new Color(192, 57, 43)));
        summary.add(createCard("📦 Đơn hàng", String.valueOf(soDon), new Color(41, 128, 185)));
        summary.add(createCard("🛒 SP đã bán", String.valueOf(soLuongSP), new Color(142, 68, 173)));
        summary.add(createCard("📈 Lợi nhuận", formatVND(loiNhuan), new Color(243, 156, 18)));
        summary.add(createCard("📊 Tỉ lệ LN", String.format("%.1f%%", tiLeLoiNhuan), new Color(52, 152, 219)));

        return summary;
    }

    private JPanel createRevenueChartPanel() {
        List<String[]> topProducts = dao.getTopProducts();
        double tongDoanhThuSP = dao.getTongDoanhThuSanPham();

        PieChart chart = new PieChartBuilder()
                .width(500)
                .height(300)
                .title("Tỷ lệ doanh thu theo món ăn")
                .build();

        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);

        double top5Total = 0;
        for (String[] product : topProducts) {
            String tenMon = product[0];
            double doanhThu = Double.parseDouble(product[2]);
            top5Total += doanhThu;
            chart.addSeries(tenMon, doanhThu);
        }

        double other = tongDoanhThuSP - top5Total;
        if (other > 0) {
            chart.addSeries("Khác", other);
        }

        JPanel panel = new XChartPanel<>(chart);
        panel.setBorder(BorderFactory.createTitledBorder("Tỷ lệ doanh thu"));
        return panel;
    }

    private JPanel createExpenseBreakdownPanel() {
        double tongLuong = dao.getTongLuong();
        double tongNguyenLieu = dao.getTongNguyenLieu();

        PieChart chart = new PieChartBuilder()
                .width(500)
                .height(300)
                .title("Phân tích chi phí")
                .build();

        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);

        chart.addSeries("Lương nhân viên", tongLuong);
        chart.addSeries("Nguyên liệu", tongNguyenLieu);

        JPanel panel = new XChartPanel<>(chart);
        panel.setBorder(BorderFactory.createTitledBorder("Chi phí"));
        return panel;
    }

    private JPanel createTopProductsPanel() {
        List<String[]> topProducts = dao.getTopProducts();
        StringBuilder html = new StringBuilder("<html><div style='margin:10px'><b>Top 5 Sản Phẩm Bán Chạy:</b><br><ol>");
        for (String[] product : topProducts) {
            html.append(String.format("<li>%s - %s (%,.0f VND)</li>", product[0], product[1], Double.parseDouble(product[2])));
        }
        html.append("</ol></div></html>");

        JLabel label = new JLabel(html.toString());
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Top sản phẩm"));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(color);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
        lblValue.setForeground(new Color(33, 33, 33));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private String formatVND(double amount) {
        return String.format("%,.0f", amount) + " VND";
    }
}
