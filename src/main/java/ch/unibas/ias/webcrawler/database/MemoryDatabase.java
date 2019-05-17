package ch.unibas.ias.webcrawler.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemoryDatabase implements Database {

    private final List<Entry> entries = new ArrayList<>();

    private static class Entry {
        private String url;
        private String html;
        private String headers;
        private final Date date;

        public Entry(String url, String html, String headers, Date date) {

            this.url = url;
            this.html = html;
            this.headers = headers;
            this.date = date;
        }
    }

    @Override
    public void addRecord(String url, String html, String httpHeaders, Date downloadDate) {
        entries.add(new Entry(url, html, httpHeaders, downloadDate));
    }
}
