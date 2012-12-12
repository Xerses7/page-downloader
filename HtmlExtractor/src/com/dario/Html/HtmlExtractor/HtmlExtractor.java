package com.dario.Html.HtmlExtractor;

import java.io.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import com.csvreader.CsvWriter;




public class HtmlExtractor {
	
	private static Document getDocument(String webAddress){
		Document doc = null;
		try {
			Connection conn = Jsoup.connect(webAddress);
			conn.timeout(15000);
			conn.userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)");
			doc = conn.get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return doc; 
	}
	
	private static File preparaFile() {
		String outputFile = "users.csv";
		File f = new File(outputFile);
		try {
			CsvWriter csvOutput = new CsvWriter(new FileWriter(f, true), ';');
			csvOutput.write("azienda");
			csvOutput.write("email");
			csvOutput.write("tel");
			csvOutput.write("fax");
			csvOutput.write("sito");
			csvOutput.write("stato");
			csvOutput.endRecord();
			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public static void main (String[] args){
		System.out.println("Inizio");
		String urlPartial = new String();
		String supplierID = new String();
		urlPartial = "http://www.azonano.com/suppliers.aspx?SupplierID=";
		String[] legenda = new String[6];
		legenda[0] = "azienda";
		legenda[1] = "email";
		legenda[2] = "tel";
		legenda[3] = "fax";
		legenda[4] = "sito";
		legenda[5] = "stato";
		try {
			File file = preparaFile();
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(file, true), ';');
			for (int i = 2000; i < 2400; i++){
				NanoRecord nr = new NanoRecord();
				supplierID = ""+i;
				System.out.println("Ciclo For iniziale: "+ supplierID);
				//seleziona il DOM dal codice della pagina
				Document doc = getDocument(urlPartial+supplierID);
				
				//estrai il nome dell'azienda
				Element supplierName = doc.select("span.supplierName").first();
				if (supplierName == null){
					continue;
				}
				String nomeAzienda = supplierName.text();
			
				//estrai il paragrafo che contiene i dati
				Element dataParagraph = doc.select("div#tab_profile p").first();
	
				//seziona il paragrafo in base ai </ br>
				String paragrafoHtml = dataParagraph.html();
				String[] paragrafoHtmlSpezzato = paragrafoHtml.split("<br />");
				
				//crea un'ArrayList di dati
				nr.setAzienda(nomeAzienda);
				for (int p = 0 ; p < paragrafoHtmlSpezzato.length ; p++){
					if (paragrafoHtmlSpezzato[p].contains("PH:")){
						String tel = paragrafoHtmlSpezzato[p].replaceFirst("PH: ", "");
						String stato = paragrafoHtmlSpezzato[p-1];
						nr.setTel(tel);
						nr.setStato(stato);
					}
					//inserisci il numero di fax in posizione 8	
					else if (paragrafoHtmlSpezzato[p].contains("Fax:")){
						String fax = paragrafoHtmlSpezzato[p].replaceFirst("Fax: ", "");

						nr.setFax(fax);
						if (paragrafoHtmlSpezzato[p-1].contains("PH:")){
							continue;
						} else {
							nr.setStato(paragrafoHtmlSpezzato[p-1]);
						}
					}
					else if (paragrafoHtmlSpezzato[p].contains("Email")){
						Element eMail = dataParagraph
								.getElementById("ctl00_cphBody_supplierPage_hypEmail");
						nr.setEmail(eMail.text());
					}
					else if (paragrafoHtmlSpezzato[p].contains("Visit")){
						//estrai l'indirizzo Web dell'azienda
						Element webAddress = dataParagraph
								.getElementById("ctl00_cphBody_supplierPage_hypWebsite");
						nr.setSito(webAddress.attr("href")); // "http://example.com/"
						if (!paragrafoHtmlSpezzato[p-1].contains("PH:") &&
							!paragrafoHtmlSpezzato[p-1].contains("Fax:") &&
							!paragrafoHtmlSpezzato[p-1].contains("Email"))
						{
							nr.setStato(paragrafoHtmlSpezzato[p-1]);
						}
					}
				}
				//la chiusura del record si chiama alla fine del ciclo while che lavora sulla
				//singola riga
				csvOutput.writeRecord(nr.getRecord());
				System.out.println("Chiudi record");
			}
			//chiudi il file e scrivi i dati definitivamente
			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	} 
}
