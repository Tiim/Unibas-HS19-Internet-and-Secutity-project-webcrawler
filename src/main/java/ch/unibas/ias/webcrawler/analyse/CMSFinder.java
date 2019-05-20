package ch.unibas.ias.webcrawler.analyse;

import ch.unibas.ias.webcrawler.webcrawler.MyDocument;
import org.jsoup.nodes.Document;


public class CMSFinder {
    private Document document;
    private String CMSName;
    private String CMSVersion;


    public CMSFinder(Document document) {
        this.document = document;
        this.CMSName = this.findCMSName();
        this.CMSVersion = this.findCMSVersion();
    }

    public String findCMSVersion() {
        String wpString = "name=\"generator\" content=\"WordPress";
        if (document.outerHtml().contains(wpString)) {
            int index = document.outerHtml().indexOf(wpString) + wpString.length();
            String version = document.outerHtml().substring(index, index + 8).replaceAll("[^\\d.]", "");
            return version;
        }
        String dpString = "name=\"generator\" content=\"Drupal";
        if (document.outerHtml().contains(dpString)) {
            int index = document.outerHtml().indexOf(dpString) + dpString.length();
            String version = document.outerHtml().substring(index, index + 8).replaceAll("[^\\d.]", "");
            return version;
        }
        String jmString = "name=\"generator\" content=\"Joomla";
        if (document.outerHtml().contains(jmString)) {
            int index = document.outerHtml().indexOf(jmString) + jmString.length();
            String version = document.outerHtml().substring(index, index + 8).replaceAll("[^\\d.]", "");
            return version;
        }

        return "-";
    }

    public String findCMSName() {
        String wpString = "name=\"generator\" content=\"WordPress";
        if (document.outerHtml().contains(wpString)) {
            return "Wordpress";
        }
        String dpString = "name=\"generator\" content=\"Drupal";
        if (document.outerHtml().contains(dpString)) {
            return "Drupal";
        }
        String jmString = "name=\"generator\" content=\"Joomla";
        if (document.outerHtml().contains(jmString)) {
            return "Joomla";
        }

        return "-";
    }

    public String getCMSVersion() {
        return this.CMSVersion;
    }

    public String getCMSName() {
        return this.CMSName;
    }
}

