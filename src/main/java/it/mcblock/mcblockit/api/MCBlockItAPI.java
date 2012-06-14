package it.mcblock.mcblockit.api;

import it.mcblock.mcblockit.api.queue.*;
import it.mcblock.mcblockit.api.queue.Queue;
import it.mcblock.mcblockit.api.queue.bancheck.BanCheckReply;
import it.mcblock.mcblockit.api.userdata.UserData;
import it.mcblock.mcblockit.api.userdata.UserDataCache;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * The MCBlockIt server API
 * Extend this for your implementation
 * 
 * @author Matt Baxter
 * 
 *         Copyright 2012 Matt Baxter
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 * 
 */
public abstract class MCBlockItAPI implements Runnable {
    /**
     * Use this value when kicking a user off the server for being locally banned.
     */
    public static final String KICK_REASON_BANNED = "      " + Utils.COLOR_CHAR + "cBanned. " + Utils.COLOR_CHAR + "fAppeal at http://banned.mcblock.it";
    /**
     * Use this value when kicking a user off the server for being blocked for other reasons.
     */
    public static final String KICK_REASON_BLOCKED = "      " + Utils.COLOR_CHAR + "cBlocked by MCBlockIt. " + Utils.COLOR_CHAR + "fMore info at http://roadblock.mcblock.it";
    /**
     * Use this value when kicking a user off the server for being temporarily banned.
     */
    public static final String KICK_REASON_TEMP_BANNED = "      " + Utils.COLOR_CHAR + "cTemporarily Banned. " + Utils.COLOR_CHAR + "fExpires ";

    private static MCBlockItAPI instance;
    private static Object playerSync = new Object();
    private static Thread thread;

    /**
     * Ban a user
     * 
     * @param name
     *            Name to be banned
     * @param admin
     *            Name of admin banning the user
     * @param type
     *            Type of ban
     * @param reason
     *            Reason for the ban
     */
    public static void ban(String name, String admin, BanType type, String reason) {
        if (isBanned(name) || isTempBanned(name) != null) {
            MCBlockItAPI.instance().messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f " + name + " has already been banned!");
            MCBlockItAPI.instance().log(Level.INFO, "[MCBlockIt] " + admin + " attempted to ban " + name + ", but they are already banned.");
            return;
        }
        MCBlockItAPI.instance().queue.add(new BanItem(name, admin, type.id(), reason));
        MCBlockItAPI.instance();
        final MCBIPlayer player = MCBlockItAPI.getPlayer(name);
        if (player != null) {
            player.kick(MCBlockItAPI.KICK_REASON_BANNED);
        }
        MCBlockItAPI.instance().banList.addBan(name);
        MCBlockItAPI.instance().messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f " + name + " has been banned [" + reason + " (" + type.toString() + ")]");
        MCBlockItAPI.instance().log(Level.INFO, "[MCBlockIt] " + admin + " has banned " + name + " for " + reason + " (" + type.toString() + ")");
    }

    /**
     * Ban a user with default reason
     *
     * @param name
     *            Name to be banned
     * @param admin
     *            Name of admin banning the user
     * @param type
     *            Type of ban
     */
    public static void ban(String name, String admin, BanType type) {
        ban(name, admin, type, MCBlockItAPI.instance().getConfig().getDefaultBanReason());
    }

    /**
     * Temporarily ban a user
     *
     * @param name
     *            Name to be banned
     * @param admin
     *            Name of admin banning the user
     * @param time
     *            Length of ban
     *
     * @return true if successful, false if failed
     */
    public static boolean tempBan(String name, String admin, String time) {
        if (isBanned(name) || isTempBanned(name) != null) {
            MCBlockItAPI.instance().messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f " + name + " has already been banned!");
            MCBlockItAPI.instance().log(Level.INFO, "[MCBlockIt] " + admin + " attempted to temporarily ban " + name + ", but they are already banned.");
            return true;
        }
        long timestamp = (new Date()).getTime() / 1000;
        long calcTime = 0;
        long goodTime = 0;
        String timePhrase = "";
        try {
            if (time.contains("d")) {
                goodTime = Long.valueOf(time.split("d")[0]);
                calcTime = goodTime * 86400;
                timePhrase = "day";
            } else if (time.contains("h")) {
                goodTime = Long.valueOf(time.split("h")[0]);
                calcTime = goodTime * 3600;
                timePhrase = "hour";
            } else if (time.contains("m")) {
                goodTime = Long.valueOf(time.split("m")[0]);
                calcTime = goodTime * 60;
                timePhrase = "minute";
            } else if (time.contains("s")) {
                goodTime = Long.valueOf(time.split("s")[0]);
                calcTime = goodTime;
                timePhrase = "second";
            } else return false;
        } catch (NumberFormatException ex) {
            // Someone was naughty and didn't listen to the formatting :<
            return false;
        }
        MCBlockItAPI.instance();
        final MCBIPlayer player = MCBlockItAPI.getPlayer(name);
        if (player != null) {
            player.kick(MCBlockItAPI.KICK_REASON_TEMP_BANNED + "in " + goodTime + " " + (goodTime != 1 ? timePhrase + "s" : timePhrase));
        }
        MCBlockItAPI.instance().banList.addTempBan(name, timestamp + calcTime);
        MCBlockItAPI.instance().messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f " + name + " has been temporarily banned [" + calcTime + " " + (calcTime != 1 ? timePhrase + "s" : timePhrase) + " (" + admin + ")]");
        MCBlockItAPI.instance().log(Level.INFO, "[MCBlockIt] " + admin + " has temporarily banned " + name + " for " + calcTime + " " + (calcTime != 1 ? timePhrase + "s" : timePhrase));
        return true;
    }

    /**
     * Check if the API is chugging along
     * 
     * @return true if running
     */
    public static boolean enabled() {
        return (MCBlockItAPI.instance != null) && (MCBlockItAPI.thread != null) && MCBlockItAPI.thread.isAlive();
    }

    /**
     * Retrieve the API key
     * 
     * @return the API key in use
     */
    public static String getAPIKey() {
        return MCBlockItAPI.instance().APIKey;
    }

    /**
     * Retrieve a user's data. Fresh only.
     * 
     * @param username
     *            Username to search.
     * @return A user's UserData
     */
    public static UserData getFreshUserData(String username) {
        return MCBlockItAPI.instance().getFreshUserDataInstance(username);
    }

    /**
     * Acquire an online player
     * 
     * @param name
     *            Username of the target
     * @return the player if online, null if not.
     */
    public static MCBIPlayer getPlayer(String name) {
        synchronized (MCBlockItAPI.playerSync) {
            return MCBlockItAPI.instance().players.get(name.toLowerCase());
        }
    }

    /**
     * Acquire online players
     * 
     * @return the list of players
     */
    public static HashMap<String, MCBIPlayer> getPlayers() {
        return new HashMap<String, MCBIPlayer>(MCBlockItAPI.instance().players);
    }

    /**
     * Retrieve a user's data. Cached if possible.
     * 
     * @param username
     *            Username to search.
     * @return A user's UserData.
     */
    public static UserData getUserData(String username) {
        return MCBlockItAPI.instance().getUserDataInstance(username);
    }

    /**
     * Import bans in String array
     * 
     * @param names
     */
    public static void importBans(List<String> names) {
        MCBlockItAPI.instance().queue.add(new ImportItem(names));
    }

    /**
     * Start a created instance
     * 
     * @param api
     */
    private static void initialize(MCBlockItAPI api) {
        MCBlockItAPI.instance = api;
        MCBlockItAPI.thread = new Thread(api);
        MCBlockItAPI.thread.start();
    }

    /**
     * Check if a username is banned
     * 
     * @param username
     *            Username to search
     * @return true if the user is banned
     */
    public static boolean isBanned(String username) {
        return MCBlockItAPI.instance().banList.isBanned(username);
    }

    /**
     * Check if a username is banned
     *
     * @param username
     *            Username to search
     * @return true if the user is banned
     */
    public static String isTempBanned(String username) {
        return MCBlockItAPI.instance().banList.isTempBanned(username);
    }

    /**
     * Send to log
     * 
     * @param level
     * @param message
     */
    public static void logAdd(Level level, String message) {
        MCBlockItAPI.instance().log(level, message);
    }

    /**
     * Send to log
     * 
     * @param level
     * @param message
     * @param thrown
     */
    public static void logAdd(Level level, String message, Throwable thrown) {
        MCBlockItAPI.instance().log(level, message, thrown);
    }

    /**
     * Send to log
     * 
     * @param message
     */
    public static void logAdd(String message) {
        MCBlockItAPI.instance().log(message);
    }

    /**
     * @param player
     * @return true if should be allowed to join
     */
    public static boolean playerJoin(MCBIPlayer player) {
        final boolean allowed = MCBlockItAPI.instance().joinCheck(player);
        if (!allowed) {
            return false;
        }
        synchronized (MCBlockItAPI.playerSync) {
            MCBlockItAPI.instance().players.put(player.getName().toLowerCase(), player);
        }
        UserData userData = MCBlockItAPI.getUserData(player.getName());
        player.sendMessage(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f You have " + userData.getBans().length + " global bans and " + userData.getFlags().length);
        if (MCBlockItAPI.instance().getConfig().isLoginNotificationEnabled() && (userData.getBans().length > 0 || userData.getFlags().length > 0)) {
            MCBlockItAPI.instance().messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f " + player.getName() + " has " + userData.getBans().length + " global bans and " + userData.getFlags().length);
        }
        return true;
    }

    public static void playerQuit(MCBIPlayer player) {
        synchronized (MCBlockItAPI.playerSync) {
            MCBlockItAPI.instance().players.remove(player.getName().toLowerCase());
        }
    }

    /**
     * Stop!
     */
    public static void stop() {
        if ((MCBlockItAPI.instance != null) && (MCBlockItAPI.thread != null)) {
            MCBlockItAPI.thread.interrupt();
        }
    }

    public static void unban(String name) {
        MCBlockItAPI.instance().queue.add(new UnbanItem(name));
        if (MCBlockItAPI.instance().banList.isBanned(name) || MCBlockItAPI.instance().banList.isTempBanned(name) != null) {
            MCBlockItAPI.instance().banList.delBan(name);
            MCBlockItAPI.instance().messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f " + name + " has been unbanned.");
            MCBlockItAPI.instance().log(Level.INFO, "[MCBlockIt] " + name + " has been unbanned.");
        } else {
            MCBlockItAPI.instance().messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f " + name + " is not banned.");
            MCBlockItAPI.instance().log(Level.INFO, "[MCBlockIt] " + name + " is not banned.");
        }
    }

    private static MCBlockItAPI instance() {
        if (MCBlockItAPI.instance == null) {
            throw new MCBlockItNotRunningException();
        }
        return MCBlockItAPI.instance;
    }

    private final HashMap<String, MCBIPlayer> players;
    private final HashMap<String, String> userIPlist;

    private final BanList banList;

    private final Queue queue;
    private long queueStallUntil = 0;

    private final String APIKey;
    private final String APIPost;

    private final String URL = "http://api.mcblock.it/";
    private final String banURL = this.URL + "ban";
    private final String banCheckURL = this.URL + "bancheck";
    private final String unbanURL = this.URL + "unban";
    private final String importURL = this.URL + "import";
    private final String userdataURL = this.URL + "userdata/";
    private final String userInfoURL = this.URL + "submitinfo";

    private final File revisionInfo;
    private String currentRevisionTimestamp = "0";
    private Long lastBanCheck = 0L;
    private Long lastInfoSubmit = 0L;

    private final Gson gsonCompact;
    private final UserDataCache cache;

    public MCBlockItAPI(String APIKey, File dataFolder) {
        this.queueStallUntil = Long.MAX_VALUE;
        MCBlockItAPI.initialize(this);
        this.APIKey = APIKey;
        this.APIPost = "API=" + APIKey;
        this.players = new HashMap<String, MCBIPlayer>();
        this.userIPlist = new HashMap<String, String>();
        this.banList = new BanList(dataFolder);
        this.queue = new Queue(dataFolder);
        this.cache = new UserDataCache(dataFolder);
        this.revisionInfo = new File(dataFolder, "revisionData");
        this.gsonCompact = new Gson();

        try {
            final FileReader read = new FileReader(this.revisionInfo);
            this.currentRevisionTimestamp = read.toString();
        } catch (final IOException e) {
            if (this.revisionInfo.exists()) {
                this.log(Level.WARNING, "[MCBlockIt] " + this.revisionInfo.toString() + " - Cannot read from revision storage file! Maybe a file permission error?");
            }
        }
        this.queueStallUntil = 0;
    }

    /**
     * Get the configuration
     * 
     * @return the configuration
     */
    public abstract MCBIConfig getConfig();
    public abstract String getVersion();

    @Override
    public void run() {
        try {
            QueueItem item;
            while (true) {
                final long time = (new Date()).getTime();
                if (time > this.queueStallUntil) {
                    if ((time - this.lastInfoSubmit) > 60000 && this.getConfig().isUserIPRecordingEnabled()) {
                        if (this.userIPlist.size() > 0) {
                            this.queue.add(new UserIPItem(this.userIPlist));
                            this.userIPlist.clear();
                        }
                        this.lastInfoSubmit = time;
                    }
                    if ((time - this.lastBanCheck) > 1200000) {
                        item = new BanCheck(this.currentRevisionTimestamp);//lol it doesn't even need to be added
                        this.lastBanCheck = time;
                    } else {
                        item = this.queue.peek();
                    }
                    if (item != null) {
                        if (this.process(item)) {
                            this.queue.remove(item);
                        }
                    }
                }
                Thread.sleep(10000);
            }
        } catch (final InterruptedException e) {
            MCBlockItAPI.instance = null;
        }
    }

    private UserData getFreshUserDataInstance(String username) {
        final StringBuilder response = new StringBuilder();
        try {
            final URL urlTarget = new URL(this.userdataURL + username);
            final URLConnection connection = urlTarget.openConnection();
            connection.setDoOutput(true);
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(9000);
            connection.setRequestProperty("User-agent", "MCBlockIt");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } catch (final Exception e) {
            this.log(Level.WARNING, "[MCBlockIt] Unexpected failure to call API", e);
            return null;
        }
        UserData data = null;
        try {
            data = this.gsonCompact.fromJson(response.toString(), UserData.class);
        } catch (final JsonSyntaxException e) {
            //Boggle
        }
        return data;
    }

    private UserData getUserDataInstance(String username) {
        final long lastMod = this.cache.lastMod(username);
        if ((lastMod != 0) && ((lastMod + (this.getConfig().getCacheTimeout() * 60000)) > (new Date()).getTime())) {
            final UserData data = this.cache.getUserData(username);
            if (data != null) {
                return data;
            }
        } else if (lastMod != 0) {
            this.cache.delUserCache(username);
        }
        final UserData data = this.getFreshUserDataInstance(username);
        this.cache.addUserData(data);
        return data;
    }

    private boolean joinCheck(MCBIPlayer player) {
        final UserData data = this.getUserDataInstance(player.getName());
        if (data == null) {
            return true;//If system derp, let on any player not locally banned.
        }
        final MCBIConfig config = this.getConfig();
        if (config.isBanRestrictionEnabled() && (data.getBans().length >= config.getBanRestriction())) {
            return false;
        }
        if (config.isFlagRestrictionEnabled()) {
            for (final String flag : data.getFlags()) {
                if (config.getFlagRestriction().contains(flag)) {
                    return false;
                }
            }
        }
        if (config.isReputationRestrictionEnabled() && (data.getReputation() <= config.getReputationRestriction())) {
            return false;
        }
        if (!this.userIPlist.containsKey(player.getName()) && config.isUserIPRecordingEnabled()) {
            this.userIPlist.put(player.getName(), player.getIP());
        }
        return true;
    }

    private void messageAdmins(String message) {
        synchronized (MCBlockItAPI.playerSync) {
            for (final MCBIPlayer player : MCBlockItAPI.getPlayers().values()) {
                player.messageIfAdmin(message);
            }
            this.log(Level.INFO, "[MCBlockIt] " + message);
        }
    }

    private boolean process(QueueItem item) {
        String url = null;
        if (item instanceof BanItem) {
            url = this.banURL;
        } else if (item instanceof UnbanItem) {
            url = this.unbanURL;
        } else if (item instanceof BanCheck) {
            url = this.banCheckURL;
        } else if (item instanceof ImportItem) {
            url = this.importURL;
        } else if (item instanceof UserIPItem) {
            url = this.userInfoURL;
        } else {
            return true;//Dump whatever this is.
        }
        //Time to send to the API!
        if (item instanceof BanCheck) {
            this.processBanCheck(this.sendToAPI(url, this.APIPost + "&data=" + this.gsonCompact.toJson(item)));
            return true;
        }
        return this.processResponse(this.sendToAPI(url, this.APIPost + "&data=" + this.gsonCompact.toJson(item)));
    }

    private void processBanCheck(String response) {
        try {
            final BanCheckReply reply = this.gsonCompact.fromJson(response, BanCheckReply.class);
            if (reply.unbans != null) {
                for (final String name : reply.unbans) {
                    this.banList.delBan(name);
                }
            }
            if (reply.bans != null) {
                for (final String name : reply.bans) {
                    this.banList.addBan(name);
                }
            }
            this.currentRevisionTimestamp = reply.timestamp;
            final FileWriter write = new FileWriter(this.revisionInfo);
            final PrintWriter out = new PrintWriter(write);
            out.print(reply.timestamp);
            out.close();
        } catch (final JsonSyntaxException e) {
            this.processResponse(response);//Error code?
        } catch (final IOException e) {
            this.log(Level.WARNING, "[MCBlockIt] " + this.revisionInfo.toString() + " - Cannot write to revision storage file! Maybe a file permission error?");
        }
    }

    private boolean processResponse(String response) {
        if ((response != null) && (response.length() > 11)) {
            try {
                final APIReply reply = this.queue.gson.fromJson(response.toString(), APIReply.class);

                if (reply.success()) {
                    return true;
                }
                final long timeNow = (new Date()).getTime();
                if (reply.getStatus() == 429) {//Rate limiting
                    this.queueStallUntil = timeNow + 60000;//Minute delay
                    return false;
                }
                if (reply.getStatus() == 503) {
                    this.queueStallUntil = timeNow + 1800000;//30 minute delay
                    this.messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f Maintenance!");
                    this.messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f Bans will not update on site for at least 30 mins.");
                    this.log("[MCBlockIt] Delaying queue by 30 minutes for maintenance");
                    return false;
                }
                this.log("[MCBlockIt] Received API reply ID " + reply.getStatus() + ": " + reply.getError());
                if (reply.getStatus() == 403) {//You cannot do this
                    return true;
                } else if (reply.getStatus() == 400) {//Invalid syntax
                    this.messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f Error! Ask MCBlockIt staff for help.");
                    return true;//I guess?
                } else if (reply.getStatus() == 401) {//Invalid key
                    this.messageAdmins(Utils.COLOR_CHAR + "c[MCBlockIt]" + Utils.COLOR_CHAR + "f Shutting down. Invalid API Key.");
                    this.shutdown();
                    MCBlockItAPI.stop();
                    /*} else if (reply.getStatus() == 418){
                      TODO: I'm a Teapot
                     */
                }
            } catch (final JsonSyntaxException e) {
            }
            return false;
        }
        return false;
    }

    private String sendToAPI(String url, String POST) {
        final StringBuilder response = new StringBuilder();
        try {
            final URL urlTarget = new URL(url);
            final URLConnection connection = urlTarget.openConnection();
            connection.setDoOutput(true);
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(9000);
            connection.setRequestProperty("User-agent", "MCBlockIt-" + this.getVersion());
            final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(POST);
            writer.flush();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            writer.close();
            reader.close();
        } catch (final Exception e) {
            this.log(Level.WARNING, "[MCBlockIt] Unexpected failure to call API", e);
            return null;
        }
        return response.toString();
    }

    protected abstract void log(Level level, String message);

    protected abstract void log(Level level, String message, Throwable thrown);

    protected abstract void log(String message);

    protected abstract void shutdown();

}
