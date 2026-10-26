package pekan5_2511533006;

import java.util.ArrayList;

public class Rekening {
    // 1. Mengunci atribut dengan 'private'
    private String nomorRekening;
    private String namaPemilik;
    protected double saldo;
    private String pin; // Data sensitif!
    protected ArrayList<Transaksi> riwayatTransaksi;
      
    // 2. Modifikasi Constructor untuk menerima PIN awal
    public Rekening(String nomor, String nama, double saldoAwal, String pinAwal) {
        this.nomorRekening = nomor;
        this.namaPemilik = nama;
        this.saldo = saldoAwal;

        // Validasi PIN di dalam Constructor
        if (pinAwal.length() == 6) {
            this.pin = pinAwal;
        } else {
            System.out.println("Peringatan: PIN harus 6 digit! Menggunakan PIN default 123456");
            this.pin = "123456";
        }

        this.riwayatTransaksi = new ArrayList<>();
        System.out.println("Rekening atas nama " + namaPemilik + " berhasil dibuat.");
    }

    // 3. Getter untuk atribut yang diizinkan dibaca publik
    public String getNomorRekening() { return nomorRekening; }
    public String getNamaPemilik() { return namaPemilik; }
    public double getSaldo() { return saldo; }

    // 4. Method Otentikasi Internal (Validasi Enkapsulasi)
    // Tidak ada setSaldo() agar saldo tidak bisa diubah langsung dari luar (harus lewat setorTunai/tarikTunai),
    // dan tidak ada getPin() agar PIN tidak pernah bisa dibaca dari luar class.
    public boolean otentikasi(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void setorTunai(double nominal) {
        if (nominal > 0) {
            saldo += nominal;

            String idTrx = "TRX-S-" + System.currentTimeMillis();
            Transaksi trxBaru = new Transaksi(idTrx, "Kredit", nominal);
            riwayatTransaksi.add(trxBaru);

            System.out.println("Setor tunai Rp" + nominal + " berhasil. Saldo saat ini: Rp" + saldo);
        } else {
            System.out.println("Gagal: Nominal setor harus lebih dari 0!");
        }
    }

    // Praktikum 5: return type diubah dari void menjadi boolean
    public boolean tarikTunai(double nominal) {
        if (nominal <= 0) {
            System.out.println("Gagal: Nominal tarik harus lebih dari 0!");
            return false;
        } else if (nominal > saldo) {
            System.out.println("Transaksi Gagal: Saldo tidak mencukupi. Saldo Anda: Rp" + saldo);
            return false;
        } else {
            saldo -= nominal;

            String idTrx = "TRX-T-" + System.currentTimeMillis();
            riwayatTransaksi.add(new Transaksi(idTrx, "Debit", nominal));

            System.out.println("Tarik tunai Rp" + nominal + " berhasil. Sisa saldo: Rp" + saldo);
            return true; // Transaksi sukses
        }
    }

    public void cetakMutasi() {
        if (riwayatTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi pada rekening ini");
        } else {
            System.out.println("--- MUTASI REKENING " + nomorRekening + " ---");
            for (Transaksi trx : riwayatTransaksi) {
                trx.cetakDetail();
            }
        }
    }

    public void cekInformasi() {
        System.out.println("--- INFO REKENING ---");
        System.out.println("No. Rekening : " + nomorRekening);
        System.out.println("Nama Pemilik : " + namaPemilik);
        System.out.println("Saldo Akhir  : Rp" + saldo);
        System.out.println("----------------------");
    }
    
    // Praktikum 5: Transfer antar rekening (parameter polimorfik)
    public void transfer(Rekening tujuan, double nominal, String pinInput) {
        // 1. Otentikasi
        if (!otentikasi(pinInput)) {
            System.out.println("Transfer Dibatalkan: PIN yang Anda masukkan salah!");
            return;
        }
        if (tujuan == this) {
            System.out.println("Transfer Dibatalkan: Rekening tujuan tidak boleh sama dengan rekening asal!");
            return;
        }
        // 2. Tarik dari rekening asal (dynamic dispatch: Giro memakai aturan overdraft)
        boolean berhasil = this.tarikTunai(nominal);

        // 3. Jika berhasil, setor ke rekening tujuan
        if (berhasil) {
            tujuan.setorTunai(nominal);
            System.out.println("Transfer Rp" + nominal + " dari " + this.namaPemilik
                    + " ke " + tujuan.getNamaPemilik() + " berhasil.");
        } else {
            System.out.println("Transfer Gagal: Penarikan dari rekening asal tidak berhasil.");
        }
    }
}