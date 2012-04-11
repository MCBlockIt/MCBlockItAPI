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
    private final long timestamp;

    public QueueItem() {
        this.timestamp = (new Date()).getTime()/1000;
    }

    public QueueItem(long time) {
        this.timestamp = 0;
    }

    @Override
    public int compareTo(QueueItem other) {
        return (int) (this.timestamp - other.timestamp);
    }
}
