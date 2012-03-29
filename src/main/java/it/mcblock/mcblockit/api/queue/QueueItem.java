package it.mcblock.mcblockit.api.queue;

import java.util.Date;

/**
 * An individual item to be sent to the API after successful action on the server.
 * No getter methods as this is handled entirely by Gson.
 * Comparison method provided for sorting in the queue.
 * 
 * @author Matt Baxter
 * 
 */
public abstract class QueueItem implements Comparable<QueueItem> {
    private final long localtimestamp;

    public QueueItem() {
        this.localtimestamp = (new Date()).getTime();
    }

    @Override
    public int compareTo(QueueItem other) {
        return (int) (this.localtimestamp - other.localtimestamp);
    }
}
