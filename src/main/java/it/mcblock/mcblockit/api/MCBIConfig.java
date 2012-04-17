package it.mcblock.mcblockit.api;

import java.util.List;

/**
 * Config for MCBlockIt
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
public interface MCBIConfig {
    /**
     * Get the ban count restriction.
     * If enabled, users with this many bans or higher will be blocked.
     * 
     * @return the ban count restriction
     */
    public int getBanRestriction();

    /**
     * Get the cache timeout in minutes.
     * Minimum value of 5 accepted by the API
     * 
     * @return the cache timeout value
     */
    public int getCacheTimeout();

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
