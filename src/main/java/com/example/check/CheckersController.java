package com.example.check;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class CheckersController {
    @FXML
    private Button plGreen;
    @FXML
    private GridPane grid;
    @FXML
    private Pane menue,boardPane;

    @FXML
    protected void onGreenClicked() {//Apstrādā pogas "Play as green" klikšķi
        boardPane.setVisible(true);
        menue.setVisible(false);
        Scene scene = plGreen.getScene();
        grid = (GridPane) scene.lookup("#grid");
        // notīra laukumu
        clearBoard(grid);
        // uzīmē laukumu
        Draw(grid,0);
    }
    @FXML
    protected void onRedClicked() { //Apstrādā pogas "Play as red" klikšķi
        boardPane.setVisible(true);
        menue.setVisible(false);
        Scene scene = plGreen.getScene();
        grid = (GridPane) scene.lookup("#grid");
        // notīra laukumu
        clearBoard(grid);
        // uzīmē laukumu
        Draw(grid,1);
    }
    protected void onBackClicked() { //Apstrādā pogas "ok klikšķi"
        boardPane.setVisible(false);
        menue.setVisible(true);
    }
    private void clearBoard(GridPane pane){ // notīra laukumu
        pane.getChildren().clear();
    }

    private void Draw(GridPane pane, int player){ // zīmē laukumu un apstrādā gājienus
        int count = 0;
        double s = 50;
        boardCell[][] board = new boardCell[8][8];
        for (int i = 0; i < 8; i++) {
            count++;
            for (int j = 0; j < 8; j++) {
                boardCell r = new boardCell(s, Color.BLACK,null);
                if (count % 2 != 0){
                    r.setDefColor(Color.WHITE);
                }
                board[i][j] = r;
                pane.add(r.getCell(), j, i);
                count++;
            }
        }
        int c = 0;
        for (int i = 0; i < 8; i++) {
            c++;
            for (int j = 0; j < 8; j++) {
                if (c % 2 == 0 && j<3){
                    peace peace = new peace(0,i,j);
                    pane.add(peace.getPeace(), i, j);
                    board[i][j].setPeace(peace);
                } else if (c % 2 == 0 && j>4) {
                    peace peace = new peace(1,i,j);
                    pane.add(peace.getPeace(), i, j);
                    board[i][j].setPeace(peace);
                }
                c++;
            }
        }
        GameState gamestate = new GameState(board);
        gamestate.setPane(pane);

        // Ja spēli uzsāk algoritms
        Alpha aplha = new Alpha();
        if(player == 0){

            if(gamestate.getTurn() == player){
                GameState copyState = new GameState(gamestate);
                Move bestMove = aplha.findBestMove(copyState,player);
                Move toMake = new Move(gamestate.getBoard()[bestMove.getPiece().getX_cord()][bestMove.getPiece().getY_cord()].getPeace(), bestMove.getNewX(), bestMove.getNewY());
                for (peace p:bestMove.getKilledpieces()) {
                    toMake.killdPeace(gamestate.getBoard()[p.getX_cord()][p.getY_cord()].getPeace());
                }
                gamestate.IsMoveLegal(toMake);
                gamestate.MakeMove(toMake);
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j].getPeace()!=null) {
                    peace peace = board[i][j].getPeace();
                    peace.getPeace().setOnMouseEntered(event -> {
                        peace.getPeace().setCursor(Cursor.OPEN_HAND);
                    });
                    peace.getPeace().setOnMouseReleased(event -> {
                        // Apstrādā gājienu
                        double x = event.getSceneX();
                        double y = event.getSceneY();
                        int newCol = (int) Math.floor((x / 50));
                        int newRow = (int) Math.floor((y / 50));
                        Move move = new Move(peace,newCol,newRow);
                        if(gamestate.getTurn() == peace.getPeaceType()){
                            if(gamestate.IsMoveLegal(move)){ // ja gājiens ir legāls
                                gamestate.MakeMove(move); // tiek izdarīts gājiens
                                gameover(gamestate);
//                                System.out.println(gamestate.getMovecount());
                            }
                        }
                        // algoritms veic gājienu
                        if(gamestate.getTurn() == player){
                            GameState copyState = new GameState(gamestate);
                            Move bestMove = aplha.findBestMove(copyState,player);
                            if(bestMove.getPiece().isking()){
                                gamestate.makeKing(gamestate.getBoard()[bestMove.getPiece().getX_cord()][bestMove.getPiece().getY_cord()].getPeace());
                            }
                            Move toMake = new Move(gamestate.getBoard()[bestMove.getPiece().getX_cord()][bestMove.getPiece().getY_cord()].getPeace(), bestMove.getNewX(), bestMove.getNewY());
                            for (peace p:bestMove.getKilledpieces()) {
                                toMake.killdPeace(gamestate.getBoard()[p.getX_cord()][p.getY_cord()].getPeace());
                            }
                            gamestate.MakeMove(toMake);
                            gameover(gamestate);
                        }
                    });
                }
            }
        }
    }
    private void gameover(GameState gameState){
        if(gameState.isGameOver()){
            Dialog dialog = new Dialog();
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButton.managedProperty().bind(closeButton.visibleProperty());
            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType);
            dialog.setTitle("Game over");
            Label label = new Label();

            dialog.getDialogPane().setContent(label);
            if(gameState.getRedScore() == 12){
                label.setText("Green wins");
            }else if(gameState.getMovecount() > 79){
                label.setText("Draw");
            }else{
                label.setText("Red wins");
            }
            label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            label.setTextAlignment(TextAlignment.CENTER);
            dialog.getDialogPane().setContent(label);
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    onBackClicked();
                }
                return null;
            });
            closeButton.setVisible(false);
            dialog.showAndWait();
        }
    }
}