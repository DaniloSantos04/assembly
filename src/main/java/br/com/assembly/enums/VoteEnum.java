package br.com.assembly.enums;

public enum VoteEnum {
    SIM(true),
    NAO(false);

    private boolean valorBoolean;

    VoteEnum(boolean valorBoolean) {
        this.valorBoolean = valorBoolean;
    }

    public boolean toBoolean() {
        return valorBoolean;
    }

    public static VoteEnum fromBoolean(boolean valorBoolean) {
        return valorBoolean ? SIM : NAO;
    }
}
