package com.huoxin4415.bwai;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BlackWhiteAI {

    private ChessBoard cb;
    private TreeNode current;

    public BlackWhiteAI(int width, int height) {
        this.cb = new ChessBoard(width, height);
    }

    public int fall(int x, int y, int piece) {
        if (this.cb.fall(x, y, piece) != 0) {
            this.current = new TreeNode(x, y, piece);
            return piece;
        } else {
            return 0;
        }
    }

    public int[] next() {
        extend(this.current, 1, this.cb);
        TreeNode nextNode = new TreeNode(0, 0, 0);
        nextNode.setScore(Integer.MIN_VALUE);
        for(TreeNode node : this.current.getChildren()) {
            if (node.getScore() > nextNode.getScore()) {
            	nextNode = node;
            }
        }

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
    	if (cb.getFreeSize() == 0) {
    		return;
    	}
    	
    		// for (int x = 0; x < cb.getWidth(); x++) {
            //     for (int y = 0; y < cb.getHeight(); y++) {
        for (int x = Math.max(cb.getMinX() - 2, 0); x < Math.min(cb.getMaxX() + 2, cb.getWidth()); x++) {
            for (int y = Math.max(cb.getMinY() - 2, 0); y < Math.min(cb.getMaxY() + 2, cb.getHeight()); y++) {
                if (cb.getBoard()[x][y] == 0) {
                    // if (node.getScore() != null && node.getParent() != null) {
                    // 	if (node.getPiece() == current.getPiece()) { // MAX
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
    	
    	
        if (node.getChildren() == null || node.getChildren().size() == 0) { // 叶子节点
            int score = Score.grade(cb.getBoard(), -current.getPiece());
            node.setScore(score);
            if (node.getPiece() == current.getPiece()) { // MAX
                TreeNode p = node.getParent(); // MIN
                TreeNode pp = p.getParent(); // MAX
                if (p.getScore() == null) {
                    p.setScore(score);
                } else {
                    p.setScore(Math.min(p.getScore(), score));
                }
                if (pp != null) {
                	pp.setScore(p.getScore());
                }
                return;
            } else { // MIN
                TreeNode p = node.getParent(); // MAX
                TreeNode pp = p.getParent(); // MIN
                if (p.getScore() == null) {
                    p.setScore(score);
                } else {
                    p.setScore(Math.max(p.getScore(), score));
                }
                if (pp != null) {
                	pp.setScore(p.getScore());
                }
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
                if (pp != null) {
                	pp.setScore(p.getScore());
                }
                return;
            } else { // MIN
                TreeNode p = node.getParent(); // MAX
                TreeNode pp = p.getParent(); // MIN
                if (p.getScore() == null) {
                    p.setScore(score);
                } else {
                    p.setScore(Math.max(p.getScore(), score));
                }
                if (pp != null) {
                	pp.setScore(p.getScore());
                }
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

    public static void main(String[] args) {
        BlackWhiteAI ai = new BlackWhiteAI(4, 4, 20);
        try(Scanner s = new Scanner(System.in)) {
            while (s.hasNext()) {
                String[] inStr = s.next().split(",");
                ai.fall(Integer.valueOf(inStr[0]), Integer.valueOf(inStr[1]), 1);
                System.out.println("BLACK:[" + inStr[0] + "," + inStr[1] + "]");
                int[] next = ai.next();
                ai.fall(next[0], next[1], -1);
                System.out.println("WHITE:[" + next[0] + "," + next[1] + "]");
                
            }
        }
        
    }
}