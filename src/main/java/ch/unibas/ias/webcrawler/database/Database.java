package ch.unibas.ias.webcrawler.database;

import ch.unibas.ias.webcrawler.webcrawler.MyDocument;
import ch.unibas.ias.webcrawler.webcrawler.WordPressLoginSecurityStats;

import java.util.Date;

public interface Database {

  public void addRecord(String url, String html, String httpHeaders, Date downloadDate, WordPressLoginSecurityStats stats, MyDocument myDocument);

}