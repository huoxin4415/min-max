package com.huoxin4415.bwai;

public class ChessBoard {
    private int width;
    private int height;
    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private int[][] board;
    private int freeSize;

    public ChessBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[width][height];
        this.board[width / 2 - 1][height / 2 - 1] = 1;
        this.board[width / 2][height / 2 - 1] = -1;
        this.board[width / 2][height / 2] = 1;
        this.board[width / 2 - 1][height / 2] = -1;
        this.freeSize = width * height - 4; 
        this.minX = width / 2 - 1;
        this.maxX = width / 2;
        this.minY = height / 2 - 1;
        this.maxY = height / 2;
    }

    public ChessBoard(ChessBoard cb) {
        this.width = cb.width;
        this.height = cb.height;
        this.board = new int[width][height];
        this.freeSize = width * height;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.board[i][j] = cb.board[i][j];
            }
        }
    }

    public int fall(int x, int y, int piece) {
        if (x >= width || y >= height) {
            return 0;
        }

        if (board[x][y] == 0) {
            if (trans(x, y, piece) == 0) { // 未翻转任何棋子
                return 0; // 落子失败
            }

            board[x][y] = piece;
            if (x < minX) {
                minX = x;
            } else if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            } else if(y > maxY) {
                maxY = y;
            }
            this.freeSize--;
            return piece;
        } else {
            return 0;
        }
    }

    public int trans(int x, int y, int piece) {
        int result = 0;
        // left
        result += trans(x, y, piece, -1, 0, x);
        
        // right
        result += trans(x, y, piece, 1, 0, width - x - 1);

        // top
        result += trans(x, y, piece, 0, -1, y);

        // bottom
        result += trans(x, y, piece, 0, 1, height - y - 1);

        // left-top
        result += trans(x, y, piece, -1, -1, Math.min(x, y));

        // right-top
        result += trans(x, y, piece, 1, -1, Math.min(width - x - 1, y));

        // left-bottom
        result += trans(x, y, piece, -1, 1, Math.min(x, height - y - 1));

        // right-bottom
        result += trans(x, y, piece, 1, 1, Math.min(width - x - 1, height - y - 1));
        return result;
    }

    private int trans(int x, int y, int piece, int xIncr, int yIncr, int length) {
        int result = 0;
        for (int i = 1; i <= length; i++) {
            if (board[x + xIncr * i][y + yIncr * i] == piece) {
                for (int j = 1; j <= i; j++) {
                    board[x + xIncr * j][y + yIncr * j] = piece;
                    result++;
                }
                break;
            } else if (board[x + xIncr * i][y + yIncr * i] == 0) {
                break;
            }
        }
        return result;
    }

    public int[][] getBoard() {
        return this.board;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getFreeSize() {
        return this.freeSize;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }
}