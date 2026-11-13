package com.mycompany.cobamvc;

import com.mycompany.cobamvc.dao.MobilDAO;
import com.mycompany.cobamvc.dao.MobilDaoImpl;
import com.mycompany.cobamvc.model.Mobil;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormRentalMobil extends javax.swing.JFrame {

    // 3. Class Interface (Penerapan Dependency Injection)
    private MobilDAO mobilDAO;
    private DefaultTableModel tableModelMaster;
    private DefaultTableModel tableModelTransaksi;
    private int selectedMasterId = -1;

    // List untuk menyimpan referensi mobil yang sedang tersedia dan disewa
    private List<Mobil> listMobilTersedia = new ArrayList<>();
    private List<Mobil> listMobilDisewa = new ArrayList<>();

    private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    // 1. Constructor Injection
    public FormRentalMobil(MobilDAO mobilDAO) {
        this.mobilDAO = mobilDAO;
        initComponentsSystem();
        setTitle("Sistem Rental Mobil - Ali RPL (Spring DAO & Hibernate)");
        setLocationRelativeTo(null);
        refreshAllData();
    }

    public FormRentalMobil() {
        this(new MobilDaoImpl());
    }

    // 2. Getter & Setter Injection
    public MobilDAO getMobilDAO() {
        return mobilDAO;
    }

    public void setMobilDAO(MobilDAO mobilDAO) {
        this.mobilDAO = mobilDAO;
    }

    private void initComponentsSystem() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(880, 650);
        setLayout(null);

        // Header Atas
        JLabel lblHeader = new JLabel("SISTEM APLIKASI RENTAL MOBIL");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setBounds(30, 15, 450, 30);
        add(lblHeader);

        JLabel lblSub = new JLabel("Ali RPL - Implementasi Spring DAO & Hibernate ORM");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setBounds(30, 45, 450, 20);
        add(lblSub);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(760, 20, 85, 32);
        btnLogout.addActionListener(e -> {
            new NewJFrame().setVisible(true);
            this.dispose();
        });
        add(btnLogout);

        // JTabbedPane (2 Tab Utama)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(30, 80, 815, 510);

        // ==========================================
        // TAB 1: MASTER DATA MOBIL
        // ==========================================
        JPanel panelMaster = new JPanel(null);

        JLabel lblKode = new JLabel("Kode Mobil:");
        txtKode = new JTextField();
        JLabel lblNama = new JLabel("Nama Mobil:");
        txtNama = new JTextField();
        JLabel lblMerk = new JLabel("Merk:");
        txtMerk = new JTextField();
        JLabel lblTarif = new JLabel("Tarif / Hari (Rp):");
        txtTarif = new JTextField();
        JLabel lblStatus = new JLabel("Status:");
        cbStatusMaster = new JComboBox<>(new String[]{"Tersedia", "Disewa", "Perawatan"});

        JButton btnSimpan = new JButton("Simpan");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnReset = new JButton("Reset");

        tableModelMaster = new DefaultTableModel(new String[]{"ID", "Kode", "Nama Mobil", "Merk", "Tarif (Rp)", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblMaster = new JTable(tableModelMaster);
        JScrollPane scrollMaster = new JScrollPane(tblMaster);

        // Posisi Tab 1
        lblKode.setBounds(20, 20, 120, 25);
        txtKode.setBounds(140, 20, 200, 25);
        lblNama.setBounds(20, 55, 120, 25);
        txtNama.setBounds(140, 55, 200, 25);
        lblMerk.setBounds(20, 90, 120, 25);
        txtMerk.setBounds(140, 90, 200, 25);
        lblTarif.setBounds(20, 125, 120, 25);
        txtTarif.setBounds(140, 125, 200, 25);
        lblStatus.setBounds(20, 160, 120, 25);
        cbStatusMaster.setBounds(140, 160, 200, 25);

        btnSimpan.setBounds(20, 205, 80, 30);
        btnUbah.setBounds(105, 205, 80, 30);
        btnHapus.setBounds(190, 205, 80, 30);
        btnReset.setBounds(275, 205, 80, 30);

        scrollMaster.setBounds(20, 250, 770, 210);

        panelMaster.add(lblKode); panelMaster.add(txtKode);
        panelMaster.add(lblNama); panelMaster.add(txtNama);
        panelMaster.add(lblMerk); panelMaster.add(txtMerk);
        panelMaster.add(lblTarif); panelMaster.add(txtTarif);
        panelMaster.add(lblStatus); panelMaster.add(cbStatusMaster);
        panelMaster.add(btnSimpan); panelMaster.add(btnUbah);
        panelMaster.add(btnHapus); panelMaster.add(btnReset);
        panelMaster.add(scrollMaster);

        // Event Tab 1
        tblMaster.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblMaster.getSelectedRow();
                if (row >= 0) {
                    selectedMasterId = Integer.parseInt(tableModelMaster.getValueAt(row, 0).toString());
                    txtKode.setText(tableModelMaster.getValueAt(row, 1).toString());
                    txtNama.setText(tableModelMaster.getValueAt(row, 2).toString());
                    txtMerk.setText(tableModelMaster.getValueAt(row, 3).toString());
                    txtTarif.setText(tableModelMaster.getValueAt(row, 4).toString());
                    cbStatusMaster.setSelectedItem(tableModelMaster.getValueAt(row, 5).toString());
                }
            }
        });

        btnSimpan.addActionListener(e -> simpanDataMaster());
        btnUbah.addActionListener(e -> ubahDataMaster());
        btnHapus.addActionListener(e -> hapusDataMaster());
        btnReset.addActionListener(e -> resetFormMaster(tblMaster));

        // ==========================================
        // TAB 2: TRANSAKSI SEWA & PENGEMBALIAN MOBIL
        // ==========================================
        JPanel panelTransaksi = new JPanel(null);

        // --- BAGIAN KIRI: FORM SEWA ---
        JLabel lblSubSewa = new JLabel("Form Penyewaan Mobil Baru");
        lblSubSewa.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSubSewa.setBounds(20, 15, 280, 25);
        panelTransaksi.add(lblSubSewa);

        JLabel lblPilihMobil = new JLabel("Pilih Mobil:");
        cbMobilTersedia = new JComboBox<>();
        JLabel lblPenyewa = new JLabel("Nama Pelanggan:");
        txtPenyewa = new JTextField();
        JLabel lblTelp = new JLabel("No. HP / KTP:");
        txtTelp = new JTextField();
        JLabel lblHari = new JLabel("Lama Sewa (Hari):");
        txtHari = new JTextField("1");
        JLabel lblTotalBiaya = new JLabel("Total: Rp 0");
        lblTotalBiaya.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalBiaya.setForeground(new java.awt.Color(16, 120, 50));

        JButton btnHitung = new JButton("Hitung Total");
        JButton btnProsesSewa = new JButton("Proses Sewa Mobil");

        lblPilihMobil.setBounds(20, 45, 120, 25);
        cbMobilTersedia.setBounds(140, 45, 230, 25);
        lblPenyewa.setBounds(20, 80, 120, 25);
        txtPenyewa.setBounds(140, 80, 230, 25);
        lblTelp.setBounds(20, 115, 120, 25);
        txtTelp.setBounds(140, 115, 230, 25);
        lblHari.setBounds(20, 150, 120, 25);
        txtHari.setBounds(140, 150, 80, 25);
        btnHitung.setBounds(230, 150, 140, 25);
        lblTotalBiaya.setBounds(140, 185, 230, 25);
        btnProsesSewa.setBounds(140, 220, 180, 32);

        panelTransaksi.add(lblPilihMobil); panelTransaksi.add(cbMobilTersedia);
        panelTransaksi.add(lblPenyewa); panelTransaksi.add(txtPenyewa);
        panelTransaksi.add(lblTelp); panelTransaksi.add(txtTelp);
        panelTransaksi.add(lblHari); panelTransaksi.add(txtHari);
        panelTransaksi.add(btnHitung); panelTransaksi.add(lblTotalBiaya);
        panelTransaksi.add(btnProsesSewa);

        // --- BAGIAN KANAN: FORM PENGEMBALIAN ---
        JLabel lblSubKembali = new JLabel("Form Pengembalian Mobil");
        lblSubKembali.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSubKembali.setBounds(430, 15, 280, 25);
        panelTransaksi.add(lblSubKembali);

        JLabel lblMobilKembali = new JLabel("Mobil Disewa:");
        cbMobilDisewa = new JComboBox<>();
        JButton btnProsesKembali = new JButton("Kembalikan Mobil");

        lblMobilKembali.setBounds(430, 45, 120, 25);
        cbMobilDisewa.setBounds(540, 45, 240, 25);
        btnProsesKembali.setBounds(540, 85, 180, 32);

        panelTransaksi.add(lblMobilKembali); panelTransaksi.add(cbMobilDisewa);
        panelTransaksi.add(btnProsesKembali);

        // Tabel Status Real-time Mobil
        JLabel lblTabelStatus = new JLabel("Daftar Status Armada Mobil Rental (Real-time):");
        lblTabelStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTabelStatus.setBounds(20, 265, 350, 20);
        panelTransaksi.add(lblTabelStatus);

        tableModelTransaksi = new DefaultTableModel(new String[]{"ID", "Kode", "Nama Mobil", "Merk", "Tarif/Hari (Rp)", "Status Rental"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblTransaksi = new JTable(tableModelTransaksi);
        JScrollPane scrollTransaksi = new JScrollPane(tblTransaksi);
        scrollTransaksi.setBounds(20, 290, 770, 170);
        panelTransaksi.add(scrollTransaksi);

        // Event Transaksi
        btnHitung.addActionListener(e -> hitungBiaya(lblTotalBiaya));
        btnProsesSewa.addActionListener(e -> prosesSewaMobil(lblTotalBiaya));
        btnProsesKembali.addActionListener(e -> prosesKembaliMobil());

        // Masukkan Tab ke TabbedPane
        tabbedPane.addTab("🚗 Data Mobil (Master)", panelMaster);
        tabbedPane.addTab("📋 Transaksi Sewa & Pengembalian", panelTransaksi);

        add(tabbedPane);
    }

    // ==========================================
    // LOGIKA MASTER DATA (TAB 1)
    // ==========================================
    private void simpanDataMaster() {
        String kode = txtKode.getText().trim();
        String nama = txtNama.getText().trim();
        String merk = txtMerk.getText().trim();
        String tarifStr = txtTarif.getText().trim();
        String status = cbStatusMaster.getSelectedItem().toString();

        if (kode.isEmpty() || nama.isEmpty() || tarifStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode, Nama, dan Tarif wajib diisi!");
            return;
        }

        try {
            double tarif = Double.parseDouble(tarifStr);
            Mobil mobil = new Mobil(kode, nama, merk, tarif, status);
            if (mobilDAO.insert(mobil)) {
                JOptionPane.showMessageDialog(this, "Mobil berhasil disimpan via Spring DAO & Hibernate!");
                refreshAllData();
                resetFormMaster(null);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tarif harus berupa angka!");
        }
    }

    private void ubahDataMaster() {
        if (selectedMasterId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mobil dari tabel terlebih dahulu!");
            return;
        }

        try {
            double tarif = Double.parseDouble(txtTarif.getText().trim());
            Mobil mobil = new Mobil(txtKode.getText().trim(), txtNama.getText().trim(), txtMerk.getText().trim(), tarif, cbStatusMaster.getSelectedItem().toString());
            mobil.setId(selectedMasterId);

            if (mobilDAO.update(mobil)) {
                JOptionPane.showMessageDialog(this, "Data Mobil berhasil diubah!");
                refreshAllData();
                resetFormMaster(null);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tarif harus berupa angka!");
        }
    }

    private void hapusDataMaster() {
        if (selectedMasterId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mobil dari tabel yang akan dihapus!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Hapus mobil ini dari database?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (mobilDAO.delete(selectedMasterId)) {
                JOptionPane.showMessageDialog(this, "Mobil berhasil dihapus!");
                refreshAllData();
                resetFormMaster(null);
            }
        }
    }

    private void resetFormMaster(JTable table) {
        selectedMasterId = -1;
        txtKode.setText("");
        txtNama.setText("");
        txtMerk.setText("");
        txtTarif.setText("");
        cbStatusMaster.setSelectedIndex(0);
        if (table != null) table.clearSelection();
    }

    // ==========================================
    // LOGIKA TRANSAKSI SEWA & KEMBALI (TAB 2)
    // ==========================================
    private double hitungBiaya(JLabel lblTotal) {
        int index = cbMobilTersedia.getSelectedIndex();
        if (index < 0 || index >= listMobilTersedia.size()) {
            JOptionPane.showMessageDialog(this, "Pilih mobil yang tersedia terlebih dahulu!");
            return 0;
        }

        try {
            int hari = Integer.parseInt(txtHari.getText().trim());
            if (hari <= 0) {
                JOptionPane.showMessageDialog(this, "Lama sewa minimal 1 hari!");
                return 0;
            }
            Mobil m = listMobilTersedia.get(index);
            double total = m.getTarif() * hari;
            lblTotal.setText("Total: " + rupiahFormat.format(total));
            return total;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lama sewa harus berupa angka hari!");
            return 0;
        }
    }

    private void prosesSewaMobil(JLabel lblTotal) {
        int index = cbMobilTersedia.getSelectedIndex();
        if (index < 0 || index >= listMobilTersedia.size()) {
            JOptionPane.showMessageDialog(this, "Tidak ada mobil tersedia yang dipilih!");
            return;
        }

        String penyewa = txtPenyewa.getText().trim();
        String telp = txtTelp.getText().trim();
        if (penyewa.isEmpty() || telp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Penyewa dan No HP/KTP wajib diisi!");
            return;
        }

        double total = hitungBiaya(lblTotal);
        if (total <= 0) return;

        Mobil mobil = listMobilTersedia.get(index);
        mobil.setStatus("Disewa"); // Update status mobil menjadi Disewa

        if (mobilDAO.update(mobil)) {
            JOptionPane.showMessageDialog(this, 
                "PENYEWAAN BERHASIL!\n" +
                "-----------------------------------------\n" +
                "Nama Penyewa : " + penyewa + "\n" +
                "Mobil Disewa : " + mobil.getNamaMobil() + " (" + mobil.getKodeMobil() + ")\n" +
                "Lama Sewa    : " + txtHari.getText().trim() + " Hari\n" +
                "Total Bayar  : " + rupiahFormat.format(total) + "\n" +
                "Status Mobil : TELAH BERUBAH MENJADI DISEWA\n" +
                "-----------------------------------------",
                "Struk Sewa Rental Mobil", JOptionPane.INFORMATION_MESSAGE);

            txtPenyewa.setText("");
            txtTelp.setText("");
            txtHari.setText("1");
            lblTotal.setText("Total: Rp 0");
            refreshAllData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memproses penyewaan mobil!");
        }
    }

    private void prosesKembaliMobil() {
        int index = cbMobilDisewa.getSelectedIndex();
        if (index < 0 || index >= listMobilDisewa.size()) {
            JOptionPane.showMessageDialog(this, "Tidak ada mobil yang sedang disewa!");
            return;
        }

        Mobil mobil = listMobilDisewa.get(index);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Konfirmasi pengembalian mobil " + mobil.getNamaMobil() + " (" + mobil.getKodeMobil() + ")?",
            "Pengembalian Mobil", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            mobil.setStatus("Tersedia"); // Update status mobil kembali menjadi Tersedia
            if (mobilDAO.update(mobil)) {
                JOptionPane.showMessageDialog(this, 
                    "PENGEMBALIAN BERHASIL!\n" +
                    "Mobil " + mobil.getNamaMobil() + " telah kembali dan statusnya aktif menjadi TERSEDIA.",
                    "Selesai", JOptionPane.INFORMATION_MESSAGE);
                refreshAllData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memproses pengembalian mobil!");
            }
        }
    }

    // Refresh semua data tabel dan combo box secara real-time
    private void refreshAllData() {
        tableModelMaster.setRowCount(0);
        tableModelTransaksi.setRowCount(0);
        cbMobilTersedia.removeAllItems();
        cbMobilDisewa.removeAllItems();
        listMobilTersedia.clear();
        listMobilDisewa.clear();

        List<Mobil> list = mobilDAO.getAll();
        if (list != null) {
            for (Mobil m : list) {
                // Isi tabel master
                tableModelMaster.addRow(new Object[]{
                    m.getId(), m.getKodeMobil(), m.getNamaMobil(), m.getMerk(), m.getTarif(), m.getStatus()
                });
                // Isi tabel transaksi
                tableModelTransaksi.addRow(new Object[]{
                    m.getId(), m.getKodeMobil(), m.getNamaMobil(), m.getMerk(), m.getTarif(), m.getStatus()
                });

                // Kelompokkan ke ComboBox
                if ("Tersedia".equalsIgnoreCase(m.getStatus())) {
                    listMobilTersedia.add(m);
                    cbMobilTersedia.addItem(m.getKodeMobil() + " - " + m.getNamaMobil() + " (" + rupiahFormat.format(m.getTarif()) + "/hari)");
                } else if ("Disewa".equalsIgnoreCase(m.getStatus())) {
                    listMobilDisewa.add(m);
                    cbMobilDisewa.addItem(m.getKodeMobil() + " - " + m.getNamaMobil() + " [Sedang Disewa]");
                }
            }
        }
    }

    // Komponen Input Tab 1
    private JTextField txtKode, txtNama, txtMerk, txtTarif;
    private JComboBox<String> cbStatusMaster;

    // Komponen Input Tab 2
    private JComboBox<String> cbMobilTersedia, cbMobilDisewa;
    private JTextField txtPenyewa, txtTelp, txtHari;
}
