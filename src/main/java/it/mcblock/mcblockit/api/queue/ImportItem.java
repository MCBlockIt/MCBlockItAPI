package it.mcblock.mcblockit.api.queue;

import java.util.ArrayList;

public class ImportItem extends QueueItem {
    private final ArrayList<String> users;

    public ImportItem (ArrayList<String> users) {
        this.users = users;
    }
}
