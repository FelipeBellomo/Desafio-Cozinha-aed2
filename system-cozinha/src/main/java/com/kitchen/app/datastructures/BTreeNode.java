package com.kitchen.app.datastructures;

public class BTreeNode {

    public int t;
    public int numKeys;
    public int[] keys;
    public long[] recipePositions;
    public long[] childrenPositions;
    public boolean isLeaf;
    public long selfPosition;

    public BTreeNode(int t, boolean isLeaf) {
        this.t = t;
        this.isLeaf = isLeaf;
        this.keys = new int[2 * t - 1];
        this.recipePositions = new long[2 * t - 1];
        this.childrenPositions = new long[2 * t];
        this.numKeys = 0;
    }
}