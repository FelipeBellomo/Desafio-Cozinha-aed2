package com.kitchen.app.datastructures;

public class BTreeNode {

    public int m;
    public int maxKeys;
    public int maxChilds;
    public int numKeys;
    public int[] keys;
    public long[] recipePositions;
    public long[] childrenPositions;
    public boolean isLeaf;
    public long selfPosition;

    public BTreeNode(int m, boolean isLeaf) {
        this.m = m;
        this.maxKeys = (2 * m) - 1;
        this.maxChilds = 2 * m;
        this.isLeaf = isLeaf;
        this.keys = new int[maxKeys];
        this.recipePositions = new long[maxKeys];
        this.childrenPositions = new long[maxChilds];
        this.numKeys = 0;
    }
}