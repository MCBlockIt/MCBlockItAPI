package it.mcblock.mcblockit.api.queue;

public class ImportItem extends QueueItem {
    private final String[] users;

    public ImportItem (String[] users) {
        this.users = users;
    }
}
