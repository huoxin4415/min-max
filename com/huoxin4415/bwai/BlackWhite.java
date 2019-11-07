package com.huoxin4415.bwai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BlackWhite extends JFrame {

    private static final long serialVersionUID = -5050817808729948877L;

    private BlackWhiteAI ai;    // AI决策对象
    private Piece userPiece;    // 玩家棋子
    private Piece currentPiece; // 当前待落棋子

    private JButton[][] bs;     // 棋盘棋子按钮
    private JLabel scoreInfoLabel;  // 得分Label
    private JLabel currentPieceLabel;   // 当前待落棋子Label

    private LinkedList<Point> trace;    // 落子轨迹

    // 构造函数
    public BlackWhite(BlackWhiteAI ai, int size, Piece userPiece) {
        this.userPiece = userPiece;
        this.currentPiece = userPiece;
        this.ai = ai;

        this.trace = new LinkedList<>();
        this.trace.addFirst(new Point(Integer.MIN_VALUE, Integer.MIN_VALUE)); // 占位对象
        this.trace.addFirst(new Point(Integer.MIN_VALUE, Integer.MIN_VALUE)); // 占位对象

        // 初始化棋盘panel
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
        boardPanel.setSize(300, 300);
        this.add(boardPanel, BorderLayout.CENTER);

        // 初始化得分panel
        JPanel scorePanel = new JPanel();
        scoreInfoLabel = new JLabel();
        scoreInfoLabel.setText("黑：2  白：2");
        scorePanel.add(scoreInfoLabel);
        scorePanel.add(new JLabel("    当前:"));
        this.currentPieceLabel = new JLabel();
        this.currentPieceLabel.setIcon(this.currentPiece.getIconSm());
        scorePanel.add(currentPieceLabel);

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

    public Piece getUserPieceEnum() {
        return this.userPiece;
    }

    public Piece getCurrentPiece() {
        return this.currentPiece;
    }

    // 保存落子轨迹
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
                if (board[x][y] == Piece.BLACK.val()) {
                    bs[x][y].setIcon(Piece.BLACK.getIcon());
                    black++;
                } else if (board[x][y] == Piece.WHITE.val()){
                    bs[x][y].setIcon(Piece.WHITE.getIcon());
                    white++;
                }
            }
        }

        for (Point p : trace) {
            if (p.getX() < 0 || p.getX() > board.length - 1 || p.getY() < 0 || p.getY() > board.length - 1) {
                continue;
            } else {
                bs[p.getX()][p.getY()].setBorder(BorderFactory.createLineBorder(Color.RED));
            }

        }

        scoreInfoLabel.setText(String.format("黑：%d  白：%d", black, white));

    }

    public Piece switchPiect() {
        if (ai.hasChoice(-this.currentPiece.val())) {
            this.currentPiece = this.currentPiece.next();
            currentPieceLabel.setIcon(this.currentPiece.getIconSm());
            return this.currentPiece;
        } else if (ai.hasChoice(this.currentPiece.val())) {
            return this.currentPiece;
        } else {
            return null;
        }

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
            if (!bw.getUserPieceEnum().equals(bw.getCurrentPiece())) {
                return;
            }

            if (ai.fall(x, y, bw.getCurrentPiece().val()) == 0) {
                return;
            } else {
                trace(x, y);
                bw.refush();

                if (ai.isFinish()) {
                    JOptionPane.showMessageDialog(bw, resultMsg(ai.result()), "黑白棋", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            Piece lastPiece = bw.getCurrentPiece();
            Piece nextPiece = bw.switchPiect();
            if (nextPiece == lastPiece) {
                return;
            } else if (nextPiece == null) {
                JOptionPane.showMessageDialog(bw, resultMsg(ai.result()), "黑白棋", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            next(nextPiece);

        }

        private void next(Piece piece) {
            
            new Thread(() -> {
                Piece lastPiece;
                Piece nextPiece = piece;
                do {
                    int[] next = ai.next(nextPiece.val());
                    if (ai.fall(next[0], next[1], bw.getCurrentPiece().val()) == nextPiece.val()) {
                        trace(next[0], next[1]);
                        bw.refush();
    
                        if (ai.isFinish()) {
                            JOptionPane.showMessageDialog(bw, resultMsg(ai.result()), "黑白棋",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    lastPiece = bw.getCurrentPiece();
                    nextPiece = bw.switchPiect();
                    if (nextPiece == null) {
                        JOptionPane.showMessageDialog(bw, resultMsg(ai.result()), "黑白棋", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                } while (nextPiece == lastPiece);
            }).start();
            
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
        new BlackWhite(ai, size, Piece.BLACK);
    }
}