package com.huoxin4415.bwai;

import java.io.File;

import javax.swing.ImageIcon;

public enum Piece {
    BLACK(1, new ImageIcon("image" + File.separator + "black.png"), new ImageIcon("image" + File.separator + "black_sm.png")), 
    WHITE(-1, new ImageIcon("image" + File.separator + "white.png"), new ImageIcon("image" + File.separator + "white_sm.png"));

    private int val;
    private ImageIcon icon;
    private ImageIcon iconSm;

    private Piece(int val, ImageIcon icon, ImageIcon iconSm) {
        this.val = val;
        this.icon = icon;
        this.iconSm = iconSm;
    }

    public int val() {
        return this.val;
    }

    public Piece next() {
        if (this.equals(BLACK)) {
            return WHITE;
        } else {
            return BLACK;
        }
    }

    public ImageIcon getIcon() {
        return this.icon;
    }

    public ImageIcon getIconSm() {
        return this.iconSm;
    }
}