package utils;

import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;

import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by scvalencia606 on 2/8/17.
 */
public class URLUtils {

    public static String extractLinks(String text) {
        LinkExtractor linkExtractor = LinkExtractor.builder()
            .linkTypes(EnumSet.of(LinkType.URL, LinkType.EMAIL))
            .build();
        Iterable<LinkSpan> links = linkExtractor.extractLinks(text);
        LinkSpan link = links.iterator().next();
        return text.substring(link.getBeginIndex(), link.getEndIndex());
    }
}
