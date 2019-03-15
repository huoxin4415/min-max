package com.huoxin4415.bwai;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BlackWhiteAI {

    private ChessBoard cb;
    private TreeNode current;

    private static final int MAX_LEVEL = 23;

    private int piece;

    public BlackWhiteAI(int width, int height) {
        this.cb = new ChessBoard(width, height);
        this.piece = -1;
    }

    public int fall(int x, int y, int piece) {
        if (this.cb.fall(x, y, piece) != 0) {
            this.current = new TreeNode(x, y, piece);
            System.out.println(String.format("%s fall:[%d,%d]", piece == 1? "black" : "white", x ,y));
            return piece;
        } else {
            return 0;
        }
    }

    public int[] next() {
        extend(this.current, 1, this.cb);
        System.out.println(String.format("current score:%d", this.current.getScore().intValue()));
        TreeNode nextNode = new TreeNode(0, 0, 0);
        nextNode.setScore(Integer.MIN_VALUE);
        System.out.print("score：");
        for(TreeNode node : this.current.getChildren()) {
            System.out.print(String.format("[%d,%d]:%d  ", node.getX(), node.getY(), node.getScore()));
            if (node.getScore() > nextNode.getScore()) {
            	nextNode = node;
            }
        }
        System.out.println();

        return new int[]{nextNode.getX(), nextNode.getY()};
    }

    public boolean isFinish() {
        return this.cb.getFreeSize() <= 0;
    }

    public int result() {
        Integer black = 0;
        Integer white = 0;
        int[][] board = this.cb.getBoard();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == 1) {
                    black++;
                } else if (board[x][y] == -1) {
                    white++;
                } 
            }
        }
        return black.compareTo(white);
    }

    public ChessBoard getCb() {
        return this.cb;
    }

    public void setCb(ChessBoard cb) {
        this.cb = cb;
    }

    private void extend(TreeNode node, int level, ChessBoard cb) {
    	// if (cb.getFreeSize() == 0) {
    	// 	return;
    	// }
        
        if (level < MAX_LEVEL) {
    		// for (int x = 0; x < cb.getWidth(); x++) {
            //     for (int y = 0; y < cb.getHeight(); y++) {
            for (int x = Math.max(cb.getMinX() - 1, 0); x < Math.min(cb.getMaxX() + 2, cb.getWidth()); x++) {
                for (int y = Math.max(cb.getMinY() - 1, 0); y < Math.min(cb.getMaxY() + 2, cb.getHeight()); y++) {
                    if (cb.getBoard()[x][y] == 0) {
                        // if (node.getScore() != null && node.getParent() != null) {
                        // 	if (node.getPiece() != current.getPiece()) { // MAX
                        // 		if (node.getScore() > node.getParent().getScore()) { // MIN
                        // 			continue; // 剪枝
                        // 		}
                        // 	} else { // MIN
                        // 		if (node.getScore() < node.getParent().getScore()) { // MAX
                        // 			continue; // 剪枝
                        // 		}
                        // 	}
                        // }
                        
                        ChessBoard childCb = new ChessBoard(cb);
                        if (childCb.fall(x, y, -node.getPiece()) == 0) {
                            continue;
                        }
                        
                        TreeNode child = new TreeNode(x, y, -node.getPiece());
                        node.addChild(child);
                        child.setParent(node);
                        
                        extend(child, ++level, childCb);
                    }
                }
            }

            if (node.getChildren().size() == 0 && cb.getFreeSize() != 0) { // 没有可下位置，换对方下
                ChessBoard childCb = new ChessBoard(cb);
                
                TreeNode child = new TreeNode(node.getX(), node.getY(), -node.getPiece());
                node.addChild(child);
                child.setParent(node);
                        
                extend(child, ++level, childCb);
            }
        }

        if (node.getChildren() == null || node.getChildren().size() == 0) { // 叶子节点
            int score = Score.grade(cb.getBoard(), this.piece, cb.getFreeSize());

            if (cb.getFreeSize() == 0) { // 结束
                if (score > 0) {
                    // System.out.println("win!");
                } else {
                    // System.out.println("lost!");
                }
            }

            node.setScore(score);
            if (node.getPiece() == current.getPiece()) { // MAX
                TreeNode p = node.getParent(); // MIN
                TreeNode pp = p.getParent(); // MAX
                if (p.getScore() == null) {
                    p.setScore(score);
                } else {
                    p.setScore(Math.min(p.getScore(), score));
                }
                // if (pp != null) {
                //     if (pp.getScore() == null) {
                //         pp.setScore(p.getScore());
                //     } else {
                //         pp.setScore(Math.max(p.getScore(), pp.getScore()));
                //     }
                // }
                return;
            } else { // MIN
                TreeNode p = node.getParent(); // MAX
                TreeNode pp = p.getParent(); // MIN
                if (p.getScore() == null) {
                    p.setScore(score);
                } else {
                    p.setScore(Math.max(p.getScore(), score));
                }
                // if (pp != null) {
                // 	if (pp.getScore() == null) {
                //         pp.setScore(p.getScore());
                //     } else {
                //         pp.setScore(Math.min(p.getScore(), pp.getScore()));
                //     }
                // }
            }
            return;
        } else if (node.getParent() != null){ // 分支节点
            int score = node.getScore();
            if (node.getPiece() == current.getPiece()) { // MAX
                TreeNode p = node.getParent(); // MIN
                TreeNode pp = p.getParent(); // MAX
                if (p.getScore() == null) {
                    p.setScore(score);
                } else {
                    p.setScore(Math.min(p.getScore(), score));
                }
                // if (pp != null) {
                //     if (pp.getScore() == null) {
                //         pp.setScore(p.getScore());
                //     } else {
                //         pp.setScore(Math.max(p.getScore(), pp.getScore()));
                //     }
                // }
                return;
            } else { // MIN
                TreeNode p = node.getParent(); // MAX
                TreeNode pp = p.getParent(); // MIN
                if (p.getScore() == null) {
                    p.setScore(score);
                } else {
                    p.setScore(Math.max(p.getScore(), score));
                }
                // if (pp != null) {
                // 	if (pp.getScore() == null) {
                //         pp.setScore(p.getScore());
                //     } else {
                //         pp.setScore(Math.min(p.getScore(), pp.getScore()));
                //     }
                // }
            }
            return;
        }

    }

    class TreeNode {
        private int x;
        private int y;
        private int piece;
        private Integer score;
        private TreeNode parent;
        private List<TreeNode> children;

        public TreeNode(int x, int y, int piece) {
            this.x = x;
            this.y = y;
            this.piece = piece;
            this.parent = null;
            this.children = new LinkedList<>();
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getPiece() {
            return this.piece;
        }

        public Integer getScore() {
            return this.score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void setParent(TreeNode parent) {
            this.parent = parent;
        }

        public TreeNode getParent() {
            return this.parent;
        }

        public void addChild(TreeNode child) {
            children.add(child);
        }

        public List<TreeNode> getChildren() {
            return this.children;
        }
    }
}