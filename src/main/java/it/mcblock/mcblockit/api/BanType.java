package it.mcblock.mcblockit.api;

public enum BanType {
    LOCAL(0), GLOBAL(1), EAC(2);
    private final int id;

    private BanType(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }
}
