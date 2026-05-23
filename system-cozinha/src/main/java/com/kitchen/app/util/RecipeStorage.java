package com.kitchen.app.util;

import com.kitchen.app.model.Recipe;

import java.io.*;

public class RecipeStorage {

    private RandomAccessFile file;

    public RecipeStorage(String fileName) throws IOException {
        file = new RandomAccessFile(fileName, "rw");
    }

    public long saveRecipe(Recipe recipe) throws IOException {
        long position = file.length();
        file.seek(position);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(recipe);
        byte[] data = baos.toByteArray();

        file.writeInt(data.length);
        file.write(data);

        return position;
    }

    public Recipe loadRecipe(long position) throws IOException, ClassNotFoundException {
        file.seek(position);
        int size = file.readInt();
        byte[] data = new byte[size];

        file.readFully(data);

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);

        return (Recipe) ois.readObject();
    }
}