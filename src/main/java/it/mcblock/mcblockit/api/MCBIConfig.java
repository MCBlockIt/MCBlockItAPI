package it.mcblock.mcblockit.api;

import java.util.List;

/**
 * Config for MCBlockIt
 * 
 * @author Matt Baxter
 * 
 */
public interface MCBIConfig {
    /**
     * Get the ban count restriction.
     * If enabled, users with this many bans or higher will be blocked.
     * 
     * @return the ban count restriction
     */
    public int getBanRestriction();

    /**
     * Get the flags blocked from the server.
     * If enabled, users with any of these flags will be blocked.
     * 
     * @return the restricted flags
     */
    public List<String> getFlagRestriction();

    /**
     * Get the reputation restriction
     * If enabled, users with this MCBlockIt-calculated rep or lower will be blocked
     * 
     * @return the reputation restriction
     */
    public double getReputationRestriction();

    /**
     * Check if ban count restrictions are enabled
     * 
     * @return true if ban count restrictions are enabled
     */
    public boolean isBanRestrictionEnabled();

    /**
     * Check if flag restrictions are enabled
     * 
     * @return true if flag restrictions are enabled
     */
    public boolean isFlagRestrictionEnabled();

    /**
     * Check if reputation restrictions are enabled
     * 
     * @return true if reputation restrictions are enabled
     */
    public boolean isReputationRestrictionEnabled();
}
