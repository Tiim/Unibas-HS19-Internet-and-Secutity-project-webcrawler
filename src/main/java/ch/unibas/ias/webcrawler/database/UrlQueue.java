package ch.unibas.ias.webcrawler.database;

import java.net.URL;

public interface UrlQueue {

    void push(URL url);
    int size();
    int crawled();
    URL poll();
    boolean isEmpty();
}
