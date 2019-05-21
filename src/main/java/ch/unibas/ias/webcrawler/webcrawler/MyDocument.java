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
    }

    private String findCMSVersion() {
        CMSFinder myCMSFinder = new CMSFinder(document);
        return myCMSFinder.getCMSVersion();
    }

    public String getCMSVersion() {
        return this.CMSVersion;
    }
    public String getCMS() {
        return this.CMS;
    }

}



