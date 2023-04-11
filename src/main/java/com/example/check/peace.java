package com.example.check;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class peace {
    private final int PeaceType;//0 = red 1 = green tips
    private int x_cord, y_cord; // kauliņa koordinātes

    private boolean Isking; // kauliņš ir vai nav karaliene

    public int getPeaceType() {
        return PeaceType;
    }
    private Circle peace; // katrs kauliņš ir parasts riņķis ar 20 px rādiusu

    public peace(int peaceType, int x_cord, int y_cord) {
        this.PeaceType = peaceType;
        this.x_cord = x_cord;
        this.y_cord = y_cord;
        String color = (peaceType == 0) ? "red" :  "green";
        peace = new Circle();
        peace.setRadius(20);
        peace.setFill(Paint.valueOf(color));
        GridPane.setHalignment(peace, HPos.CENTER);
        GridPane.setValignment(peace, VPos.CENTER);
    }
    public peace(peace piece){
        this.PeaceType = piece.PeaceType;
        this.x_cord = piece.x_cord;
        this.y_cord = piece.y_cord;
        this.Isking = piece.getIsking();
        String color = (PeaceType == 0) ? "red" :  "green";
        peace = new Circle();
        peace.setRadius(20);
        peace.setFill(Paint.valueOf(color));
        GridPane.setHalignment(peace, HPos.CENTER);
        GridPane.setValignment(peace, VPos.CENTER);
    }
    // kustība
    public void Move(int newX,int newY){
        GridPane.setColumnIndex(this.getPeace(), newX);
        GridPane.setRowIndex(this.getPeace(), newY);
        this.setX_cord(newX);
        this.setY_cord(newY);
    }
    public boolean isking() {
        return Isking;
    }
    public void setIsking(boolean isking) {
        Isking = isking;
    }
    public boolean getIsking() {
        return Isking;
    }
    public Circle getPeace() { return peace; }
    public int getX_cord() {
        return x_cord;
    }
    public void setX_cord(int x_cord) {
        this.x_cord = x_cord;
    }
    public int getY_cord() {
        return y_cord;
    }
    public void setY_cord(int y_cord) {
        this.y_cord = y_cord;
    }
}
