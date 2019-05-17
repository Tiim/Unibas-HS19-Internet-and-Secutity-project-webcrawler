package ch.unibas.ias.webcrawler.database;

import java.net.MalformedURLException;
import java.net.URL;

public class QueueExtender implements UrlQueue {

    private UrlQueue queue;

    public QueueExtender(UrlQueue q) {

        queue = q;
    }

    @Override
    public void push(URL url) {

        url = getCleanedUrl(url);

        queue.push(url);
        try {
            URL root = getRoot(url);
            queue.push(root);
        } catch (MalformedURLException e) {
            System.out.println("Failed to get root for url " + url);
            e.printStackTrace();
        }
    }

    private URL getRoot(URL url) throws MalformedURLException {
        String protocol = url.getProtocol();
        String host = url.getHost();
        int port = url.getPort();

        // if the port is not explicitly specified in the input, it will be -1.
        if (port == -1) {
            return new URL(String.format("%s://%s", protocol, host));
        } else {
            return new URL(String.format("%s://%s:%d", protocol, host, port));
        }
    }

    private URL getCleanedUrl(URL urls) {
        String url = urls.toString();
        if (url != null) {
            int queryPosition = url.indexOf('?');
            if (queryPosition <= 0) {
                queryPosition = url.indexOf('#');
            }

            if (queryPosition >= 0) {
                url = url.substring(0, queryPosition);
            }
        }
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return urls;
        }
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public int crawled() {
        return queue.crawled();
    }

    @Override
    public URL poll() {
        return queue.poll();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
