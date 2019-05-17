package ch.unibas.ias.webcrawler.database;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Queue;

public class MemoryQueue implements UrlQueue {

    private int crawled = 0;
    private Queue<URL> q = new ArrayDeque<>();

    @Override
    public boolean isEmpty() {
        return q.isEmpty();
    }

    @Override
    public void push(URL url) {
        q.add(url);
    }

    @Override
    public int size() {
        return q.size();
    }

    @Override
    public int crawled() {
        return crawled;
    }

    @Override
    public URL poll() {
        crawled++;
        return q.poll();
    }
}
