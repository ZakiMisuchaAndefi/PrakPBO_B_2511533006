package pekan5_2511533006;

public class RekeningTabungan extends Rekening {
	
	
	private double sukuBunga;
	public RekeningTabungan(String nomor, String nama, double saldoAwal, String pinAwal, double sukuBunga) {
		
		super(nomor, nama, saldoAwal, pinAwal);
		this.sukuBunga = sukuBunga;
	}
	
	public void tambahanBungaAkhirBulan() {
		//Hitung bunga
		double nominalBunga = saldo * (sukuBunga / 100);
		saldo += nominalBunga;
		
		
		String idTrx = "TRX-B-" + System.currentTimeMillis();
		riwayatTransaksi.add(new Transaksi(idTrx, "Bunga", nominalBunga));
		
		System.out.println("Bunga " + sukuBunga + "% berhasil ditambahkan: Rp" + nominalBunga);
	}
	

}