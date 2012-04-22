package it.mcblock.mcblockit.api;

import java.io.*;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class BanList {

    private class save extends TimerTask {
        @Override
        public void run() {
            BanList.this.scheduledSave = false;
            try {
                final BufferedWriter output = new BufferedWriter(new FileWriter(BanList.this.banList));
                synchronized (BanList.this.sync) {
                    for (final String ban : BanList.this.bans) {
                        output.write(ban + "\n");
                    }
                }
                output.close();
            } catch (final Exception e) {
                System.out.println("[MCBlockIt] Error while saving bans list");
                e.printStackTrace();
            }
        }
    }

    private final File banList;
    private final HashSet<String> bans;
    private final Object sync = new Object();

    private boolean scheduledSave = false;

    public BanList(File dataFolder) {
        this.banList = new File(dataFolder, "playerbans.txt");
        this.bans = new HashSet<String>();
        try {
            if (this.banList.exists()) {
                final BufferedReader input = new BufferedReader(new FileReader(this.banList));
                String line;
                while ((line = input.readLine()) != null) {
                    if (line.length() > 0) {
                        this.bans.add(line.toLowerCase());
                    }
                }
                System.out.println("[MCBlockIt] Loaded " + this.bans.size() + " bans");
            } else {
                this.banList.createNewFile();
            }
        } catch (final Exception e) {
            System.out.println("[MCBlockIt] Error loading bans list");
            e.printStackTrace();
        }
    }

    public void addBan(String name) {
        synchronized (this.sync) {
            this.bans.add(name.toLowerCase());
        }
        this.save();
    }

    public void delBan(String name) {
        synchronized (this.sync) {
            this.bans.remove(name.toLowerCase());
        }
        this.save();
    }

    public boolean isBanned(String username) {
        synchronized (this.sync) {
            return this.bans.contains(username.toLowerCase());
        }
    }

    private void save() {
        if (this.scheduledSave) {
            return;
        }
        this.scheduledSave = true;
        new Timer().schedule(new save(), 1000);
    }

}
