package com.huoxin4415.bwai;

/**
 * Player类
 */
public abstract class Player {

    /** 对战棋盘 */
    protected ChessBoard cb;
    /** 所持棋子 */
    protected Piece piece;
    /** 状态 */
    private volatile State state;

    /**
     * 构造函数
     * @param cb 对战棋盘
     * @param piece 所持棋子
     */
    public Player(ChessBoard cb, Piece piece) {
        this.cb = cb;
        this.piece = piece;
    }

    public abstract void think();

    /**
     * 落子
     * @param x 横坐标
     * @param y 纵坐标
     * @return 0 : 失败； 黑落子成功：1，白落子成功：-1
     */
    public int fall(int x, int y) {
        System.out.println(String.format("%s fall:[%d,%d]", piece.toString(), x ,y));
        int result = this.cb.fall(x, y, piece.val());
        System.out.println("result:" + result);
        setState(State.DONE);
        System.out.println(getState());
        return result;
    }

    public Piece getPiece() {
        return piece;
    }

    public enum State {
        THINKING, DONE;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}