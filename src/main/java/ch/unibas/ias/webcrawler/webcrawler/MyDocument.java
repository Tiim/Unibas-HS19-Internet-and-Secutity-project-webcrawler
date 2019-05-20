package ch.unibas.ias.webcrawler.webcrawler;

import ch.unibas.ias.webcrawler.StringHelper;
import ch.unibas.ias.webcrawler.analyse.CMSFinder;
import org.checkerframework.checker.units.qual.C;
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
        this.CMSVersion = this.findCMSVersion();
    }

    public Document getDocument() {
        return this.document;
    }


    @Override
    public String toString() {
        return "Doc{" +
                "  location = " + document.location() + "\t" +
                "CMS = " + this.CMS + "\t" +
                "CMSVer = " + this.CMSVersion + "\t" +
                '}';
    }

    public String determinCMS() {
        CMSFinder myCMSFinder = new CMSFinder(document);
        return myCMSFinder.getCMSName();
        /*if( document.outerHtml().contains("wordpress")||
            document.outerHtml().contains("WordPress")||
            document.outerHtml().contains("WORDPRESS")||
            document.outerHtml().contains("word press")||
            document.outerHtml().contains("Word Press")) {
            return "WordPress";
        } else if( document.outerHtml().contains("Joomla")||
                document.outerHtml().contains("joomla")||
                document.outerHtml().contains("JOOMLA")) {
            return "Joomla";

        } else if( document.outerHtml().contains("Drupal")||
                document.outerHtml().contains("drupal")||
                document.outerHtml().contains("DRUPAL")) {
            return "Drupal";
        }
        return "?";
        */
    }

    private String findCMSVersion() {
        CMSFinder myCMSFinder = new CMSFinder(document);
        return myCMSFinder.getCMSVersion();
        /*
        if(StringHelper.containsIgnoreCase(document.outerHtml(),"name=\"generator\" content=\"WordPress")) {
            int index = StringHelper.indexOfIgnoreCase(document.outerHtml(),"name=\"generator\" content=\"WordPress");
            index+=36;
            if(Character.isDigit(document.outerHtml().charAt(index))) {
                if (Character.isDigit(document.outerHtml().charAt(index + 5))) {
                    return document.outerHtml().substring(index, index + 5);
                }
                return document.outerHtml().substring(index, index + 3);
            } else {
                return "?";
            }

        } else if(StringHelper.containsIgnoreCase(document.outerHtml(),"name=\"generator\" content=\"Drupal")) {
            int index = StringHelper.indexOfIgnoreCase(document.outerHtml(),"name=\"generator\" content=\"Drupal");
            index+=33;
            return document.outerHtml().substring(index, index+1);
        } else if(StringHelper.containsIgnoreCase(document.outerHtml(),"name=\"generator\" content=\"Joomla")) {
            int index = StringHelper.indexOfIgnoreCase(document.outerHtml(),"name=\"generator\" content=\"Joomla");
            index+=33;
            return document.outerHtml().substring(index, index+3);
        }
        return "?";
        */
    }

    public String getCMSVersion() {
        return this.CMSVersion;
    }
    public String getCMS() {
        return this.CMS;
    }

}



