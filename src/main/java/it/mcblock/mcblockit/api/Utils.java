package it.mcblock.mcblockit.api;

/**
 * Utilities class
 * 
 * @author Matt Baxter
 * 
 *         Copyright 2012 Matt Baxter
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
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

}
