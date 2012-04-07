package it.mcblock.mcblockit.api;

/**
 * A player on the server
 * 
 * @author Matt
 * 
 */
public interface MCBIPlayer {
    /**
     * Get the player's IP
     * 
     * @return the IP address of the player eg. 192.0.0.1
     */
    public String getIP();

    /**
     * Get the player's username
     * 
     * @return the username of the player
     */
    public String getName();

    /**
     * Kick the player off the server
     * 
     * @param reason
     *            Message to display to the player
     */
    public void kick(String reason);

    /**
     * Message the user if they are an admin
     * Done this way to allow for sync'd permission checking
     * 
     * @param message
     *            The messages to send
     */
    public void messageIfAdmin(String message);
}
