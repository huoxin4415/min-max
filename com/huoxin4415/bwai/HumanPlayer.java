package com.huoxin4415.bwai;

public class HumanPlayer extends Player {

    public HumanPlayer(ChessBoard cb, Piece piece) {
        super(cb, piece);
    }

    @Override
    public void think() {
        // 玩家去想
        setState(State.THINKING);
    }

}