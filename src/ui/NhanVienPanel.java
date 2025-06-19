package ui;

import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import model.Nhanvien;
import model.TaiKhoan;
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

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)), "Danh sách nhân viên", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), Color.DARK_GRAY));

        table = createStyledTable();
        reloadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnThem = createStyledButton("➕ Thêm nhân viên");
        btnThem.addActionListener(e -> showAddForm());

        JButton btnQuenMatKhau = createStyledButton("Quên mật khẩu");
        btnQuenMatKhau.addActionListener(e -> new ForgotPasswordUI());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(245, 245, 245));
        btnPanel.add(btnThem);
        btnPanel.add(btnQuenMatKhau);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void reloadTable() {
        List<Nhanvien> list = nhanVienDAO.getAllNhanVien();
        String[] cols = {"ID", "Tên", "SĐT", "Vị trí", "Trạng thái tài khoản"};
        Object[][] data = new Object[list.size()][cols.length];

        for (int i = 0; i < list.size(); i++) {
            Nhanvien nv = list.get(i);
            TaiKhoan tk = taiKhoanDAO.getTaiKhoanById(nv.getId_taikhoan());
            data[i][0] = nv.getId_nhanvien();
            data[i][1] = nv.getTen();
            data[i][2] = nv.getSdt();
            data[i][3] = nv.getVitri();
            data[i][4] = tk != null ? tk.getTrangThai() : "Không có tài khoản";
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
        f.setSize(500, 400);
        f.getContentPane().setBackground(new Color(240, 240, 240));
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        JTextField tfTen = new JTextField();
        JTextField tfSdt = new JTextField();
        JTextField tfDiaChi = new JTextField();
        JTextField tfNgaySinh = new JTextField("dd/MM/yyyy");
        JTextField tfViTri = new JTextField();
        JTextField tfEmail = new JTextField();
        JPasswordField tfMatKhau = new JPasswordField();
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"Nhân viên", "Quản lý"});

        formPanel.add(createLabel("Tên nhân viên:"));
        formPanel.add(tfTen);
        formPanel.add(createLabel("Số điện thoại:"));
        formPanel.add(tfSdt);
        formPanel.add(createLabel("Địa chỉ:"));
        formPanel.add(tfDiaChi);
        formPanel.add(createLabel("Ngày sinh:"));
        formPanel.add(tfNgaySinh);
        formPanel.add(createLabel("Vị trí:"));
        formPanel.add(tfViTri);
        formPanel.add(createLabel("Email:"));
        formPanel.add(tfEmail);
        formPanel.add(createLabel("Mật khẩu:"));
        formPanel.add(tfMatKhau);
        formPanel.add(createLabel("Vai trò:"));
        formPanel.add(cbVaiTro);

        JButton btnLuu = createStyledButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Nhanvien nv = new Nhanvien();
                nv.setTen(tfTen.getText());
                nv.setSdt(tfSdt.getText());
                nv.setDiachi(tfDiaChi.getText());
                nv.setNgaysinh(sdf.parse(tfNgaySinh.getText()));
                nv.setVitri(tfViTri.getText());

                TaiKhoan tk = new TaiKhoan(
                        tfEmail.getText(),
                        new String(tfMatKhau.getPassword()),
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

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 245, 245));
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
        f.getContentPane().setBackground(new Color(240, 240, 240));
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        JTextField tfTen = new JTextField(nv.getTen());
        JTextField tfSdt = new JTextField(nv.getSdt());
        JTextField tfDiaChi = new JTextField(nv.getDiachi());
        JTextField tfNgaySinh = new JTextField(
                nv.getNgaysinh() != null ? new SimpleDateFormat("dd/MM/yyyy").format(nv.getNgaysinh()) : ""
        );
        JTextField tfViTri = new JTextField(nv.getVitri());
        JTextField tfEmail = new JTextField(tk != null ? tk.getEmail() : "");
        JPasswordField tfMatKhau = new JPasswordField(tk != null ? tk.getMatKhau() : "");
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"Nhân viên", "Quản lý"});
        if (tk != null) cbVaiTro.setSelectedItem(tk.getVaiTro());

        formPanel.add(createLabel("Tên nhân viên:"));
        formPanel.add(tfTen);
        formPanel.add(createLabel("Số điện thoại:"));
        formPanel.add(tfSdt);
        formPanel.add(createLabel("Địa chỉ:"));
        formPanel.add(tfDiaChi);
        formPanel.add(createLabel("Ngày sinh:"));
        formPanel.add(tfNgaySinh);
        formPanel.add(createLabel("Vị trí:"));
        formPanel.add(tfViTri);
        formPanel.add(createLabel("Email:"));
        formPanel.add(tfEmail);
        formPanel.add(createLabel("Mật khẩu:"));
        formPanel.add(tfMatKhau);
        formPanel.add(createLabel("Vai trò:"));
        formPanel.add(cbVaiTro);

        JButton btnLuu = createStyledButton("Cập nhật");
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
                    tk.setMatKhau(new String(tfMatKhau.getPassword()));
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

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(245, 245, 245));
        bottom.add(btnLuu);

        f.add(formPanel, BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
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

    private void adjustColumnWidths() {
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Tên
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // SĐT
    }
}