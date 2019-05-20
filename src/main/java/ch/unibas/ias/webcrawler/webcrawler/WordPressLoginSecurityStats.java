package ch.unibas.ias.webcrawler.webcrawler;


import ch.unibas.ias.webcrawler.StringHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WordPressLoginSecurityStats {

    private MyDocument document;
    private Document loginDocument;
    private boolean canAccessAdminLogin;
    private boolean hasJetpackPlugin;
    private boolean hasProveYourHumanity;
    private boolean hasRecaptcha;
    private boolean hasForceStrongPassword;


    public WordPressLoginSecurityStats(MyDocument document) {
        this.document = document;
        this.loginDocument = createLoginDocument();
        this.canAccessAdminLogin = determineAdminLogin();
        this.hasJetpackPlugin = determineJetPack();
        this.hasProveYourHumanity = determineProveYourHumanity();
        this.hasRecaptcha = determineRecaptcha();
        this.hasForceStrongPassword = determineForceStrongPassword();
    }

    private Document createLoginDocument() {
        try {
            return Jsoup.connect(createAdminLoginURL()).get();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean determineAdminLogin() {
        return (this.loginDocument == null) ?  false :  true;
    }

    private String createAdminLoginURL() {
        String loginURL;
        if(this.document.getDocument().location().endsWith("/")) {
            loginURL = this.document.getDocument().location() + "wp-admin";
        } else {
            loginURL = this.document.getDocument().location() + "/wp-admin";
        }
        return loginURL;
    }

    private boolean determineJetPack() {
        if(this.loginDocument!=null) {
            if (StringHelper.containsIgnoreCase(loginDocument.outerHtml(),"jetpack")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean determineProveYourHumanity() {
        if(this.loginDocument!=null) {
            if (StringHelper.containsIgnoreCase(loginDocument.outerHtml(),"Prove your humanity")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean determineRecaptcha() {
        if(this.loginDocument!=null) {
            if (StringHelper.containsIgnoreCase(loginDocument.outerHtml(),"recaptcha")&&StringHelper.containsIgnoreCase(loginDocument.outerHtml(),"google")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean determineForceStrongPassword() {
        if(this.loginDocument!=null) {
            if (StringHelper.containsIgnoreCase(loginDocument.outerHtml(),"force-strong-passwords")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean getCanAccessAdminLogin() {
        return canAccessAdminLogin;
    }

    public boolean getHasForceStrongPassword() {
        return hasForceStrongPassword;
    }

    public boolean getHasProveYourHumanity() {
        return hasProveYourHumanity;
    }

    public boolean getHasJetpackPlugin() {
        return hasJetpackPlugin;
    }

    public boolean getHasRecaptcha() {
        return hasRecaptcha;
    }

    public String toString() {
        return "Succesfully accessed wp-admin: " + getCanAccessAdminLogin() + "\n" +
               "Identified strong Password Plugin: " + getHasForceStrongPassword() + "\n" +
               "Identified prove your humanity: " + getHasProveYourHumanity() + "\n" +
               "Identified Jetpack Plugin: " + getHasJetpackPlugin() + "\n" +
               "Identified Recaptacha: " + getHasRecaptcha();
    }
}
