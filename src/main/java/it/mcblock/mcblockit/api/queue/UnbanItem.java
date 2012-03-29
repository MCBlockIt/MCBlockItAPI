package it.mcblock.mcblockit.api.queue;

/**
 * An individual unban item to be sent to the API after successful unbanning on the server.
 * No getter methods as this is handled entirely by Gson
 * 
 * @author Matt Baxter
 * 
 */
public class UnbanItem extends QueueItem {
    public static UnbanItem loadData(String[] args) {
        if (args.length < 2) {
            return null;
        }
        return new UnbanItem(args[1]);
    }

    @SuppressWarnings("unused")
    private final String name;

    public UnbanItem(String name) {
        this.name = name;
    }

}
