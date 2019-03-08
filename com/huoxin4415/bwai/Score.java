package com.huoxin4415.bwai;

/**
 * 评分
 */
public class Score {

    public static int grade(int[][] chess, int piece) {
        piece = -1;
        int score = 0;
        for (int x = 0; x < chess.length; x++) {
            for (int y = 0; y < chess[x].length; y++) {
            	if (chess[x][y] == piece) {
                    score++;
            		
            	} else if (chess[x][y] == -piece) {
            		score--;
                }
            }
        }
        return score;
    }
}