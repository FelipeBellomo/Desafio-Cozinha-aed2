package com.kitchen.app.util;

import java.io.IOException;
import java.io.RandomAccessFile;

import com.kitchen.app.datastructures.BTreeNode;

public class DiskManager {

    private static final int PAGE_SIZE = 4096;

    private RandomAccessFile file;

    public DiskManager(String fileName) throws IOException {
        file = new RandomAccessFile(fileName, "rw");

        if (file.length() == 0) {
            file.setLength(8); 
            file.seek(0);
            file.writeLong(-1); // -1 doesnt have root
        }
    }

    public void saveRootPosition(long rootPosition) throws IOException {
        file.seek(0);
        file.writeLong(rootPosition);
    }

    public long getRootPosition() throws IOException {
        file.seek(0);
        return file.readLong();
    }

    public long allocateNode() throws IOException {
        long position = file.length();
        file.setLength(position + PAGE_SIZE);
        return position;
    }

    public void writeNode(BTreeNode node) throws IOException {
        file.seek(node.selfPosition);
        file.writeBoolean(node.isLeaf);
        file.writeInt(node.numKeys);

        for (int i = 0; i < node.maxKeys; i++)
            file.writeInt(node.keys[i]);

        for (int i = 0; i < node.maxKeys; i++)
            file.writeLong(node.recipePositions[i]);

        for (int i = 0; i < node.maxChilds; i++)
            file.writeLong(node.childrenPositions[i]);
    }

    public BTreeNode readNode(long position, int t) throws IOException {
        file.seek(position);

        boolean isLeaf = file.readBoolean();

        BTreeNode node = new BTreeNode(t, isLeaf);
        node.selfPosition = position;
        node.numKeys = file.readInt();

        for (int i = 0; i < node.maxKeys; i++)
            node.keys[i] = file.readInt();

        for (int i = 0; i < node.maxKeys; i++)
            node.recipePositions[i] = file.readLong();

        for (int i = 0; i < node.maxChilds; i++)
            node.childrenPositions[i] = file.readLong();

        return node;
    }

}