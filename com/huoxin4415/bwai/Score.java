package com.huoxin4415.bwai;

/**
 * 评分
 */
public class Score {

    private static final int SCORE_HIGHEST = 10;
    private static final int SCORE_HIGH = 5;
    private static final int SCORE_MIDDLE = 2;
    private static final int SCORE_LOW = 1;
    
    public static int grade(int[][] chess, int piece, int freeSize) {
        if (freeSize == 0) {
            return expectScore(chess, piece);
        } else {
            return clearScore(chess, piece);
        }
    }

    /**
     * 期望评分
     * 用于未完成棋局评分
     * @return
     */
    private static int expectScore(int[][] chess, int piece) {
        int score = 0;
        for (int x = 0; x < chess.length; x++) {
            for (int y = 0; y < chess[x].length; y++) {
                if (chess[x][y] == piece) {
                    score += SCORE_MIDDLE;
                    
                } else if (chess[x][y] == -piece) {
                    score -= SCORE_MIDDLE;
                }
            }
        }
        return score;
    } 

    /**
     * 明确评分
     * 用于已完成棋局评分
     * @return
     */
    private static int clearScore(int[][] chess, int piece) {
        int score = 0;
        int[][] highest = {{0,0}, {0, chess.length - 1}, {chess.length - 1, 0}, {chess.length - 1, chess.length - 1}};
        // highest
        for(int[] each : highest) {
            if (chess[each[0]][each[1]] == piece) {
                score += SCORE_HIGHEST;
            } else if (chess[each[0]][each[1]] == -piece) {
                score -= SCORE_HIGHEST;
            }
        }

        // high
        for (int x = 1; x < chess.length - 1; x++) {
            if (chess[x][0] == piece || chess[x][chess.length - 1] == piece) {
                score += SCORE_HIGH;
            } else if (chess[x][0] == -piece || chess[x][chess.length - 1] == -piece) {
                score -= SCORE_HIGH;
            }
        }
        for (int y = 1; y < chess.length - 1; y++) {
            if (chess[0][y] == piece || chess[chess.length - 1][y] == piece) {
                score += SCORE_HIGH;
            } else if (chess[0][y] == -piece || chess[chess.length - 1][y] == -piece) {
                score -= SCORE_HIGH;
            }
        }

        // middle
        for (int x = 2; x < chess.length - 3; x++) {
            for (int y = 2; y < chess[x].length - 3; y++) {
            	if (chess[x][y] == piece) {
                    score += SCORE_MIDDLE;
            		
            	} else if (chess[x][y] == -piece) {
            		score -= SCORE_MIDDLE;
                }
            }
        }
        
        // low
        for (int x = 1; x < chess.length - 1; x++) {
            if (chess[x][1] == piece || chess[x][chess.length - 2] == piece) {
                score += SCORE_LOW;
            } else if (chess[x][1] == -piece || chess[x][chess.length - 2] == -piece) {
                score -= SCORE_LOW;
            }
        }
        for (int y = 1; y < chess.length - 1; y++) {
            if (chess[1][y] == piece || chess[chess.length - 2][y] == piece) {
                score += SCORE_LOW;
            } else if (chess[1][y] == -piece || chess[chess.length - 2][y] == -piece) {
                score -= SCORE_LOW;
            }
        }
        return score;
    }
}