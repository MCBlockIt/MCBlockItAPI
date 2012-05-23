package it.mcblock.mcblockit.api;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

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
                    for (final Map.Entry<String, Long> tempBan : BanList.this.tempBans.entrySet()) {
                        output.write(tempBan.getKey() + ":" + tempBan.getValue() + "\n");
                    }
                }
                output.close();
            } catch (final Exception e) {
                MCBlockItAPI.logAdd(Level.WARNING, "[MCBlockIt] Error while saving bans list");
                e.printStackTrace();
            }
        }
    }

    private final File banList;
    private final HashSet<String> bans;
    private final HashMap<String, Long> tempBans;
    private final Object sync = new Object();

    private boolean scheduledSave = false;

    public BanList(File dataFolder) {
        this.banList = new File(dataFolder, "playerbans.txt");
        this.bans = new HashSet<String>();
        this.tempBans = new HashMap<String, Long>();
        long timestamp = (new Date()).getTime() / 1000;
        try {
            if (this.banList.exists()) {
                final BufferedReader input = new BufferedReader(new FileReader(this.banList));
                String line;
                while ((line = input.readLine()) != null) {
                    if (line.length() > 0) {
                        if (line.contains(":")) {
                            String[] tempBan = line.split(":");
                            if (timestamp < Long.valueOf(tempBan[1])) {
                                this.tempBans.put(tempBan[0], Long.valueOf(tempBan[1]));
                            }
                        } else {
                            this.bans.add(line.toLowerCase());
                        }
                    }
                }
                Integer count = this.bans.size() + this.tempBans.size();
                MCBlockItAPI.logAdd("[MCBlockIt] Loaded " + count + " bans");
                this.save();
            } else {
                this.banList.createNewFile();
            }
        } catch (final Exception e) {
            MCBlockItAPI.logAdd(Level.WARNING, "[MCBlockIt] Error loading bans list");
            e.printStackTrace();
        }
    }

    public void addBan(String name) {
        synchronized (this.sync) {
            this.bans.add(name.toLowerCase());
        }
        this.save();
    }

    public void addTempBan(String name, Long timestamp) {
        synchronized (this.sync) {
            this.tempBans.put(name.toLowerCase(), timestamp);
        }
        this.save();
    }

    public void delBan(String name) {
        synchronized (this.sync) {
            this.bans.remove(name.toLowerCase());
            this.tempBans.remove(name.toLowerCase());
        }
        this.save();
    }

    public String hasBanExpired(String username) {
        if (!this.tempBans.containsKey(username.toLowerCase())) return null;

        long timestamp = this.tempBans.get(username.toLowerCase()) - ((new Date()).getTime() / 1000);

        if (timestamp < 0) {
            this.tempBans.remove(username.toLowerCase());
            return null;
        }

        java.util.Date d = new java.util.Date((timestamp * 1000) + "L");

        return d.toString();
    }

    public boolean isBanned(String username) {
        synchronized (this.sync) {
            return this.bans.contains(username.toLowerCase());
        }
    }

    public String isTempBanned(String username) {
        synchronized (this.sync) {
            return this.hasBanExpired(username.toLowerCase());
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
