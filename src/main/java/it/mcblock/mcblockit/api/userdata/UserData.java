package it.mcblock.mcblockit.api.userdata;

/**
 * A user's MCBlockIt data
 * 
 * @author Matt Baxter
 * 
 */
public class UserData {
    /**
     * A single ban on a user
     * 
     * @author Matt Baxter
     * 
     */
    public class BanData {
        private String server;
        private long timestamp;
        private String admin;
        private String reason;

        /**
         * Get the banning admin
         * 
         * @return username of banning admin
         */
        public String getAdmin() {
            return this.admin;
        }

        /**
         * Get the reason for the ban
         * 
         * @return ban reason
         */
        public String getReason() {
            return this.reason;
        }

        /**
         * Get the server of the ban
         * 
         * @return banning server name
         */
        public String getServer() {
            return this.server;
        }

        /**
         * Get the time of the ban
         * 
         * @return the ban timestamp
         */
        public long getTimestamp() {
            return this.timestamp;
        }
    }

    private String username;
    private double reputation;
    private String[] flags;

    private BanData[] bans;

    /**
     * Get the user's bans
     * 
     * @return the user's bans
     */
    public BanData[] getBans() {
        return this.bans;
    }

    /**
     * Get the user's flags
     * 
     * @return the user's flags
     */
    public String[] getFlags() {
        return this.flags;
    }

    /**
     * Get the user's calculated reputation
     * 
     * @return the user's calculated reputation
     */
    public double getReputation() {
        return this.reputation;
    }

    /**
     * Get the user's username
     * 
     * @return the username of the user
     */
    public String getUsername() {
        return this.username;
    }
}
