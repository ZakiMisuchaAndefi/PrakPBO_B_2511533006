package pekan1_2511533006;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Tugas 2: Tantangan Multi-Akun -> pakai ArrayList<Rekening>
        ArrayList<Rekening> daftarRekening = new ArrayList<>();
        Rekening akunAktif = null; // Objek belum diinisialisasi (null)
        boolean isRunning = true;

        System.out.println("=== SISTEM PERBANKAN MINI ===");

        while (isRunning) {
            System.out.println("\nMenu Utama:");
            System.out.println("1. Buka Rekening Baru");
            System.out.println("2. Setor Tunai");
            System.out.println("3. Tarik Tunai");
            System.out.println("4. Cek Informasi Rekening");
            System.out.println("5. Ganti Akun");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            int pilihan = input.nextInt();
            input.nextLine(); // Membersihkan buffer enter

            switch (pilihan) {
                case 1:
                    System.out.print("Masukkan No Rekening: ");
                    String no = input.nextLine();
                    System.out.print("Masukkan Nama Pemilik: ");
                    String nama = input.nextLine();
                    System.out.print("Masukkan Saldo Awal: ");
                    double saldo = input.nextDouble();
                    input.nextLine();

                    // Instansiasi Object / Menjalankan Constructor
                    Rekening rekeningBaru = new Rekening(no, nama, saldo);
                    daftarRekening.add(rekeningBaru); // ditambahkan ke ArrayList
                    akunAktif = rekeningBaru; // rekening baru langsung jadi akun aktif
                    break;

                case 2:
                    if (akunAktif == null) {
                        System.out.println("Error: Mohon maaf, Anda belum memiliki nomor rekening!");
                    } else {
                        System.out.print("Masukkan nominal setor: ");
                        double setor = input.nextDouble();
                        akunAktif.setorTunai(setor); // Memanggil Behavior / method
                    }
                    break;

                case 3:
                    if (akunAktif == null) {
                        System.out.println("Error: Mohon maaf, Anda belum memiliki nomor rekening!");
                    } else {
                        System.out.print("Masukkan nominal tarik: ");
                        double tarik = input.nextDouble();
                        akunAktif.tarikTunai(tarik);
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
                    // Bonus: Ganti Akun -> cari di ArrayList berdasarkan nomor rekening
                    if (daftarRekening.isEmpty()) {
                        System.out.println("Error: Belum ada rekening yang terdaftar!");
                    } else {
                        System.out.print("Masukkan No Rekening yang ingin diaktifkan: ");
                        String cariNo = input.nextLine();
                        Rekening ditemukan = null;

                        for (Rekening r : daftarRekening) {
                            if (r.nomorRekening.equals(cariNo)) {
                                ditemukan = r;
                                break;
                            }
                        }

                        if (ditemukan != null) {
                            akunAktif = ditemukan;
                            System.out.println("Akun aktif berhasil diganti ke rekening atas nama " + akunAktif.namaPemilik);
                        } else {
                            System.out.println("Error: Nomor rekening tidak ditemukan!");
                        }
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