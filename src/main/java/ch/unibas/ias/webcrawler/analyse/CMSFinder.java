package ch.unibas.ias.webcrawler.analyse;

import ch.unibas.ias.webcrawler.StringHelper;
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

    private String findCMSVersion() {
        String wpString = "name=\"generator\" content=\"WordPress";

        if (StringHelper.containsIgnoreCase(document.outerHtml(), wpString)) {
            int index = document.outerHtml().indexOf(wpString) + wpString.length() + 1;
            if (Character.isDigit(document.outerHtml().charAt(index))) {
                String version = document.outerHtml().substring(index, index + 8).replaceAll("[^\\d.]", "");
                return version;
            }
        }
        String dpString = "name=\"generator\" content=\"Drupal";
        if (StringHelper.containsIgnoreCase(document.outerHtml(), dpString)) {
            int index = document.outerHtml().indexOf(dpString) + dpString.length() + 1;
            if (Character.isDigit(document.outerHtml().charAt(index))) {
                String version = document.outerHtml().substring(index, index + 8).replaceAll("[^\\d.]", "");
                return version;
            }
        }
        String jmString = "name=\"generator\" content=\"Joomla";
        if (StringHelper.containsIgnoreCase(document.outerHtml(), jmString)) {
            int index = document.outerHtml().indexOf(jmString) + jmString.length() + 1;
            if (Character.isDigit(document.outerHtml().charAt(index))) {
                String version = document.outerHtml().substring(index, index + 8).replaceAll("[^\\d.]", "");
                return version;
            }
        }
        String mwString = "name=\"generator\" content=\"MediaWiki";       //wikipedia etc.
        if (StringHelper.containsIgnoreCase(document.outerHtml(), mwString)) {
            int index = document.outerHtml().indexOf(mwString) + mwString.length() + 1;
            if (Character.isDigit(document.outerHtml().charAt(index))) {
                String version = document.outerHtml().substring(index, index + 8).replaceAll("[^\\d.]", "");
                return version;
            }
        }

        return "?";
    }

    private String findCMSName() {
        String wpString = "name=\"generator\" content=\"WordPress";
        if (StringHelper.containsIgnoreCase(document.outerHtml(), wpString) ||
                StringHelper.containsIgnoreCase(document.outerHtml(), "https://yoast.com/wordpress/plugins/seo/")) {        //popular plugin
            return "Wordpress";
        }
        String dpString = "name=\"generator\" content=\"Drupal";
        if (StringHelper.containsIgnoreCase(document.outerHtml(), dpString)) {
            return "Drupal";
        }
        String jmString = "name=\"generator\" content=\"Joomla";
        if (StringHelper.containsIgnoreCase(document.outerHtml(), jmString)) {
            return "Joomla";
        }
        String mwString = "name=\"generator\" content=\"MediaWiki";       //wikipedia etc.
        if (StringHelper.containsIgnoreCase(document.outerHtml(), mwString)) {
            return "MediaWiki";
        }
        String sfString = "id=\"shopify-digital-wallet\" name=\"shopify-digital-wallet";
        if (StringHelper.containsIgnoreCase(document.outerHtml(), sfString)) {
            return "Shopify";
        }

            return "?";
    }

    public String getCMSVersion() {
        return this.CMSVersion;
    }

    public String getCMSName() {
        return this.CMSName;
    }

}

