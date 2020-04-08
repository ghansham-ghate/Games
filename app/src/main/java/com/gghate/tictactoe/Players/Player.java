package com.gghate.tictactoe.Players;

public class Player {
    String name;
    int type;
    String status;
    String path;
    public Player(String name, int type, String status) {
        this.name = name;
        this.type = type;
        this.status = status;
    }
    public Player()
    { }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
