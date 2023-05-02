package io.github.kuroka3.playermanager.Class;

public class Users {
    private String nickname;
    private boolean ban;
    private String banreason;
    private int warns;

    public String getNickname() {
        return nickname;
    }

    public boolean isBan() {
        return ban;
    }

    public String getBanreason() {
        return banreason;
    }

    public int getWarns() {
        return warns;
    }
}
