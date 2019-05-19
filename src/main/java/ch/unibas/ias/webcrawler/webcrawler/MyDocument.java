package ch.unibas.ias.webcrawler.webcrawler;

import org.jsoup.nodes.Document;

import java.util.concurrent.ExecutionException;

public class MyDocument {
    /**
     * Jsoup document
     */
    private final Document document;
    /**
     * distance to the startURL
     */
    private final int distance;

    /**
     * consturctor
     * @param document
     * @param distance
     */
    public MyDocument(Document document, int distance) {
        this.document = document;
        this. distance = distance;
    }

    public Document getDocument() {
        return this.document;
    }

    public int getDistance() {
        return this.distance;
    }

    @Override
    public String toString() {
        return "Doc{" +
                "location=" + document.location() +
                ", distance=" + distance +
                '}';
    }

    public String getCMS() {
        if( document.outerHtml().contains("wordpress")||
            document.outerHtml().contains("WordPress")||
            document.outerHtml().contains("WORDPRESS")||
            document.outerHtml().contains("word press")||
            document.outerHtml().contains("Word Press")) {
            return "WordPress " + findWPVersion();
        }
        return "?";
    }

    private String findWPVersion() {
        if(document.outerHtml().contains("WordPress")) {
            int index = document.outerHtml().indexOf("WordPress");
            index+=10;
            return document.outerHtml().substring(index, index+5);

        }
        return "?";
    }

}



