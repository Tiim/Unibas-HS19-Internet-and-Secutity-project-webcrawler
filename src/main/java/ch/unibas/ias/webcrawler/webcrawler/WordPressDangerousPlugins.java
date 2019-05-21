package ch.unibas.ias.webcrawler.webcrawler;

import ch.unibas.ias.webcrawler.StringHelper;

public class WordPressDangerousPlugins {

    private MyDocument document;

    private boolean hasWooCommerce; // 19 known vulnerabilities
    private boolean hasYoastSEO; // 10 known vulnerabilities
    private boolean hasRedirection; // multiple major hacks in its history
    private boolean hasNextGENGallery; // 14 vulnerabilities
    private boolean hasContactForm7;

    public WordPressDangerousPlugins(MyDocument document) {
        this.document = document;
        this.hasWooCommerce = checkWooCommerce();
        this.hasYoastSEO = checkYoastSEO();
        this.hasRedirection = checkRedirection();
        this.hasNextGENGallery = checkNextGENGallery();
        this.hasContactForm7 = checkContactForm7();

    }

    private boolean checkWooCommerce() {
        return StringHelper.containsIgnoreCase(this.document.getDocument().outerHtml(),"woocommerce");
    }

    private boolean checkYoastSEO() {
        return (StringHelper.containsIgnoreCase(this.document.getDocument().outerHtml(),"Yoast")&&StringHelper.containsIgnoreCase(this.document.getDocument().outerHtml(),"SEO"));
    }

    private boolean checkRedirection() {
        return (StringHelper.containsIgnoreCase(this.document.getDocument().outerHtml(),"redirection"));
    }

    private boolean checkNextGENGallery() {
        return (StringHelper.containsIgnoreCase(this.document.getDocument().outerHtml(),"nextgen")&&StringHelper.containsIgnoreCase(this.document.getDocument().outerHtml(),"gallery"));
    }

    private boolean checkContactForm7() {
        return (StringHelper.containsIgnoreCase(this.document.getDocument().outerHtml(),"contact-form-7"));
    }

    public MyDocument getDocument() {
        return document;
    }

    public boolean isHasWooCommerce() {
        return hasWooCommerce;
    }

    public boolean isHasYoastSEO() {
        return hasYoastSEO;
    }

    public boolean isHasRedirection() {
        return hasRedirection;
    }

    public boolean isHasNextGENGallery() {
        return hasNextGENGallery;
    }

    public boolean isHasContactForm7() {
        return hasContactForm7;
    }


    public String toString() {
        return "Woocommerce: " + this.hasWooCommerce + "\n" +
                "YoastSeo: " + this.hasYoastSEO + "\n" +
                "Redirection: " + this.hasRedirection + "\n" +
                "NextGenGallery: " + this.hasNextGENGallery + "\n" +
                "ContactForm7: " + this.hasContactForm7;
    }


}
