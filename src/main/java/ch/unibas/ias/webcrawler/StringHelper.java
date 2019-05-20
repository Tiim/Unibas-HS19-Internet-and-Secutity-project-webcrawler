package ch.unibas.ias.webcrawler;

public class StringHelper {

    public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }

    public static int indexOfIgnoreCase(String str, String substring) {
        return str.toLowerCase().indexOf(substring.toLowerCase());
    }

}
