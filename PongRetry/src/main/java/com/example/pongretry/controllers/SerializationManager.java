package com.example.pongretry.controllers;

import java.io.*;

public class SerializationManager {

    // Method to serialize an object
    public static void serialize(Object obj, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(obj);
        }
    }

    // Method to deserialize an object
    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            return objectIn.readObject();
        }
    }
}
