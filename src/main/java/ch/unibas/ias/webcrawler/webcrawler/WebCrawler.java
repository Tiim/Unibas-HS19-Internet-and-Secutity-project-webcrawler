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
    private final boolean master;
    private final double runtime;
    private final long startTime;

    public WebCrawler(final double runtime, Database db, UrlQueue queue, boolean master) {
        this.db = db;
        this.queue = queue;
        this.master = master;
        this.startTime = System.currentTimeMillis();
        this.runtime = runtime;
    }

    public static void startUrls(UrlQueue queue) {
        try {
            queue.push(new URL("https://scbirs.ch"));
            queue.push(new URL("https://reddit.com/r/wordpress"));
            queue.push(new URL("https://news.ycombinator.com/"));
            queue.push(new URL("https://dev.to/"));
            queue.push(new URL("https://github.com/"));
            queue.push(new URL("https://www.microsoft.com/de-ch"));
            queue.push(new URL("https://de.wordpress.org/themes/browse/featured/"));
            queue.push(new URL("https://de.wordpress.org"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void crawl() {

        int loops = 0;

        while (!queue.isEmpty() && (System.currentTimeMillis()-startTime)<runtime) {
            try {
                Document currentWebPage = Jsoup.connect(queue.poll().toString()).get();
                MyDocument document = new MyDocument(currentWebPage);
                WordPressLoginSecurityStats stats = null;
                WordPressDangerousPlugins plugins = null;
                if(document.getCMS().equals("Wordpress")) {
                    stats = new WordPressLoginSecurityStats(document);
                    plugins = new WordPressDangerousPlugins(document);
                }
                save(document, stats, plugins, document);



                final Elements linksOnPage = currentWebPage.select("a[href]");
                for(Element e : linksOnPage) {
                    final String urlText = e.attr("abs:href");
                    final URL newURL = new URL(urlText);
                    queue.push(newURL);
                }
                //System.out.println("Crawled " + document + " links=" + linksOnPage.size());


                if (loops % 5 == 0 && master) {
                    //System.out.println("========================");
                    System.out.println("PROGRESS: crawled:" + queue.crawled() + ", left in queue: " + queue.size());
                    //System.out.println("========================");
                }

                loops += 1;
            } catch(Exception e) {
                System.out.print("");
            }
        }
        //System.out.println("Crawl completed!");
    }

    private void save(MyDocument document, WordPressLoginSecurityStats stats, WordPressDangerousPlugins plugs, MyDocument myDocument) {
        db.addRecord(document.getDocument().location(), document.getDocument().outerHtml(),"", new Date(), stats, plugs, myDocument);
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

        WebCrawler.startUrls(queue);
        // call with Double.POSITIVE_INFINITY to run infinitely
        final Crawler crawler = new WebCrawler(180000, db, queue, true);
        crawler.crawl();

    }
}
