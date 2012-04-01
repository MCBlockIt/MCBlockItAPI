package it.mcblock.mcblockit.api.queue;

public class APIReply {
    private int status;
    private String errmsg;

    public String getError() {
        return this.errmsg;
    }

    public int getStatus() {
        return this.status;
    }

    public boolean success() {
        return this.status == 0;
    }
}
