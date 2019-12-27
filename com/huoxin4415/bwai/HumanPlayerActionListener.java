package com.huoxin4415.bwai;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HumanPlayerActionListener implements ActionListener {

    private int x;
    private int y;
    private BlackWhite bw;

    public HumanPlayerActionListener(int x, int y, BlackWhite bw) {
        this.x = x;
        this.y = y;
        this.bw = bw;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Player currentPlayer = bw.getCurrentPlayer();
        System.out.println(currentPlayer.getPiece());
        System.out.println(currentPlayer.getState());
        // 当前玩家是人类
        if (currentPlayer instanceof HumanPlayer) {
            currentPlayer.fall(x, y);
        }
    }

}