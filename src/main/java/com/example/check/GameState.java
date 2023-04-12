package com.example.check;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {
   private int redScore=0, greenScore = 0, movecount= 0; // spēlētāju rezultāti un kopējais gājienu skaits
    private boardCell[][] board; // 2d masīvs reprezentē laukumu
   private int turn = 0; // kura spēlētāja kārta iet
   private GridPane pane;
    public void setPane(GridPane pane) {
        this.pane = pane;
    }
    private boolean IsKill;
    public GameState(boardCell[][] board) {
        this.board = board;
    }

    public boolean IsMoveLegal(Move move){// šī funkcija pārbauda vai gājiens ir legāls
        peace peace = move.getPiece();
        int newCol = move.getNewX();
        int newRow = move.getNewY();
        if(turn == peace.getPeaceType()){
            if(newCol < 0 || newRow < 0 || newCol > 7 || newRow > 7) return false;
            if (board[newCol][newRow].getPeace() != null) {
                return false;
            }
            int dx = Math.abs(newCol - peace.getX_cord());
            int dy = Math.abs(newRow - peace.getY_cord());
            if (dx != dy) {
                return false;
            }
            if(peace.isking()){
                int jumpedCol = (newCol + peace.getX_cord()) / 2;
                int jumpedRow = (newRow + peace.getY_cord()) / 2;
                if(Math.abs(dx) == 2 && Math.abs(dy) == 2 &&
                        ((board[jumpedCol][jumpedRow].getPeace() == null) ||
                        board[jumpedCol][jumpedRow].getPeace().getPeaceType() == peace.getPeaceType())){
                    return false;
                }
                if (Math.abs(dx) == 2 && Math.abs(dy) == 2 &&
                        (board[jumpedCol][jumpedRow].getPeace() != null) &&
                        board[jumpedCol][jumpedRow].getPeace().getPeaceType() != peace.getPeaceType()) {
                    move.killdPeace(board[jumpedCol][jumpedRow].getPeace());
                    return true;
                }
                if(hasAvailableJumpsForPiece(peace,peace.getX_cord(),peace.getY_cord())){
                    return false;
                }
            }else {
                if (!IsDirection(newRow, peace)) {
                    return false;
                }
                int jumpedCol = (newCol + peace.getX_cord()) / 2;
                int jumpedRow = (newRow + peace.getY_cord()) / 2;
                if (Math.abs(dx) == 2 && Math.abs(dy) == 2 &&
                        (board[jumpedCol][jumpedRow].getPeace() != null) &&
                        board[jumpedCol][jumpedRow].getPeace().getPeaceType() != peace.getPeaceType()) {
                    move.killdPeace(board[jumpedCol][jumpedRow].getPeace());
                    IsKill = true;
                    return true;
                }
                if ((dx != 1 && dy != 1)) {
                    return false;
                }
            }

            if(hasAvailableJumps()){
                return false;
            }

        }
        else{
            return false;
        }
        return true;
    }
    public void makeKing(peace piece){// pārtaisa kauliņu par karalieni
        piece.setIsking(true);
        if(piece.getPeaceType() == 0){
            piece.getPeace().setFill(Color.YELLOW);
        }else{
            piece.getPeace().setFill(Color.BLUE);
        }
    }
    public GameState(GameState state){ // kopēšanas konstruktors, šis ir nepieciešams, lai netiktu ietekmēts oriģinālasi laukums
        this.board = new boardCell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j <8 ; j++) {
                this.board[i][j] = new boardCell(state.getBoard()[i][j]);
            }
        }
        this.IsKill = state.IsKill;
        this.turn = state.turn;
        this.greenScore = state.greenScore;
        this.redScore = state.redScore;
        this.movecount = state.movecount;
    }
    public boolean IsDirection(int newRow, peace peace){ // pārbauda vai gājiens notiek pareizajā virzienā
        int direction = turn == 1 ? -1 : 1;
        return Math.signum(newRow - peace.getY_cord()) == Math.signum(direction);
    }
    public boolean hasAvailableJumps() { // pārbauda vai spēlētājam ir pieejami kaušanas gājieni
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                peace piece = board[row][col].getPeace();
                if (piece != null && piece.getPeaceType() == turn) {//
                    if(hasAvailableJumpsForPiece(piece, row, col)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean hasAvailableJumpsForPiece(peace piece, int col, int row) { // atkriež vai kauliņam ir iespējams kaut
        if(piece.getIsking()){
            for (int dirRow = -1; dirRow <= 1; dirRow += 2) {
                for (int dirCol = -1; dirCol <= 1; dirCol += 2) {
                    int jumpedRow = row + dirRow;
                    int jumpedCol = col + dirCol;
                    int destRow = row + dirRow * 2;
                    int destCol = col + dirCol * 2;
                    if (destRow>=0&&destCol>=0&&destRow<8&&destCol<8&&board[destCol][destRow].getPeace() == null &&
                            board[jumpedCol][jumpedRow].getPeace() != null &&
                            board[jumpedCol][jumpedRow].getPeace().getPeaceType() != piece.getPeaceType()) {
                        return true;
                    }
                }
            }
        }else {
            int dirRow = (piece.getPeaceType() == 0) ? 1 : -1;
            for (int dirCol = -1; dirCol <= 1; dirCol += 2) {
                int jumpedRow = row + dirRow;
                int jumpedCol = col + dirCol;
                int destRow = row + dirRow * 2;
                int destCol = col + dirCol * 2;
                if (destRow >= 0 && destCol >= 0 && destRow < 8 && destCol < 8 && board[destCol][destRow].getPeace() == null &&
                        board[jumpedCol][jumpedRow].getPeace() != null &&
                        board[jumpedCol][jumpedRow].getPeace().getPeaceType() != piece.getPeaceType()) {
                    return true;
                }
            }
        }
        return false;
    }


    public List<Move> getAllMoves() { // atgriež visus iespējamos atļautos gājienus
        List<Move> moves = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                peace piece = board[col][row].getPeace();
                if (piece == null || piece.getPeaceType() != turn) {
                    continue;
                }
                for (int dirRow = -1; dirRow <= 1; dirRow += 2) {
                    for (int dirCol = -1; dirCol <= 1; dirCol += 2) {
                        int destrow = piece.getY_cord() + dirRow;
                        int destcol = piece.getX_cord() + dirCol;
                        Move move = new Move(piece,destcol,destrow);
                        Move jumpMove = new Move(piece,destcol+dirCol,destrow+dirRow);
                        if(IsMoveLegal(move)){
                            moves.add(move);
                        }
                        if(IsMoveLegal(jumpMove)){
                            GameState newState = new GameState(this);
                            for (Move movee:jumpMoves(piece, newState)) {
                                movee.getPiece().setX_cord(col);
                                movee.getPiece().setY_cord(row);
                                moves.add(movee);
                            }

                        }
                    }
                }
            }
        }
        return moves;
    }
    private List<Move> jumpMoves(peace peace, GameState state){ // atgriež visus iespējamos kaušanas gājienus
        Map<Move,peace> jumps = get_Multiple_Jump_Move(peace, state);
        List<Move> lastJumps = new ArrayList<>();
        for (Map.Entry<Move,peace> entry : jumps.entrySet()) {
            Move jump = entry.getKey();
            peace killed = entry.getValue();
            state.MakeAIMove(jump);
            jump.getPiece().setX_cord(jump.getNewX());
            jump.getPiece().setY_cord(jump.getNewY());
            List<Move> nextJumps = jumpMoves(jump.getPiece(),state);
            if (nextJumps == null || nextJumps.size() == 0) {
                lastJumps.add(jump);
            } else {
                for (Move nextJump : nextJumps) {
                    nextJump.killdPeace(killed);
                    lastJumps.add(nextJump);
                }
            }
        }
        return lastJumps;
    }
    private Map<Move,peace> get_Multiple_Jump_Move(peace peace, GameState state){
        Map<Move,peace> map =new HashMap();
        int row = peace.getY_cord();
        int col = peace.getX_cord();
        if(peace.getIsking()){
            for (int dirRow = -1; dirRow <= 1; dirRow += 2) {
                for (int dirCol = -1; dirCol <= 1; dirCol += 2) {
                    int jumpedRow = row + dirRow;
                    int jumpedCol = col + dirCol;
                    int destRow = row + dirRow * 2;
                    int destCol = col + dirCol * 2;
                    Move move = new Move(peace, destCol,destRow);
                    if(state.IsMoveLegal(move)){
                        map.put(move,state.getBoard()[jumpedCol][jumpedRow].getPeace());
                    }
                }
            }
        }else {
            int dirRow = (peace.getPeaceType() == 0) ? 1 : -1;
            for (int dirCol = -1; dirCol <= 1; dirCol += 2) {
                int jumpedRow = row + dirRow;
                int jumpedCol = col + dirCol;
                int destRow = row + dirRow * 2;
                int destCol = col + dirCol * 2;
                Move move = new Move(peace, destCol,destRow);
                if(state.IsMoveLegal(move)){
                    if((destRow == 7 && move.getPiece().getPeaceType()==0 ) ||destRow == 0 && move.getPiece().getPeaceType()==1 ){
                        move.getPiece().setIsking(true);
                    }
                    map.put(move,state.getBoard()[jumpedCol][jumpedRow].getPeace());
                }
            }

        }
        return map;
    }

    public void MakeAIMove(Move move){ // šī funkcija paredzēta lai alpha-beta varētu veikt gājienus tā teikt prātā kas neitekmētu reālo spēles laukumu
        movecount++;
        peace peace = move.getPiece();
        int newCol = move.getNewX();
        int newRow = move.getNewY();
        board[newCol][newRow].setPeace(board[peace.getX_cord()][peace.getY_cord()].getPeace());

        if(!(newCol == peace.getX_cord() && newRow == peace.getY_cord())){
            board[peace.getX_cord()][peace.getY_cord()].setPeace(null);
        }
        board[newCol][newRow].getPeace().setX_cord(newCol);
        board[newCol][newRow].getPeace().setY_cord(newRow);
        if((board[newCol][newRow].getPeace().getY_cord() == 7 && board[newCol][newRow].getPeace().getPeaceType()==0 ) || board[newCol][newRow].getPeace().getY_cord() == 0 && board[newCol][newRow].getPeace().getPeaceType()==1 ){
            board[newCol][newRow].getPeace().setIsking(true);
            if(board[newCol][newRow].getPeace().getPeaceType() == 0){
                board[newCol][newRow].getPeace().getPeace().setFill(Color.YELLOW);
            }else{
                board[newCol][newRow].getPeace().getPeace().setFill(Color.BLUE);
            }
        }
        if(move.Iskill()){
            for (peace piece : move.getKilledpieces()) {
                board[piece.getX_cord()][piece.getY_cord()].setPeace(null);
            }
            if(peace.getPeaceType() == 0){
                redScore++;
            }else{
                greenScore++;
            }
            if(!hasAvailableJumpsForPiece(peace,newCol,newRow)){
                if(turn == 0){
                    this.setTurn(1);
                }else{
                    this.setTurn(0);
                }
            }
        }else{
            if(turn == 0){
                this.setTurn(1);
            }else{
                this.setTurn(0);
            }
        }
    }
    public void MakeMove(Move move){// šī finklcija veic reālo gājienu
        movecount++;
        peace peace = move.getPiece();
        int newCol = move.getNewX();
        int newRow = move.getNewY();
        board[newCol][newRow].setPeace(board[peace.getX_cord()][peace.getY_cord()].getPeace());
        if(!(newCol == peace.getX_cord() && newRow == peace.getY_cord())){
            board[peace.getX_cord()][peace.getY_cord()].setPeace(null);
        }
        if((newRow == 7 && peace.getPeaceType()==0 ) || newRow == 0 && peace.getPeaceType()==1 ){
            board[newCol][newRow].getPeace().setIsking(true);
            if(board[newCol][newRow].getPeace().getPeaceType() == 0){
                board[newCol][newRow].getPeace().getPeace().setFill(Color.YELLOW);
            }else{
                board[newCol][newRow].getPeace().getPeace().setFill(Color.BLUE);
            }
        }
        if(move.Iskill()){
            for (peace piece : move.getKilledpieces()) {
                int x = piece.getX_cord();
                int y = piece.getY_cord();
                pane.getChildren().remove(board[x][y].getPeace().getPeace());
                board[piece.getX_cord()][piece.getY_cord()].setPeace(null);
                if(piece.getPeaceType() == 0){
                    redScore++;
                }else{
                    greenScore++;
                }
            }
            if(!hasAvailableJumpsForPiece(peace,newCol,newRow)){
                if(turn == 0){
                    this.setTurn(1);
                }else{
                    this.setTurn(0);
                }
            }
        }else{
            if(turn == 0){
                this.setTurn(1);
            }else{
                this.setTurn(0);
            }
        }
        peace.Move(newCol,newRow);
    }
    public boardCell[][] getBoard() {
        return board;
    }
    public int getMovecount() {
        return movecount;
    }
    public int getRedScore() {
        return redScore;
    }
    public boolean isGameOver(){ // pārbauda vai spēle ir beigusies
        if(redScore == 12 || greenScore == 12 || movecount == 80){
            return true;
        }
        return false;
    }
    public void setTurn(int turn) {
        this.turn = turn;
    }
    public int getTurn() {
        return turn;
    }
public int evaluate(int player) { // novērtē spēles stāvokli
    int pieceCount = 0;
    int kingCount = 0;
    int opponentPieceCount = 0;
    int opponentKingCount = 0;
    int playerPositionScore = 0;
    int opponentPositionScore = 0;
    int centralControlScore = 0;

    for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
            peace piece = board[col][row].getPeace();
            if (piece == null) {
                continue;
            }
            if (piece.getPeaceType() == player) {
                pieceCount++;
                if (piece.isking()) {
                    kingCount++;
                }
                if (player == 0) {
                    playerPositionScore += row;
                } else {
                    playerPositionScore += (7 - row);
                }
                if ((row == 3 || row == 4) && (col == 3 || col == 4)) {
                    centralControlScore += 10;
                } else if ((row == 2 || row == 5) && (col == 2 || col == 5)) {
                    centralControlScore += 5;
                } else if ((row == 1 || row == 6) && (col == 1 || col == 6)) {
                    centralControlScore += 2;
                }
            } else {
                opponentPieceCount++;
                if (piece.isking()) {
                    opponentKingCount++;
                }
                if (player == 0) {
                    opponentPositionScore += (7 - row);
                } else {
                    opponentPositionScore += row;
                }
            }
        }
    }
    int score = (pieceCount - opponentPieceCount) * 100;
    score += (kingCount - opponentKingCount) * 200;
    score += (playerPositionScore - opponentPositionScore) * 10;
    score += centralControlScore;

    if(movecount == 80){
        return 0;
    } else {
        return score;
    }
}
}
