package ch.unibas.ias.webcrawler.webcrawler;

import ch.unibas.ias.webcrawler.database.Database;
import ch.unibas.ias.webcrawler.database.UrlQueue;
import org.jsoup.nodes.Document;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.List;
import java.util.Set;

public interface Crawler {

    void crawl();

    Database getDatabase();
    UrlQueue getQueue();
}
