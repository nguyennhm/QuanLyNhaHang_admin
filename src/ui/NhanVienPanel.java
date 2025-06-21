package ui;

import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import model.Nhanvien;
import model.TaiKhoan;
import utils.HashUtil;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NhanVienPanel extends JPanel {
    private JTable table;
    private NhanVienDAO nhanVienDAO;
    private TaiKhoanDAO taiKhoanDAO;

    public NhanVienPanel() {
        nhanVienDAO = new NhanVienDAO(JDBCUtil.getConnection());
        taiKhoanDAO = new TaiKhoanDAO(JDBCUtil.getConnection());

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true),
                "Danh sách nhân viên",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(100, 149, 237)
        ));
        setBackground(new Color(245, 248, 250));

        table = createStyledTable();
        reloadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnThem = createStyledButton("➕ Thêm nhân viên", new Color(46, 204, 113), new Color(39, 174, 96));
        btnThem.addActionListener(e -> showAddForm());

        JButton btnQuenMatKhau = createStyledButton("🔑 Quên mật khẩu", new Color(52, 152, 219), new Color(41, 128, 185));
        btnQuenMatKhau.addActionListener(e -> new ForgotPasswordUI());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(new Color(245, 248, 250));
        btnPanel.add(btnThem);
        btnPanel.add(btnQuenMatKhau);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void reloadTable() {
        List<Nhanvien> list = nhanVienDAO.getAllNhanVien();
        String[] cols = {"ID", "Tên", "SĐT", "Địa chỉ", "Vị trí", "Trạng thái tài khoản"};
        Object[][] data = new Object[list.size()][cols.length];

        for (int i = 0; i < list.size(); i++) {
            Nhanvien nv = list.get(i);
            TaiKhoan tk = taiKhoanDAO.getTaiKhoanById(nv.getId_taikhoan());
            data[i][0] = nv.getId_nhanvien();
            data[i][1] = nv.getTen();
            data[i][2] = nv.getSdt();
            data[i][3] = nv.getDiachi();
            data[i][4] = nv.getVitri();
            data[i][5] = tk != null ? tk.getTrangThai() : "Không có tài khoản";
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

    public void showAddForm() {
        JFrame f = new JFrame("Thêm nhân viên");
        f.setSize(500, 500);
        f.setLocationRelativeTo(this);
        f.getContentPane().setLayout(new BorderLayout(10, 10));
        f.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField tfTen = new JTextField(20);
        JTextField tfSdt = new JTextField(20);
        JTextField tfDiaChi = new JTextField(20);
        JTextField tfViTri = new JTextField(20);
        JTextField tfNgaySinh = new JTextField(20);
        JTextField tfEmail = new JTextField(20);
        JPasswordField tfMatKhau = new JPasswordField(20);
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"nhanvien", "admin"});

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createStyledLabel("Tên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createStyledLabel("SĐT:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfSdt, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createStyledLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfDiaChi, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createStyledLabel("Vị trí:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfViTri, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createStyledLabel("Ngày sinh (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfNgaySinh, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(createStyledLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfMatKhau, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(createStyledLabel("Vai trò:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbVaiTro, gbc);

        JButton btnLuu = createStyledButton("💾 Lưu", new Color(46, 204, 113), new Color(39, 174, 96));
        btnLuu.addActionListener(e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                Nhanvien nv = new Nhanvien();
                nv.setTen(tfTen.getText());
                nv.setSdt(tfSdt.getText());
                nv.setDiachi(tfDiaChi.getText());
                nv.setVitri(tfViTri.getText());
                Date ngaysinh = sdf.parse(tfNgaySinh.getText());
                nv.setNgaysinh(ngaysinh);

                TaiKhoan tk = new TaiKhoan(
                        tfEmail.getText(),
                        HashUtil.sha256(new String(tfMatKhau.getPassword())),
                        (String) cbVaiTro.getSelectedItem(),
                        "Hoạt động"
                );

                boolean taiKhoanOk = taiKhoanDAO.themTaiKhoan(tk);
                if (taiKhoanOk) {
                    nv.setId_taikhoan(tk.getId_taikhoan());
                    boolean nhanVienOk = nhanVienDAO.themNhanVien(nv);
                    if (nhanVienOk) {
                        reloadTable();
                        f.dispose();
                        JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        taiKhoanDAO.xoaTaiKhoan(tk.getId_taikhoan());
                        JOptionPane.showMessageDialog(f, "Thêm nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(f, "Thêm tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Lỗi dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottom.setBackground(new Color(240, 242, 245));
        bottom.add(btnLuu);

        f.add(formPanel, BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    public void showDetailForm(int nhanVienId) {
        Nhanvien nv = nhanVienDAO.getNhanVienById(nhanVienId);
        TaiKhoan tk = taiKhoanDAO.getTaiKhoanById(nv.getId_taikhoan());

        JFrame f = new JFrame("Chi tiết nhân viên");
        f.setSize(500, 400);
        f.setLocationRelativeTo(this);
        f.getContentPane().setLayout(new BorderLayout(15, 15));
        f.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField tfTen = new JTextField(nv.getTen());
        JTextField tfSdt = new JTextField(nv.getSdt());
        JTextField tfDiaChi = new JTextField(nv.getDiachi());
        JTextField tfNgaySinh = new JTextField(nv.getNgaysinh() != null ? new SimpleDateFormat("dd/MM/yyyy").format(nv.getNgaysinh()) : "");
        JTextField tfViTri = new JTextField(nv.getVitri());
        JTextField tfEmail = new JTextField(tk != null ? tk.getEmail() : "");
        JPasswordField tfMatKhau = new JPasswordField();
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"nhanvien", "admin"});
        if (tk != null) cbVaiTro.setSelectedItem(tk.getVaiTro());

        formPanel.add(createStyledLabel("Tên nhân viên:"));
        formPanel.add(tfTen);
        formPanel.add(createStyledLabel("Số điện thoại:"));
        formPanel.add(tfSdt);
        formPanel.add(createStyledLabel("Địa chỉ:"));
        formPanel.add(tfDiaChi);
        formPanel.add(createStyledLabel("Ngày sinh:"));
        formPanel.add(tfNgaySinh);
        formPanel.add(createStyledLabel("Vị trí:"));
        formPanel.add(tfViTri);
        formPanel.add(createStyledLabel("Email:"));
        formPanel.add(tfEmail);
        formPanel.add(createStyledLabel("Mật khẩu:"));
        formPanel.add(tfMatKhau);
        formPanel.add(createStyledLabel("Vai trò:"));
        formPanel.add(cbVaiTro);

        JButton btnLuu = createStyledButton("🔄 Cập nhật", new Color(52, 152, 219), new Color(41, 128, 185));
        btnLuu.addActionListener(e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                nv.setTen(tfTen.getText());
                nv.setSdt(tfSdt.getText());
                nv.setDiachi(tfDiaChi.getText());
                nv.setNgaysinh(sdf.parse(tfNgaySinh.getText()));
                nv.setVitri(tfViTri.getText());

                if (tk != null) {
                    tk.setEmail(tfEmail.getText());
                    String password = new String(tfMatKhau.getPassword()).trim();
                    if (!password.isEmpty()) {
                        tk.setMatKhau(HashUtil.sha256(password));
                    }
                    tk.setVaiTro((String) cbVaiTro.getSelectedItem());
                    boolean taiKhoanOk = taiKhoanDAO.capNhatTaiKhoan(tk);
                    boolean nhanVienOk = nhanVienDAO.capNhatNhanVien(nv);
                    if (taiKhoanOk && nhanVienOk) {
                        reloadTable();
                        f.dispose();
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(f, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Lỗi dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottom.setBackground(new Color(240, 242, 245));
        bottom.add(btnLuu);

        f.add(formPanel, BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    private JTable createStyledTable() {
        JTable t = new JTable();
        JTableHeader header = t.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setRowHeight(30);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setShowGrid(true);
        t.setGridColor(new Color(200, 200, 200));
        t.setSelectionBackground(new Color(173, 216, 230));
        t.setSelectionForeground(Color.BLACK);
        return t;
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(33, 37, 41));
        return label;
    }

    public JTable getTable() {
        return table;
    }

    private void adjustColumnWidths() {
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(130);
    }
}
