/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ch.unibas.ias.webcrawler;

import ch.unibas.ias.webcrawler.database.*;
import ch.unibas.ias.webcrawler.webcrawler.Crawler;
import ch.unibas.ias.webcrawler.webcrawler.WebCrawler;

import java.net.URL;

public class App {

    private static final int THREADS = 30;
    private static final double RUNTIME = Double.POSITIVE_INFINITY;


    public static void run(Database db, UrlQueue queue) {
        try {
            final Crawler crawler = new WebCrawler(RUNTIME, db, queue);
            crawler.crawl();
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws Exception {

        Thread[] t = new Thread[THREADS];

        DBConnection conn = new DBConnection();
        Database db = new H2Database(conn);

        UrlQueue queue = new QueueExtender(new H2Queue(conn));

        WebCrawler.startUrls(queue);

        for(int i = 0; i < THREADS; i++) {
            t[i] = new Thread(() -> run(db, queue));
            t[i].start();
        }

    }
}
