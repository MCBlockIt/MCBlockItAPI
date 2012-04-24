package it.mcblock.mcblockit.api.queue;

import java.util.List;

@SuppressWarnings("unused")
public class ImportItem extends QueueItem {
    private final List<String> users;

    public ImportItem(List<String> users) {
        this.users = users;
    }
}
