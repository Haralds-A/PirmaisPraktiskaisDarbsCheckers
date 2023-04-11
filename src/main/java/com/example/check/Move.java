package com.example.check;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private peace piece;
    private int newX, newY;
    public int getNewX() {
        return newX;
    }

    public peace getPiece() {
        return piece;
    }

    public int getNewY() {
        return newY;
    }
    private List<peace> killedpieces = new ArrayList<peace>();

    private boolean iskill = false;

    public boolean Iskill() {
        return iskill;
    }

    public List<peace> getKilledpieces() {
        return killedpieces;
    }

    public void killdPeace(peace piece){
        iskill = true;
        killedpieces.add(piece);
    }
    public Move(peace piece, int newX, int newY) {
        this.piece = piece;
        this.newX = newX;
        this.newY = newY;
    }
}
