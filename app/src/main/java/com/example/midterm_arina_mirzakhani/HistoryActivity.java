package com.example.midterm_arina_mirzakhani;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ArrayAdapter<Integer> adapter;
    private List<Integer> numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("History");

        // Optional: show Up arrow as well
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ListView lv = findViewById(R.id.lvHistory);
        numbers = DataStore.getHistory();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numbers);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((p, v, pos, id) ->
                Toast.makeText(this, "Table generated for " + numbers.get(pos), Toast.LENGTH_SHORT).show()
        );

        // NEW: Back button -> return to Main screen
        Button btnBack = findViewById(R.id.btnBackToMain);
        btnBack.setOnClickListener(v -> finish()); // simply close this activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Handle ActionBar Up arrow as back
        if (id == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }

        if (id == R.id.action_clear_history) {
            if (numbers.isEmpty()) {
                Toast.makeText(this, "History is already empty", Toast.LENGTH_SHORT).show();
                return true;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Clear history?")
                    .setMessage("Remove all saved numbers from history?")
                    .setPositiveButton("Clear", (d, w) -> {
                        DataStore.clearHistory();
                        numbers.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
