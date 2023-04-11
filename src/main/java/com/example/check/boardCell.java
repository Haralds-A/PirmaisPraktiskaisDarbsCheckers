package com.example.check;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class boardCell {
    private double side;
    private Color defColor;
    private Rectangle cell;
    private peace peace;
    public boardCell(double side, Color defColor, peace peace) {
        this.side = side;
        this.defColor = defColor;
        this.peace = peace;
        cell = new Rectangle(side,side,side,side);
        cell.setFill(getDefColor());
    }
    public boardCell(boardCell boardCell){
        this.side = boardCell.side;
        this.defColor = boardCell.defColor;
        if(boardCell.getPeace() != null){
            this.peace = new peace(boardCell.peace);
        }
        cell = new Rectangle(boardCell.side,boardCell.side,boardCell.side,boardCell.side);
        cell.setFill(getDefColor());
    }
    public peace getPeace() {
        return peace;
    }
    public void setPeace(peace peace) {
        this.peace = peace;
    } // piešķir šūnai kauliņu

    public void setDefColor(Color defColor) { // piešķir šūnai attiecīgo krāsu
        this.defColor = defColor;
        getCell().setFill(getDefColor());
    }
    public Color getDefColor() {
        return defColor;
    }
    public Rectangle getCell() {
        return cell;
    }
}
