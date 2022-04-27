package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.LinkedList;

import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	MeteoDAO meteoDao;
	List<Citta> sequenza;
	List<Citta> leCitta = new ArrayList<Citta>();

	Citta torino = new Citta("Torino");
	Citta milano = new Citta("Milano");
	Citta genova = new Citta("Genova");
	
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {
		this.meteoDao = new MeteoDAO();
		leCitta.add(torino);
		leCitta.add(milano);
		leCitta.add(genova);
	}
	
	public double getUmiditaMedia(int mese, String localita) {
		return this.meteoDao.getAVGLocalitaMese(mese, localita);
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		
		torino.setRilevamenti(this.meteoDao.getAllRilevamentiLocalitaMese(mese, "Torino"));
		milano.setRilevamenti(this.meteoDao.getAllRilevamentiLocalitaMese(mese, "Milano"));
		genova.setRilevamenti(this.meteoDao.getAllRilevamentiLocalitaMese(mese, "Genova"));
		
		sequenza = null;
		List<Citta> parziale = new LinkedList<Citta>();
		trovaSequenzaRicorsiva(parziale, 0);
		
		return sequenza;
	}


	private void trovaSequenzaRicorsiva(List<Citta> parziale, int Livello) {
		if(Livello == NUMERO_GIORNI_TOTALI) {					//caso terminale
			double costo = costo(parziale);	
			if(sequenza == null || costo < costo(sequenza)) {			//se il costo migliora, lo cambio
				sequenza = new LinkedList<Citta>(parziale);
			}
		}else {															//caso intermedio
			for(Citta prova : leCitta) {
				if(valida(prova, parziale)) {							//provo ogni citta, se è valida
					parziale.add(prova);								//aggiungo
					trovaSequenzaRicorsiva(parziale, Livello+1);		//ricorsione
					parziale.remove(parziale.size()-1);					//backtracking
				}
			}
		}
	}
	
	private boolean valida(Citta prova, List<Citta> parziale) {			//controllo se la città che sto aggiungendo è valida
		int conta = 0;
		for(Citta precedente : parziale) {
			if(precedente.equals(prova)) {
				conta++;
			}
		}
		
		if(conta>=NUMERO_GIORNI_CITTA_MAX) {
			return false;
		}
		
		if(parziale.size()==0) {
			return true;
		}
		
		if(parziale.size() == 1 || parziale.size() == 2) {
			if(parziale.get(parziale.size()-1).equals(prova)) {
				return true;
			}else {
				return false;
			}
		}
		
		if(parziale.get(parziale.size()-1).equals(prova)) {
			return true;
		}
		
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3))) {
			return true;
		}
		
		return false;
	}

	public double costo(List<Citta> parziale) {							//calcolo il costo
		double costo = 0;
		
		for(int giorno=1;giorno<=NUMERO_GIORNI_TOTALI;giorno++) {
			Citta c = parziale.get(giorno-1);
			double umidita = c.getRilevamenti().get(giorno-1).getUmidita();
			costo = costo + umidita;
		}
		
		for(int giorno=2;giorno<=NUMERO_GIORNI_TOTALI;giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo = costo + COST;
			}
		}
		
		return costo;
	}
	
	
	

}
