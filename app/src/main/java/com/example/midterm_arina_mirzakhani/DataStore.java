package com.example.midterm_arina_mirzakhani;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Simple in-memory store for the app’s process lifetime. */
public class DataStore {
    private static final Set<Integer> historyNumbers = new LinkedHashSet<>();

    public static void addToHistory(int n) {
        historyNumbers.add(n); // keeps insertion order, no duplicates
    }

    public static List<Integer> getHistory() {
        return new ArrayList<>(historyNumbers);
    }

    public static void clearHistory() {
        historyNumbers.clear();
    }
}

