package com.example.midterm_arina_mirzakhani;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etNumber;
    private Button btnInsert;
    private Button btnHistory;
    private ListView lvResults;
    private ArrayAdapter<String> adapter;
    private final ArrayList<String> rows = new ArrayList<>();
    private static final String KEY_ROWS = "rows";
    private static final String KEY_NUM  = "num";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DataStore.init(getApplicationContext());

        setContentView(R.layout.activity_main);
        setTitle("timesTable");

        etNumber = findViewById(R.id.etNumber);
        btnInsert = findViewById(R.id.btnInsert);
        btnHistory = findViewById(R.id.btnHistory);
        lvResults = findViewById(R.id.lvResults);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows);
        lvResults.setAdapter(adapter);


        if (savedInstanceState != null) {
            ArrayList<String> saved = savedInstanceState.getStringArrayList(KEY_ROWS);
            if (saved != null) {
                rows.clear();
                rows.addAll(saved);
                adapter.notifyDataSetChanged();
            }
            etNumber.setText(savedInstanceState.getString(KEY_NUM, ""));
        }

        btnInsert.setOnClickListener(v -> {
            String txt = etNumber.getText().toString().trim();
            if (TextUtils.isEmpty(txt)) {
                etNumber.setError("Enter a number");
                return;
            }
            try {
                int n = Integer.parseInt(txt);
                generateTable(n);
                DataStore.addToHistory(n); // persisted by DataStore
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });


        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HistoryActivity.class))
        );


        lvResults.setOnItemClickListener((parent, view, position, id) -> {
            String item = rows.get(position);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete row?")
                    .setMessage("Remove: " + item + " ?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        rows.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Deleted: " + item, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void generateTable(int n) {
        rows.clear();
        for (int i = 1; i <= 10; i++) {
            rows.add(n + " × " + i + " = " + (n * i));
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        } else if (id == R.id.action_clear_all) {
            if (rows.isEmpty()) return true;
            new AlertDialog.Builder(this)
                    .setTitle("Clear all?")
                    .setMessage("Delete all rows from the current table?")
                    .setPositiveButton("Clear", (d, w) -> {
                        rows.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "All rows cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle out) {
        super.onSaveInstanceState(out);
        out.putStringArrayList(KEY_ROWS, rows);
        out.putString(KEY_NUM, etNumber.getText().toString());
    }
}
