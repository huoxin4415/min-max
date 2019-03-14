package com.huoxin4415.bwai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BlackWhite extends JFrame {

    private static final long serialVersionUID = -5050817808729948877L;

    private static final ImageIcon iconBlack = new ImageIcon("image" + File.separator + "black.png");
    private static final ImageIcon iconWhite = new ImageIcon("image" + File.separator + "white.png");

    private int piece;
    private BlackWhiteAI ai;

    private JButton[][] bs;
    private JLabel scoreInfo;

    private LinkedList<Point> trace;

    // 构造函数
    public BlackWhite(BlackWhiteAI ai, int size) {
        this.piece = 1;
        this.ai = ai;
        this.trace = new LinkedList<>();
        this.trace.addFirst(new Point(Integer.MIN_VALUE, Integer.MIN_VALUE));
        this.trace.addFirst(new Point(Integer.MIN_VALUE, Integer.MIN_VALUE));

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(size, size, 0, 0));
        bs = new JButton[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                JButton button = new JButton();
                button.addActionListener(new BWActionListener(x, y, ai, this));
                button.setBackground(new Color(37, 105, 46));
                button.setSize(58, 58);
                button.setBorder(BorderFactory.createLineBorder(new Color(123, 179, 128)));
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
        this.setSize(480, 530);
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

    public void trace(int x, int y) {
        trace.addLast(new Point(x, y));
        Point rp = trace.removeFirst();
        

        if (rp.getX() >= 0 && rp.getX() < bs.length && rp.getY() >= 0 && rp.getY() < bs.length) {
            bs[rp.getX()][rp.getY()].setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    }

    public void refush() {
        int[][] board = ai.getCb().getBoard();
        int white = 0;
        int black = 0;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == 1) {
                    bs[x][y].setIcon(iconBlack);
                    black++;
                } else if (board[x][y] == -1){
                    bs[x][y].setIcon(iconWhite);
                    white++;
                }
            }
        }

        for(Point p : trace) {
            if (p.getX() < 0 || p.getX() > board.length - 1 || p.getY() < 0 || p.getY() > board.length - 1) {
                continue;
            } else {
                bs[p.getX()][p.getY()].setBorder(BorderFactory.createLineBorder(Color.RED));
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
            trace(x, y);
            refush();

            if (ai.isFinish()) {

                ai.result();
                JOptionPane.showMessageDialog(bw, resultMsg(ai.result()), "黑白棋", JOptionPane.INFORMATION_MESSAGE);  
                return;
            } 

            bw.setPiece(-bw.getPiece());

            int[] next = ai.next();
            if (ai.fall(next[0], next[1], bw.getPiece()) == 0) {
                return;
            }
            trace(next[0], next[1]);
            refush();

            if (ai.isFinish()) {
                JOptionPane.showMessageDialog(bw, resultMsg(ai.result()), "黑白棋", JOptionPane.INFORMATION_MESSAGE);  
                return;
            } 

            bw.setPiece(-bw.getPiece());
        }

        private String resultMsg(int result) {
            return result > 0 ? "对局结束，黑方胜！" : result < 0 ? "对局结束，白方胜！" : "对局结束，双方平局！";
        }

    }

    class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        } 

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }
    }

    public static void main(String[] args) {
        int size = 8;
        BlackWhiteAI ai = new BlackWhiteAI(size, size);
        new BlackWhite(ai, size);
    }
}