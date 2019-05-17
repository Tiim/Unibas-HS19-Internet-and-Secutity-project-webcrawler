package ch.unibas.ias.webcrawler.webcrawler;

import ch.unibas.ias.webcrawler.database.Database;
import ch.unibas.ias.webcrawler.database.FileDatabase;
import org.jsoup.Connection;
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
import java.util.regex.PatternSyntaxException;

public class WebCrawler implements Crawler {


    //inventory of all links visited
    private final List<MyDocument> visitedLinks;
    private final double runtime;
    private final long startTime;

    private WebCrawler(final URL startURL, final double runtime) {
        this.visitedLinks = new LinkedList<>();
        this.startTime = System.currentTimeMillis();
        this.runtime = runtime;
        crawl(startURL);
    }

    @Override
    public void crawl(final URL startURL) {
        Queue<URL> toVisit = new LinkedList<>();
        Queue<Integer> distances = new LinkedList<>();
        int currentDist = 0;
        toVisit.add(startURL);
        distances.add(currentDist);
            while (!toVisit.isEmpty() && (System.currentTimeMillis()-startTime)<runtime) {
                try {
                    Document currentWebPage = Jsoup.connect(toVisit.poll().toString()).get();
                    currentDist = distances.poll();
                    MyDocument document = new MyDocument(currentWebPage, currentDist);
                    this.visitedLinks.add(document);
                    System.out.println("Crawled " + document);
                    currentDist++;

                    final Elements linksOnPage = currentWebPage.select("a[href]");
                    for(Element e : linksOnPage) {
                        final String urlText = e.attr("abs:href");
                        final URL newURL = new URL(urlText);
                        toVisit.add(newURL);
                        distances.add(currentDist);
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

    @Override
    public List<MyDocument> getVisitedLinks() {
        return visitedLinks;
    }

    public static void main(String args[]) {
        try {
          // call with Double.POSITIVE_INFINITY to run infinitely
          final Crawler crawler = new WebCrawler(new URL("http://mysmallwebpage.com/"),10000);
          Database database = new FileDatabase();

          List<MyDocument> results = crawler.getVisitedLinks();

          for(MyDocument d : results) {
              //database.addRecord(d.getDocument().location(),d.getDocument().html(),d.getDocument().head().toString(),Calendar.getInstance().getTime());
              System.out.println(d.getDistance());
              System.out.println(d.getDocument().location());
          }




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
