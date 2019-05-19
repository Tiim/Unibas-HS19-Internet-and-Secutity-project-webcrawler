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
        int currentDist = 0;
        queue.push(startURL);
            while (!queue.isEmpty() && (System.currentTimeMillis()-startTime)<runtime) {
                try {
                    Document currentWebPage = Jsoup.connect(queue.poll().toString()).get();
                    MyDocument document = new MyDocument(currentWebPage, currentDist);
                    save(document);
                    System.out.println("Crawled " + document + " " + document.getCMS());
                    currentDist++;

                    final Elements linksOnPage = currentWebPage.select("a[href]");
                    for(Element e : linksOnPage) {
                        final String urlText = e.attr("abs:href");
                        final URL newURL = new URL(urlText);
                        queue.push(newURL);
                    }

                } catch(MalformedURLException e) {
                    //TODO figure out if this is an issue, malformed URLS happen quite often
                    //e.printStackTrace();
                } catch(UnsupportedMimeTypeException e) {
                    //TODO figure out if this is an issue, UnsupportedMimeType happen quite rarely, seems to happen with pdf
                    //https://www.unibas.ch/dam/jcr:5a79b475-5fc5-4f16-be03-91b06adfd5c3/AS_MAGAZIN_2018_Web.pdf
                    //e.printStackTrace();
                } catch(HttpStatusException e) {
                    //TODO figure out if this is an issue, HttpStatusException happen quite rarely, happened when accessing:
                    //https://www.linkedin.com/school/university-of-basel/
                    //e.printStackTrace();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        System.out.println("Crawl completed!");
    }

    private void save(MyDocument document) {
        db.addRecord(document.getDocument().location(), document.getDocument().outerHtml(),"", new Date());
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
          final Crawler crawler = new WebCrawler(new URL("https://bonestructure.ca/en/os/"),180000, db, queue);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
