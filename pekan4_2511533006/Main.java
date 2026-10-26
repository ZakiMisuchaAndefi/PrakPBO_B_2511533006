package pekan4_2511533006;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        ArrayList<Rekening> daftarRekening = new ArrayList<>();
        Rekening akunAktif = null;
        boolean isRunning = true;

        System.out.println("=== SISTEM PERBANKAN MINI ===");

        while (isRunning) {
            System.out.println("\nMenu Utama:");
            System.out.println("1. Buka Rekening Baru");
            System.out.println("2. Setor Tunai");
            System.out.println("3. Tarik Tunai");
            System.out.println("4. Cek Informasi Rekening");
            System.out.println("5. Ganti Akun");
            System.out.println("6. Cetak Mutasi (Riwayat)");
            System.out.println("7. Simulasi Akhir Bulan (Khusus Tabungan)");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            int pilihan = input.nextInt();
            input.nextLine();

            switch (pilihan) {
                case 1:
                    System.out.print("Masukkan No Rekening: ");
                    String no = input.nextLine();
                    System.out.print("Masukkan Nama Pemilik: ");
                    String nama = input.nextLine();
                    System.out.print("Masukkan Saldo Awal: ");
                    double saldo = input.nextDouble();
                    input.nextLine();
                    System.out.print("Buat PIN (6 digit): ");
                    String pinBaru = input.nextLine();

                    System.out.println("Pilih Produk:");
                    System.out.println("1. Tabungan Umum");
                    System.out.println("2. Giro Bisnis");
                    System.out.print("Pilihan produk: ");
                    int pilihanProduk = input.nextInt();
                    input.nextLine();

                    Rekening rekeningBaru;
                    if (pilihanProduk == 1) {
                        System.out.print("Masukkan suku bunga (%): ");
                        double sukuBunga = input.nextDouble();
                        input.nextLine();
                        rekeningBaru = new RekeningTabungan(no, nama, saldo, pinBaru, sukuBunga);
                    } else if (pilihanProduk == 2) {
                        System.out.print("Masukkan batas overdraft: ");
                        double batasOverdraft = input.nextDouble();
                        input.nextLine();
                        rekeningBaru = new RekeningGiro(nama, no, saldo, pinBaru, batasOverdraft);
                    } else {
                        System.out.println("Pilihan produk tidak valid!");
                        break;
                    }

                    daftarRekening.add(rekeningBaru);
                    akunAktif = rekeningBaru;
                    break;

                case 2:
                    if (akunAktif == null) {
                        System.out.println("Error: Mohon maaf, Anda belum memiliki nomor rekening!");
                    } else {
                        System.out.print("Masukkan nominal setor: ");
                        double setor = input.nextDouble();
                        akunAktif.setorTunai(setor);
                    }
                    break;

                case 3:
                    if (akunAktif == null) {
                        System.out.println("Error: Mohon maaf, Anda belum memiliki nomor rekening!");
                    } else {
                        System.out.print("Masukkan PIN: ");
                        String pinTarik = input.nextLine();
                        if (akunAktif.otentikasi(pinTarik)) {
                            System.out.print("Masukkan nominal tarik: ");
                            double tarik = input.nextDouble();
                            input.nextLine();
                            akunAktif.tarikTunai(tarik);
                        } else {
                            System.out.println("Akses Ditolak: PIN yang Anda masukkan salah!");
                        }
                    }
                    break;

                case 4:
                    if (akunAktif == null) {
                        System.out.println("Error: Anda belum membuka rekening!");
                    } else {
                        akunAktif.cekInformasi();
                    }
                    break;

                case 5:
                    if (daftarRekening.isEmpty()) {
                        System.out.println("Error: Belum ada rekening yang terdaftar!");
                    } else {
                        System.out.print("Masukkan No Rekening yang ingin diaktifkan: ");
                        String cariNo = input.nextLine();
                        Rekening ditemukan = null;

                        for (Rekening r : daftarRekening) {
                            if (r.getNomorRekening().equals(cariNo)) {
                                ditemukan = r;
                                break;
                            }
                        }

                        if (ditemukan != null) {
                            akunAktif = ditemukan;
                            System.out.println("Akun aktif berhasil diganti ke rekening atas nama " + akunAktif.getNamaPemilik());
                        } else {
                            System.out.println("Error: Nomor rekening tidak ditemukan!");
                        }
                    }
                    break;

                case 6:
                    if (akunAktif == null) {
                        System.out.println("Error: Anda belum membuka rekening!");
                    } else {
                        System.out.print("Masukkan PIN: ");
                        String pinMutasi = input.nextLine();
                        if (akunAktif.otentikasi(pinMutasi)) {
                            akunAktif.cetakMutasi();
                        } else {
                            System.out.println("Akses Ditolak: PIN yang Anda masukkan salah!");
                        }
                    }
                    break;

                case 7:
                    if (akunAktif instanceof RekeningTabungan) {
                        RekeningTabungan tabunganAktif = (RekeningTabungan) akunAktif;
                        tabunganAktif.tambahanBungaAkhirBulan();
                    } else {
                        System.out.println("Gagal: Fitur bunga akhir bulan hanya berlaku untuk Rekening Tabungan.");
                    }
                    break;

                case 0:
                    isRunning = false;
                    System.out.println("Sistem ditutup. Terima kasih!");
                    break;

                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
        input.close();
    }
}