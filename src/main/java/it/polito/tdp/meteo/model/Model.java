package it.polito.tdp.meteo.model;

import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	MeteoDAO meteoDao;
	List<Citta> sequenza;

	Citta torino = new Citta("Torino");
	Citta milano = new Citta("Milano");
	Citta genova = new Citta("Genova");
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {
		this.meteoDao = new MeteoDAO();
	}

	
	public double getUmiditaMedia(int mese, String localita) {
		return this.meteoDao.getAVGLocalitaMese(mese, localita);
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		
		List<Rilevamento> rilevamentiT = this.meteoDao.getAllRilevamentiLocalita(torino.getNome());
		torino.setRilevamenti(rilevamentiT);
		List<Rilevamento> rilevamentiM = this.meteoDao.getAllRilevamentiLocalita(milano.getNome());
		milano.setRilevamenti(rilevamentiM);
		List<Rilevamento> rilevamentiG = this.meteoDao.getAllRilevamentiLocalita(genova.getNome());
		genova.setRilevamenti(rilevamentiG);
		
		sequenza = new LinkedList<Citta>();
		List<Citta> parziale = new LinkedList<Citta>();
		trovaSequenzaRicorsiva(parziale, 0);
		
		return sequenza;
	}


	private void trovaSequenzaRicorsiva(List<Citta> parziale, int Livello) {
		if(parziale.size() == 15) {
			sequenza.addAll(parziale);
		}
		
		if(torino.getCounter() > 6 || milano.getCounter() > 6 || genova.getCounter() > 6) {
			return;
		}
		
		
		
		
	}
	
	
	

}
