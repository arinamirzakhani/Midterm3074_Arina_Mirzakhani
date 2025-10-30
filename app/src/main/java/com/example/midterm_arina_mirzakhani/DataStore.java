package com.example.midterm_arina_mirzakhani;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class DataStore {


    private static final String PREFS = "times_table_prefs";
    private static final String KEY_HISTORY_CSV = "history_csv";
    private static SharedPreferences prefs;


    private static final LinkedHashSet<Integer> historyNumbers = new LinkedHashSet<>();


    public static void init(Context appCtx) {
        if (prefs != null) return;
        prefs = appCtx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);


        String csv = prefs.getString(KEY_HISTORY_CSV, "");
        if (csv != null && !csv.isEmpty()) {
            for (String p : csv.split(",")) {
                try {
                    historyNumbers.add(Integer.parseInt(p.trim()));
                } catch (NumberFormatException ignored) {}
            }
        }
    }

    public static void addToHistory(int n) {
        boolean added = historyNumbers.add(n);
        if (added) persist();
    }

    public static List<Integer> getHistory() {
        return new ArrayList<>(historyNumbers);
    }

    public static void clearHistory() {
        historyNumbers.clear();
        persist();
    }

    private static void persist() {
        if (prefs == null) return;
        StringBuilder sb = new StringBuilder();
        for (Integer i : historyNumbers) {
            if (sb.length() > 0) sb.append(',');
            sb.append(i);
        }
        prefs.edit().putString(KEY_HISTORY_CSV, sb.toString()).apply();
    }
}
