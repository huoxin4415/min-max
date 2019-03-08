package com.huoxin4415.bwai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BlackWhite extends JFrame {

    private static final long serialVersionUID = -5050817808729948877L;

    private int piece;
    private BlackWhiteAI ai;

    private JButton[][] bs;
    private JLabel scoreInfo;

    // 构造函数
    public BlackWhite(BlackWhiteAI ai, int size) {
        this.piece = 1;
        this.ai = ai;

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(size, size, 0, 0));
        bs = new JButton[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                JButton button = new JButton();
                button.addActionListener(new BWActionListener(x, y, ai, this));
                boardPanel.add(button);
                bs[x][y] = button;
            }
        }

        // tihs.add(boardPanel,BorderLayout.NORTH);
        boardPanel.setSize(300, 300);
        this.add(boardPanel, BorderLayout.CENTER);
        

        JPanel scorePanel = new JPanel();
        scoreInfo = new JLabel();
        scoreInfo.setText("黑：2  白：2");
        scorePanel.add(scoreInfo);

        this.add(scorePanel, BorderLayout.SOUTH);

        // 设置窗体属性
        this.setTitle("黑白棋");
        this.setSize(300, 350);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(200, 200);

        // 显示
        this.setVisible(true);

        refush();
    }

    public int getPiece() {
        return this.piece;
    }

    public void setPiece(int piece) {
        this.piece = piece;
    }

    public void refush() {
        int[][] board = ai.getCb().getBoard();
        int white = 0;
        int black = 0;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == 1) {
                    bs[x][y].setBackground(Color.BLACK);
                    black++;
                } else if (board[x][y] == -1){
                    bs[x][y].setBackground(Color.WHITE);
                    white++;
                }
            }
        }
        scoreInfo.setText(String.format("黑：%d  白：%d", black, white));
    }

    class BWActionListener implements ActionListener {

        private int x;
        private int y;
        private BlackWhiteAI ai;
        private BlackWhite bw;

        public BWActionListener(int x, int y, BlackWhiteAI ai, BlackWhite bw) {
            this.x = x;
            this.y = y;
            this.ai = ai;
            this.bw = bw;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (ai.fall(x, y, bw.getPiece()) == 0) {
                return;
            }
            refush();

            bw.setPiece(-bw.getPiece());

            int[] next = ai.next();
            if (ai.fall(next[0], next[1], bw.getPiece()) == 0) {
                return;
            }
            refush();

            bw.setPiece(-bw.getPiece());
        }

    }

    public static void main(String[] args) {
        int size = 8;
        BlackWhiteAI ai = new BlackWhiteAI(size, size);
        new BlackWhite(ai, size);
    }
}