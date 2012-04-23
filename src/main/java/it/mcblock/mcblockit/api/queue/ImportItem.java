package it.mcblock.mcblockit.api.queue;

public class ImportItem extends QueueItem {
    private final ArrayList<String> users;

    public ImportItem (ArrayList<String> users) {
        this.users = users;
    }
}
