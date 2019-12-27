package com.huoxin4415.bwai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.huoxin4415.bwai.Player.State;

public class BlackWhite extends JFrame {

    private static final long serialVersionUID = -5050817808729948877L;

    // private BlackWhiteAI ai;    // AI决策对象
    private Piece userPiece;    // 玩家棋子
    private Piece currentPiece; // 当前待落棋子
    private BWState state;    // 棋局状态

    private ChessBoard cb;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    private JButton[][] bs;     // 棋盘棋子按钮
    private JLabel scoreInfoLabel;  // 得分Label
    private JLabel currentPieceLabel;   // 当前待落棋子Label

    

    // 构造函数
    public BlackWhite(Player player1, Player player2, ChessBoard cb) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = this.player1;
        this.cb = cb;
        this.state = BWState.WAITING;
        this.currentPiece = player1.getPiece();

        // 初始化棋盘panel
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(cb.getWidth(), cb.getHeight(), 0, 0));
        bs = new JButton[cb.getWidth()][cb.getHeight()];
        for (int y = 0; y < cb.getHeight(); y++) {
            for (int x = 0; x < cb.getWidth(); x++) {
                JButton button = new JButton();
                button.addActionListener(new HumanPlayerActionListener(x, y, this));
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

    public void refush() {
        int[][] board = this.cb.getBoard();
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

        for (Point p : cb.getTrace()) {
            if (p.getX() < 0 || p.getX() > board.length - 1 || p.getY() < 0 || p.getY() > board.length - 1) {
                continue;
            } else {
                bs[p.getX()][p.getY()].setBorder(BorderFactory.createLineBorder(Color.RED));
            }

        }

        scoreInfoLabel.setText(String.format("黑：%d  白：%d", black, white));

    }

    public Piece nextPlayer() {
        if (this.currentPlayer == this.player1) {
            this.currentPlayer = this.player2;
        } else {
            this.currentPlayer = this.player1;
        }

        if (this.cb.hasChoice(this.currentPlayer.getPiece())) {
            this.currentPlayer.think();
            return this.currentPlayer.getPiece();
        } else if (isFinish()) {
            this.state = BWState.FINISH;
            return null;
        } else {
            return nextPlayer();
        }
    }

    private boolean isFinish() {
        return this.cb.getFreeSize() <= 0 
            || (!this.cb.hasChoice(this.player1.getPiece()) && !this.cb.hasChoice(this.player2.getPiece()));
    }

    public void start() {
        while (this.state != BWState.FINISH) {
            if (this.currentPlayer.getState() == Player.State.THINKING) {
                continue;
            } else if (this.currentPlayer.getState() == Player.State.DONE) {
                System.out.println("refush");
                refush();
                System.out.println("nextPlayer");
                nextPlayer();
            }
        }

        // finished
        JOptionPane.showMessageDialog(this, resultMsg(this.cb.result()), "黑白棋", JOptionPane.INFORMATION_MESSAGE);
    }

    private String resultMsg(int result) {
        return result > 0 ? "对局结束，黑方胜！" : result < 0 ? "对局结束，白方胜！" : "对局结束，双方平局！";
    }

    public void setState(BWState state) {
        this.state = state;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public static void main(String[] args) {
        int size = 8;
        ChessBoard cb = new ChessBoard(size, size);
        Player player1 = new HumanPlayer(cb, Piece.BLACK);
        player1.setState(State.THINKING);
        Player player2 = new BlackWhiteAI(cb, Piece.WHITE);
        player2.setState(State.DONE);
        BlackWhite bw = new BlackWhite(player1, player2, cb);
        bw.start();
    }

}