package ch.unibas.ias.webcrawler.database;

import java.util.Date;

public interface Database {

  public void addRecord(String url, String html, String httpHeaders, Date downloadDate);

}