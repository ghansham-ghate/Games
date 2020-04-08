package com.gghate.tictactoe.Message;

public class Message {
    String gameID;
    String requestorPlayer;
    String acceptorPlayer;

    public String getGameID() {
        return gameID;
    }

    public String getRequestorPlayer() {
        return requestorPlayer;
    }

    public String getAcceptorPlayer() {
        return acceptorPlayer;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setRequestorPlayer(String requestorPlayer) {
        this.requestorPlayer = requestorPlayer;
    }

    public void setAcceptorPlayer(String acceptorPlayer) {
        this.acceptorPlayer = acceptorPlayer;
    }
}
