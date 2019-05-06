package ch.unibas.ias.webcrawler.webcrawler;

import java.net.URL;
import java.util.Set;

public interface Crawler {

    void crawl(final Set<URL> urls);

}
