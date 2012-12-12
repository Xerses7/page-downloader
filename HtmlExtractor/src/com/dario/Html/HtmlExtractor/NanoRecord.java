package com.dario.Html.HtmlExtractor;

public class NanoRecord {
	private String azienda, email, tel, fax, sito, stato;
	private String[] rec = new String[6];
	public NanoRecord(){
	};
	public String getAzienda(){ if (this.azienda == null){this.azienda = " ";} return azienda; };
	public String getEmail(){ if (this.email == null){this.email = " ";} return email; };
	public String getTel(){ if (this.tel == null){this.tel = " ";} return tel; };
	public String getFax(){ if (this.fax == null){this.fax = " ";} return fax; };
	public String getSito(){ if (this.sito == null){this.sito = " ";} return sito; };
	public String getStato(){ if (this.stato == null){this.stato = " ";} return stato; };
	public String[] getRecord(){
		rec[0] = this.getAzienda();
		rec[1] = this.getEmail();
		rec[2] = this.getTel();
		rec[3] = this.getFax();
		rec[4] = this.getSito();
		rec[5] = this.getStato();
		return rec;	
	};
	public void setAzienda(String azienda){ this.azienda = azienda; };
	public void setEmail(String email){ this.email = email; };
	public void setTel(String tel){ this.tel = tel; };
	public void setFax(String fax){ this.fax = fax; };
	public void setSito(String sito){ this.sito = sito; };
	public void setStato(String stato){ this.stato = stato; };
}