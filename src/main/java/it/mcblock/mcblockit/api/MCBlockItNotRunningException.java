package it.mcblock.mcblockit.api;

/**
 * Uh oh! It's not running! How did that happen?
 * 
 * @author Matt Baxter
 * 
 */
public class MCBlockItNotRunningException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MCBlockItNotRunningException() {
        super("MCBlockIt is not running!");
    }

}
