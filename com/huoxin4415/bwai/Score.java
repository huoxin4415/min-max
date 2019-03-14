package com.huoxin4415.bwai;

/**
 * 评分
 */
public class Score {

    public static int grade(int[][] chess, int piece, int freeSize) {
        int score = 0;
        piece = -1;

        if (freeSize == 0) {
            for (int x = 0; x < chess.length; x++) {
                for (int y = 0; y < chess[x].length; y++) {
                    if (chess[x][y] == piece) {
                        score += 2;
                        
                    } else if (chess[x][y] == -piece) {
                        score -= 2;
                    }
                }
            }
            return score;
        }

        int[][] highest = {{0,0}, {0, chess.length - 1}, {chess.length - 1, 0}, {chess.length - 1, chess.length - 1}};
        // highest
        for(int[] each : highest) {
            if (chess[each[0]][each[1]] == piece) {
                score += 10;
            } else if (chess[each[0]][each[1]] == -piece) {
                score -= 10;
            }
        }

        // high
        for (int x = 1; x < chess.length - 1; x++) {
            if (chess[x][0] == piece || chess[x][chess.length - 1] == piece) {
                score += 5;
            } else if (chess[x][0] == -piece || chess[x][chess.length - 1] == -piece) {
                score -= 5;
            }
        }
        for (int y = 1; y < chess.length - 1; y++) {
            if (chess[0][y] == piece || chess[chess.length - 1][y] == piece) {
                score += 5;
            } else if (chess[0][y] == -piece || chess[chess.length - 1][y] == -piece) {
                score -= 5;
            }
        }
        // middle
        for (int x = 2; x < chess.length - 3; x++) {
            for (int y = 2; y < chess[x].length - 3; y++) {
            	if (chess[x][y] == piece) {
                    score += 2;
            		
            	} else if (chess[x][y] == -piece) {
            		score -= 2;
                }
            }
        }
        // low
        for (int x = 1; x < chess.length - 1; x++) {
            if (chess[x][1] == piece || chess[x][chess.length - 2] == piece) {
                score += 1;
            } else if (chess[x][1] == -piece || chess[x][chess.length - 2] == -piece) {
                score -= 1;
            }
        }
        for (int y = 1; y < chess.length - 1; y++) {
            if (chess[1][y] == piece || chess[chess.length - 2][y] == piece) {
                score += 1;
            } else if (chess[1][y] == -piece || chess[chess.length - 2][y] == -piece) {
                score -= 1;
            }
        }
        return score;
    }
}