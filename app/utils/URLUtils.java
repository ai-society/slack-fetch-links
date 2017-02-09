package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by scvalencia606 on 2/8/17.
 */
public class URLUtils {

    public static String extractLinks(String text) {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher matche = p.matcher(text);
        int i = 0;
        while(matche.find()) {
            text = text.replaceAll(matche.group(i), "").trim();
            i++;
        }

        return text;
    }
}
