package ch.unibas.ias.webcrawler.database;

import ch.unibas.ias.webcrawler.webcrawler.WordPressLoginSecurityStats;

import java.util.Date;
import java.util.HashMap;

public interface Database {

  public void addRecord(String url, String html, String httpHeaders, Date downloadDate, WordPressLoginSecurityStats stats);

}