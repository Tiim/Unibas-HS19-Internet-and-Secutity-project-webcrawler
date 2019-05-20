package ch.unibas.ias.webcrawler.webcrawler;

import ch.unibas.ias.webcrawler.database.*;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;

public class WebCrawler implements Crawler {


    //inventory of all links visited
    private final Database db;
    private final UrlQueue queue;
    private final double runtime;
    private final long startTime;

    private WebCrawler(final URL startURL, final double runtime, Database db, UrlQueue queue) {
        this.db = db;
        this.queue = queue;
        this.startTime = System.currentTimeMillis();
        this.runtime = runtime;
        crawl(startURL);
    }


    @Override
    public void crawl(final URL startURL) {
        queue.push(startURL);
            while (!queue.isEmpty() && (System.currentTimeMillis()-startTime)<runtime) {
                try {
                    Document currentWebPage = Jsoup.connect(queue.poll().toString()).get();
                    MyDocument document = new MyDocument(currentWebPage);
                    System.out.println("Crawled " + document);
                    WordPressLoginSecurityStats stats = null;
                    if(document.getCMS().equals("WordPress")) {
                        stats = new WordPressLoginSecurityStats(document);
                        System.out.println(stats);
                    }
                    save(document, stats, document);

                    final Elements linksOnPage = currentWebPage.select("a[href]");
                    for(Element e : linksOnPage) {
                        final String urlText = e.attr("abs:href");
                        final URL newURL = new URL(urlText);
                        queue.push(newURL);
                    }
                } catch(Exception e) {
                    System.out.print("");
                }
            }
        System.out.println("Crawl completed!");
    }

    private void save(MyDocument document, WordPressLoginSecurityStats stats, MyDocument myDocument) {
        db.addRecord(document.getDocument().location(), document.getDocument().outerHtml(),"", new Date(), stats, myDocument);
    }

    @Override
    public Database getDatabase() {
        return db;
    }

    @Override
    public UrlQueue getQueue() {
        return queue;
    }

    public static void main(String args[]) throws Exception {

        //Database db = new MemoryDatabase();
        DBConnection conn = new DBConnection();
        Database db = new H2Database(conn);

        UrlQueue queue = new QueueExtender(new H2Queue(conn));

        try {
          // call with Double.POSITIVE_INFINITY to run infinitely
          final Crawler crawler = new WebCrawler(new URL("https://dictionary-api.cambridge.org/"),180000, db, queue);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
