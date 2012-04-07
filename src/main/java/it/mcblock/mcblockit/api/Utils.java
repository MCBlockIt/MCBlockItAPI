package it.mcblock.mcblockit.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Utilities class
 * 
 * @author Matt Baxter
 * 
 */
public class Utils {
    public static final char COLOR_CHAR = '\u00A7';

    /**
     * Combine a String array. Should be outofbounds-proof
     * 
     * @param args
     *            the array to combine
     * @param delimiter
     *            separate elements by this string
     * @param start
     *            starting point for combination
     * @param finish
     *            ending point for combination
     * @return combined String
     */
    public static String combineSplit(String[] args, String delimiter, int start, int finish) {
        if (args.length < start) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        for (int x = start; (x < args.length) && (x <= finish); x++) {
            if (builder.length() > 0) {
                builder.append(delimiter);
            }
            builder.append(args[x]);
        }
        return builder.toString();
    }

    public static String UTF8Attempt(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            return string;//Apparently hates UTF-8. We'll see how that goes.
        }
    }
}
