package ch.unibas.ias.webcrawler.webcrawler;

import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;
import java.util.Set;

public interface Crawler {

    void crawl(final URL startURL);

    List<MyDocument> getVisitedLinks();

}
