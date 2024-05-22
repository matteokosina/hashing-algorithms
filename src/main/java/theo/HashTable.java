package theo;

import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class HashTable {
    private int size;
    private Integer[] table;

    public HashTable(int size) {
        this.size = size;
        table = new Integer[size];
    }

    private int hashFunction(int key) {
        return key % size;
    }

    public void insert(int key) {
        int index = hashFunction(key);
        while (table[index] != null) {
            index = (index + 1) % size;
        }
        table[index] = key;
    }

    public Integer search(int key) {
        int index = hashFunction(key);
        while (table[index] != null) {
            if (table[index] == key) {
                return index;
            }
            index = (index + 1) % size;
        }
        return null;
    }

    public int getCollisionsAt(int index) {
        int collisions = 0;
        int currentIndex = index;
        while (table[currentIndex] != null) {
            collisions++;
            currentIndex = (currentIndex + 1) % size;
        }
        return collisions;
    }

    public void writeCollisionsToCSV(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.append("Index,Kollisionen\n");

            // Iteriere über die Tabelle und schreibe die Anzahl der Kollisionen für jeden Index
            for (int i = 0; i < size; i++) {
                int collisions = getCollisionsAt(i);
                writer.append(String.valueOf(i)).append(",").append(String.valueOf(collisions)).append("\n");
            }

            writer.close();
            System.out.println("CSV-Datei erfolgreich erstellt: " + filename);
        } catch (IOException e) {
            System.out.println("Fehler beim Schreiben der CSV-Datei: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        int tableSize = 100;
        HashTable hashTable = new HashTable(tableSize);
        Random random = new Random();

        // Zufällige Zahlen generieren und in die Hashtabelle einfügen
        for (int i = 0; i < 50; i++) { // Füllstand von 50%
            hashTable.insert(random.nextInt(1000));
        }

        // Zugriff auf die Hashtabelle und Zählen der Kollisionen
        int collisions = 0;
        int numTests = 100;
        int minCollisions = Integer.MAX_VALUE;
        int maxCollisions = Integer.MIN_VALUE;
        for (int i = 0; i < numTests; i++) {
            int key = random.nextInt(1000);
            if (hashTable.search(key) != null) {
                collisions++;
                // Update minCollisions und maxCollisions
                int currentCollisions = hashTable.getCollisionsAt(hashTable.hashFunction(key));
                minCollisions = Math.min(minCollisions, currentCollisions);
                maxCollisions = Math.max(maxCollisions, currentCollisions);
            }
        }

        // Mittelwert der Kollisionen berechnen
        double avgCollisions = (double) collisions / numTests;
        System.out.println("Durchschnittliche Kollisionen: " + avgCollisions);
        System.out.println("Minimale Kollisionen für den Index: " + minCollisions);
        System.out.println("Maximale Kollisionen für den Index: " + maxCollisions);

        // CSV-Datei mit den Kollisionen erstellen
        hashTable.writeCollisionsToCSV("collisions.csv");
    }


}
