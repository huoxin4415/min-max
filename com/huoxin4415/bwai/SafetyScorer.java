package com.huoxin4415.bwai;

/**
 * 安全性评分算法
 * @author huoxin4415 
 * @version 1.0 2019-11-08
 */
public class SafetyScorer {
    
    /**
     * 棋子在某一方向预测分为：安全、危险。
     * 安全意味着在该方向当前棋子不存在被吃的可能。
     * 遍历棋盘内所有待评分方棋子，若该棋子在某方向安全，则评分++。
     * @param chess 全部棋子
     * @param piece 棋子类型（1：黑棋，-1：白棋）
     * @return 棋局评分
     */
    public static int grade(int[][] chess, int piece) {
        int score = 0;
        for (int x = 0; x < chess.length; x++) {
            for (int y = 0; y < chess[x].length; y++) {
                if (chess[x][y] != piece) {
                    continue;
                }
                int width = chess.length;
                int height = chess[x].length;
                // left
                if(isSafe(chess, x, y, piece, -1, 0, x)) {
                    score++;
                }  
                // right
                if (isSafe(chess, x, y, piece, 1, 0, width - x - 1)) {
                    score++;
                }
                // top
                if (isSafe(chess, x, y, piece, 0, -1, y)) {
                    score++;
                }
                // bottom
                if (isSafe(chess, x, y, piece, 0, 1, height - y - 1)) {
                    score++;
                }
                // left-top
                if (isSafe(chess, x, y, piece, -1, -1, Math.min(x, y))) {
                    score++;
                }
                // right-top
                if (isSafe(chess, x, y, piece, 1, -1, Math.min(width - x - 1, y))) {
                    score++;
                }
                // left-bottom
                if (isSafe(chess, x, y, piece, -1, 1, Math.min(x, height - y - 1))) {
                    score++;
                }
                // right-bottom
                if (isSafe(chess, x, y, piece, 1, 1, Math.min(width - x - 1, height - y - 1))) {
                    score++;
                }   
            }
        }
        return score;
    }

    private static boolean isSafe(int[][] chess, int x, int y, int piece, int xIncr, int yIncr, int length) {
        for (int i = 1; i <= length; i++) {
            if (chess[x + xIncr * i][y + yIncr * i] == -piece) {
                return true;
            } else if (chess[x + xIncr * i][y + yIncr * i] == piece) { 
                continue;
            } else if (chess[x + xIncr * i][y + yIncr * i] == 0) {
                return false;
            }
        }
        return true;
    }
}