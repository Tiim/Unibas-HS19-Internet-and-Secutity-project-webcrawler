package ch.unibas.ias.webcrawler.webcrawler;

import ch.unibas.ias.webcrawler.database.Database;
import ch.unibas.ias.webcrawler.database.FileDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.util.resources.cldr.rw.CurrencyNames_rw;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WebCrawler implements Crawler {

    //inventory of all links visited
    private final Set<URL> links;
    private final long startTime;

    private WebCrawler(final URL startURL) {
        this.links = new HashSet<>();
        this.startTime = System.currentTimeMillis();
        crawl(initURLS(startURL));
    }

    private Set<URL> initURLS(final URL startURL) {
        return Collections.singleton(startURL);
    }

    @Override
    public void crawl(final Set<URL> urls) {
        //remove all URLS we have already visited
        urls.removeAll(this.links);
        if(!urls.isEmpty()) {
            final Set<URL> newURLS = new HashSet<>();
            try {
                //add all links on current webpage to Hashset
                this.links.addAll(urls);
                for(final URL url : urls) {
                    System.out.println("time = " + (System.currentTimeMillis()-startTime) +
                            "   connected to: " + url);
                    //save current URL page into Document object
                    final Document document = Jsoup.connect(url.toString()).get();
                    //save all URLS on current page into Elements object
                    final Elements linksOnPage = document.select("a[href]");
                    //save all URLS on current page into newURLS HashSet;
                    for(final Element element : linksOnPage) {
                        final String urlText = element.attr("abs:href");
                        final URL discoveredURL = new URL(urlText);
                        newURLS.add(discoveredURL);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            crawl(newURLS);
        }
    }

    public static void main(String args[]) {
        try {
          final Crawler crawler = new WebCrawler(new URL("http://www.amazon.com/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }
}
