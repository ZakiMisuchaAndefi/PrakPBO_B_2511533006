package pekan4_2511533006;

public class RekeningGiro extends Rekening {
	private double batasOverdraft;
	
	public RekeningGiro(String nama, String nomor, double saldoAwal, String pinAwal, double batasOverdraft) {
		super(nomor, nama, saldoAwal, pinAwal);
		this.batasOverdraft = batasOverdraft;
	}
	
	//Get giro
	public double getBatasOverdraft() {
		return batasOverdraft;
	}
		

}
