package it.mcblock.mcblockit.api.queue;

import java.io.*;
import java.util.PriorityQueue;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * API sending queue
 * 
 * @author Matt Baxter
 * 
 */
public class Queue extends PriorityQueue<QueueItem> {
    private static final long serialVersionUID = 1L;
    private final File banQueue;
    private final File unbanQueue;
    public final Gson gson;

    public Queue(File dataFolder) {
        this.gson = new Gson();
        final File queueFolder = new File(dataFolder, "queue");
        if (!queueFolder.exists()) {
            queueFolder.mkdirs();
        }
        this.banQueue = new File(queueFolder, "bans");
        this.load(this.banQueue, BanItem.class);
        this.unbanQueue = new File(queueFolder, "unbans");
        this.load(this.unbanQueue, UnbanItem.class);
    }

    @Override
    public boolean add(QueueItem item) {
        if (item == null) {
            return false;
        }
        boolean success;
        synchronized (this) {
            success = super.add(item);
        }
        this.update();
        return success;
    }

    @Override
    public boolean remove(Object item) {
        boolean success;
        synchronized (this) {
            success = super.remove(item);
        }
        this.update();
        return success;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void load(File file, Class clazz) {
        try {
            if (file.exists()) {
                final BufferedReader input = new BufferedReader(new FileReader(file));
                String line;
                while ((line = input.readLine()) != null) {
                    if (line.length() < 10) {
                        continue;
                    }
                    try{
                        super.add((QueueItem) this.gson.fromJson(line, clazz));
                    } catch (JsonSyntaxException e){
                        //Discard bad lines
                    }
                }
                input.close();
            }
        } catch (final IOException e) {
            System.out.println("Failed to read " + file);
            e.printStackTrace();
        }
    }

    private void update() {
        try {
            final BufferedWriter outputBans = new BufferedWriter(new FileWriter(this.banQueue));
            final BufferedWriter outputUnbans = new BufferedWriter(new FileWriter(this.unbanQueue));

            synchronized (this) {
                for (final QueueItem item : this) {
                    if (item instanceof BanItem) {
                        outputBans.write(this.gson.toJson(item) + "\n");
                    } else if (item instanceof UnbanItem) {
                        outputUnbans.write(this.gson.toJson(item) + "\n");
                    }
                }
            }
            outputBans.close();
            outputUnbans.close();
        } catch (final IOException e) {
            System.out.println("Failed to write");
            e.printStackTrace();
        }
    }
}
