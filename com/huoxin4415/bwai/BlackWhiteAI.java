package com.huoxin4415.bwai;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlackWhiteAI extends Player{

    private TreeNode current;

    private ExecutorService es = Executors.newSingleThreadExecutor();

    public BlackWhiteAI(ChessBoard cb, Piece piece) {
        super(cb, piece);
    }

    public int[] next(int nextPiece) {
        long startTime = System.currentTimeMillis();
        LinkedList<Point> trace = cb.getTrace();
        this.current = new TreeNode(trace.getLast().getX(), trace.getLast().getY(), -nextPiece);
        // extend(this.current, 1, this.cb);
        extendCurrent(this.current, 1, this.cb);
        System.out.println(String.format("current score:%d", this.current.getScore().intValue()));
        TreeNode nextNode = new TreeNode(0, 0, 0);
        nextNode.setScore(Integer.MIN_VALUE);
        System.out.print("score:");
        for(TreeNode node : this.current.getChildren()) {
            System.out.print(String.format("[%d,%d]:%d  ", node.getX(), node.getY(), node.getScore()));
            if (node.getScore() > nextNode.getScore()) {
            	nextNode = node;
            } else if (node.getScore() == nextNode.getScore()) {
                if (Math.random() < 0.5) { // 评分相等，随机匹配
                    nextNode = node;
                }
            }
        }
        System.out.println();
        System.out.println("Cost Time: " + (System.currentTimeMillis() - startTime) / 1000 + "s");
        return new int[]{nextNode.getX(), nextNode.getY()};
    }

    public boolean hasChoice(int piece) {
        for (int x = Math.max(cb.getMinX() - 1, 0); x < Math.min(cb.getMaxX() + 2, cb.getWidth()); x++) {
            for (int y = Math.max(cb.getMinY() - 1, 0); y < Math.min(cb.getMaxY() + 2, cb.getHeight()); y++) {
                if (cb.getBoard()[x][y] == 0) {
                    ChessBoard childCb = new ChessBoard(cb);
                    if (childCb.fall(x, y, piece) != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
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
        
        if (level < Config.MAX_LEVEL) {
            boolean cut = false;
            for (int x = Math.max(cb.getMinX() - 1, 0); x < Math.min(cb.getMaxX() + 2, cb.getWidth()) && !cut; x++) {
                for (int y = Math.max(cb.getMinY() - 1, 0); y < Math.min(cb.getMaxY() + 2, cb.getHeight()); y++) {
                    if (cb.getBoard()[x][y] == 0) {

                        if (node.getScore() != null && node.getParent() != null) {
                            List<TreeNode> brothers = node.getParent().getChildren();

                            if (node.getPiece() != this.getPiece().val()) { // MAX
                                for (TreeNode brother : brothers) {
                                    if (node != brother && null != brother.getScore() && node.getScore() >= brother.getScore()) {
                                        cut = true;
                                        break;
                                    }
                                }

                                if (cut) {
                                    break; // Beta剪枝
                                }
                        		
                        	} else { // MIN
                                for (TreeNode brother : brothers) {
                                    if (node != brother && null != brother.getScore() && node.getScore() <= brother.getScore()) {
                                        cut = true;
                                        break;
                                    }
                                }

                                if (cut) {
                                    break; // Alpha剪枝
                                }
                        	}
                        }
                        
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
            // int score = SafetyScorer.grade(cb.getBoard(), this.piece);
            int score = PositionScorer.grade(cb.getBoard(), this.getPiece().val(), cb.getFreeSize());

            node.setScore(score);
            TreeNode p = node.getParent();
            if (p != null) {
                if (node.getPiece() != this.getPiece().val()) { // MAX
                    if (p.getScore() == null) {
                        p.setScore(score);
                    } else {
                        p.setScore(Math.min(p.getScore(), score));
                    }
                } else { // MIN
                    if (p.getScore() == null) {
                        p.setScore(score);
                    } else {
                        p.setScore(Math.max(p.getScore(), score));
                    }
                }
            }
            
            return;
        } else if (node.getParent() != null){ // 分支节点
            int score = node.getScore();
            if (node.getPiece() != this.getPiece().val()) { // MAX
                TreeNode p = node.getParent(); // MIN
                if (p.getScore() == null) {
                    p.setScore(score);
                } else {
                    p.setScore(Math.min(p.getScore(), score));
                }
            } else { // MIN
                TreeNode p = node.getParent(); // MAX
                if (p.getScore() == null) {
                    p.setScore(score);
                } else {
                    p.setScore(Math.max(p.getScore(), score));
                }
            }
            return;
        }

    }

    private void extendCurrent(TreeNode node, int level, ChessBoard cb) {
        List<CompletableFuture<Void>> cfList = new LinkedList<>();

        for (int x = Math.max(cb.getMinX() - 1, 0); x < Math.min(cb.getMaxX() + 2, cb.getWidth()); x++) {
            for (int y = Math.max(cb.getMinY() - 1, 0); y < Math.min(cb.getMaxY() + 2, cb.getHeight()); y++) {
                if (cb.getBoard()[x][y] == 0) {
                    
                    ChessBoard childCb = new ChessBoard(cb);
                    if (childCb.fall(x, y, -node.getPiece()) == 0) {
                        continue;
                    }
                    
                    TreeNode child = new TreeNode(x, y, -node.getPiece());
                    node.addChild(child);
                    child.setParent(node);

                    cfList.add(CompletableFuture.runAsync(new ExtendRunnable(child, ++level, childCb)));

                    
                }
            }
        }
        CompletableFuture<Void> cfs = CompletableFuture.allOf(cfList.toArray(new CompletableFuture[cfList.size()]));
        cfs.join();
    }

    @Override
    public void think() {
        setState(State.THINKING);
        
        es.execute(() -> {
            int[] nextFall = next(this.getPiece().val());

            // AI想好后自动落子
            fall(nextFall[0], nextFall[1]);
        });
    }

    class ExtendRunnable implements Runnable {

        private TreeNode node;
        private int level;
        private ChessBoard cb;

        public ExtendRunnable(TreeNode node, int level, ChessBoard cb) {
            this.node = node;
            this.level = level;
            this.cb = cb;
        }

        public void run() {
            BlackWhiteAI.this.extend(node, level, cb);
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

        public void setPiece(int piece) {
            this.piece = piece;
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