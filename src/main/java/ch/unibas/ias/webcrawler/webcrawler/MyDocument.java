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

    private String CMS;

    private String CMSVersion;

    /**
     * consturctor
     * @param document
     */
    public MyDocument(Document document) {
        this.document = document;
        this.CMS = this.determinCMS();
        this.CMSVersion = this.findWPVersion();
    }

    public Document getDocument() {
        return this.document;
    }


    @Override
    public String toString() {
        return "Doc{" +
                "location=" + document.location() +
                '}';
    }

    public String determinCMS() {
        if( document.outerHtml().contains("wordpress")||
            document.outerHtml().contains("WordPress")||
            document.outerHtml().contains("WORDPRESS")||
            document.outerHtml().contains("word press")||
            document.outerHtml().contains("Word Press")) {
            return "WordPress";
        }
        return "?";
    }

    private String findWPVersion() {
        if(document.outerHtml().contains("name=\"generator\" content=\"WordPress")) {
            int index = document.outerHtml().indexOf("name=\"generator\" content=\"WordPress ");
            index+=36;
            return document.outerHtml().substring(index, index+5);

        }
        return "?";
    }

    public String getCMSVersion() {
        return this.CMSVersion;
    }
    public String getCMS() {
        return this.CMS;
    }

    public String checkWPLogin() {
        String logintest;
        if(document.location().endsWith("/")) {
            logintest = document.location() + "wp-admin";
        } else {
            logintest = document.location() + "/wp-admin";
        }
        return logintest;
    }

}



