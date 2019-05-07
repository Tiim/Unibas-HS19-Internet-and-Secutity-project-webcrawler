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

}
