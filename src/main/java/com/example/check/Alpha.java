package com.example.check;

public class Alpha {
    private static final int MAX_DEPTH = 8; // maksimālais algoritma dziļums
    private static int player; // norāda kurš spēlētājs ir maksimizētājs vai nu 0 = sarkanie vai 1 = zaļie

    private static int alphaBeta(GameState state, int depth, int alpha, int beta, boolean maximizingPlayer) { // pats alpha-beta lgoritms
        if (depth == 0 || state.isGameOver()) { // ja dziļums ir sasniegts vai ja spēles stāvoklis ir beigu stāvoklis novērtēt stavokli un atgries heiristisko novērtējumu
            return state.evaluate(player);
        }
        if (maximizingPlayer) { // ja ir gājiens maksimizācijas spēlētājam
            int maxEval = Integer.MIN_VALUE;
            for (Move move : state.getAllMoves()) { // Ģenerē visus iespējamos gājienus noteiktajā stāvoklī
                GameState newstate = new GameState(state); // rada stāvokļa kopiju, lai neietekmētu reālo laikumu
                newstate.MakeAIMove(move);// veic AI gājienu
                int eval = alphaBeta(newstate, depth - 1, alpha, beta, false); // rekursīvi izsauc alpha beta funkciju ar jauno stāvokli un samazinātu dziļumu
                maxEval = Math.max(maxEval, eval); //  izvēlas leilāko vērtību kas līdz šim atrasta
                alpha = Math.max(alpha, maxEval);// atjauno alpha vērtību
                if (beta <= alpha) {
                    break; // ja beta ir vienāda vai mazāka par alpha notiek nogriešana
                }
            }
            return maxEval;
        } else { // ja ir minimizētāja gājiens tiek mēģināts minimizēt maksimizētāja iznākumu
            int minEval = Integer.MAX_VALUE;
            for (Move move : state.getAllMoves()) {
                GameState newstate = new GameState(state);
                newstate.MakeAIMove(move);
                int eval = alphaBeta(newstate, depth - 1, alpha, beta, true); // notiek rekursija
                minEval = Math.min(minEval, eval); // izvēlas mazāko vērtību kas līdz šim atrasta
                beta = Math.min(beta, minEval);// atjauno beta vērtību
                if (beta <= alpha) {
                    break; // ja beta ir vienāda vai mazāka par alpha notiek nogriešana
                }
            }
            return minEval;
        }
    }
    public static Move findBestMove(GameState state, int Player) { // meklēt labāko gājienu noteiktajā stāvoklī
        player = Player;
        Move bestMove = null;
        int maxEval = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        for (Move move : state.getAllMoves()) {// ģenērē visus iespējamos gājienus noteiktajā stāvoklī
            GameState newstate = new GameState(state); // rada stāvokļa kopiju, lai neietekmētu reālo laikumu
            newstate.MakeAIMove(move);// veic AI gājienu
            int eval = alphaBeta(newstate, MAX_DEPTH, alpha, beta, false); // izsauc alpha beta algoritmu kas rekursīvi noteiks šī gājiena patieso heiristisko novērtējumu
            if (eval > maxEval) { //atjauno to kurš ir labākasi gājiens līdz šim
                maxEval = eval;
                bestMove = move;
            }
            alpha = Math.max(alpha, eval);
        }
        return bestMove; // atkgriež labāko gājienu
    }

}
