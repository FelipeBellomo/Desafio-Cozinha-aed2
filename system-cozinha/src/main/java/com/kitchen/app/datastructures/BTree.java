package com.kitchen.app.datastructures;

import java.io.IOException;

import com.kitchen.app.util.DiskManager;

public class BTree {

    private BTreeNode root;
    private DiskManager diskManager;

    private int m;

    public BTree(String fileName, int m) {
        this.m = m;
        try {
            diskManager = new DiskManager(fileName);
            long rootPos = diskManager.getRootPosition();

            if (rootPos != -1) {
                // file exists
                root = diskManager.readNode(rootPos, m);
                System.out.println("Árvore B carregada do disco (Raiz na posição: " + rootPos + ")");
            } else {
                // file doesnt exists
                root = new BTreeNode(m, true);
                root.selfPosition = diskManager.allocateNode();
                diskManager.writeNode(root);
                diskManager.saveRootPosition(root.selfPosition);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long search(int key) {
        return search(root.selfPosition, key);
    }

    private Long search(long nodePosition, int key) {
        try {
            BTreeNode node = diskManager.readNode(nodePosition, m);
            int i = 0;

            while (i < node.numKeys && key > node.keys[i])
                i++;

            if (i < node.numKeys && node.keys[i] == key)
                return node.recipePositions[i];

            if (node.isLeaf)
                return null;

            return search(node.childrenPositions[i], key);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(int key, long recipePosition) {
        BTreeNode rootNode = root;

        if (rootNode.numKeys == root.maxKeys) {
            BTreeNode newRoot = new BTreeNode(m, false);

            try {
                newRoot.selfPosition = diskManager.allocateNode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            newRoot.childrenPositions[0] = rootNode.selfPosition;
            splitChild(newRoot, 0, rootNode);
            root = newRoot;

            try {
                diskManager.saveRootPosition(root.selfPosition);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        insertNonFull(root, key, recipePosition);
    }

    private void insertNonFull(BTreeNode node, int key, long recipePosition) {
        int i = node.numKeys - 1;
        if (node.isLeaf) {
            while (i >= 0 && key < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                node.recipePositions[i + 1] = node.recipePositions[i];
                i--;
            }

            node.keys[i + 1] = key;
            node.recipePositions[i + 1] = recipePosition;
            node.numKeys++;

        } else {
            while (i >= 0 && key < node.keys[i])
                i--;

            i++;
            try {
                BTreeNode child = diskManager.readNode(node.childrenPositions[i], m);

                if (child.numKeys == root.maxKeys) {
                    splitChild(node, i, child);

                    if (key > node.keys[i])
                        i++;

                    child = diskManager.readNode(node.childrenPositions[i], m);
                }

                insertNonFull(child, key, recipePosition);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            diskManager.writeNode(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void splitChild(BTreeNode parent, int index, BTreeNode fullChild) {
        BTreeNode newNode = new BTreeNode(fullChild.m, fullChild.isLeaf);

        newNode.numKeys = m - 1;

        try {
            newNode.selfPosition = diskManager.allocateNode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int j = 0; j < m - 1; j++) {
            newNode.keys[j] = fullChild.keys[j + m];
            newNode.recipePositions[j] = fullChild.recipePositions[j + m];
        }

        if (!fullChild.isLeaf) {
            for (int j = 0; j < m; j++) {
                newNode.childrenPositions[j] = fullChild.childrenPositions[j + m];
            }
        }

        fullChild.numKeys = m - 1;

        for (int j = parent.numKeys; j >= index + 1; j--) {
            parent.childrenPositions[j + 1] = parent.childrenPositions[j];
        }
        parent.childrenPositions[index + 1] = newNode.selfPosition;

        for (int j = parent.numKeys - 1; j >= index; j--) {
            parent.keys[j + 1] = parent.keys[j];
            parent.recipePositions[j + 1] = parent.recipePositions[j];
        }

        parent.keys[index] = fullChild.keys[m - 1];
        parent.recipePositions[index] = fullChild.recipePositions[m - 1];
        parent.numKeys++;
        try {
            diskManager.writeNode(fullChild);
            diskManager.writeNode(newNode);
            diskManager.writeNode(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}