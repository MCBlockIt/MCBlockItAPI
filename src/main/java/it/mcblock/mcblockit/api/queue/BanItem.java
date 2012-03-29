package it.mcblock.mcblockit.api.queue;

/**
 * An individual ban item to be sent to the API after successful banning on the server.
 * No getter methods as this is handled entirely by Gson
 * 
 * @author Matt Baxter
 * 
 */
@SuppressWarnings("unused")
public class BanItem extends QueueItem {
    private final String name;
    private final String reason;
    private final String admin;

    public BanItem(String name, String admin, String reason) {
        this.name = name;
        this.admin = admin;
        this.reason = reason;
    }

}
