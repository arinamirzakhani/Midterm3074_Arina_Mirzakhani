package com.example.midterm_arina_mirzakhani;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class TableFragment extends Fragment implements MenuProvider {

    private EditText etNumber;
    private Button btnInsert;
    private ListView lvResults;
    private ArrayAdapter<String> adapter;
    private final ArrayList<String> rows = new ArrayList<>();

    private static final String KEY_ROWS = "rows";
    private static final String KEY_NUM  = "num";

    public TableFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_table, container, false);

        etNumber  = v.findViewById(R.id.etNumber);
        btnInsert = v.findViewById(R.id.btnInsert);
        lvResults = v.findViewById(R.id.lvResults);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, rows);
        lvResults.setAdapter(adapter);

        if (savedInstanceState != null) {
            ArrayList<String> saved = savedInstanceState.getStringArrayList(KEY_ROWS);
            if (saved != null) { rows.clear(); rows.addAll(saved); adapter.notifyDataSetChanged(); }
            etNumber.setText(savedInstanceState.getString(KEY_NUM, ""));
        }

        btnInsert.setOnClickListener(vw -> {
            String txt = etNumber.getText().toString().trim();
            if (TextUtils.isEmpty(txt)) {
                etNumber.setError("Enter a number");
                return;
            }
            try {
                int n = Integer.parseInt(txt);
                generateTable(n);
                DataStore.addToHistory(n);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });

        lvResults.setOnItemClickListener((parent, view, position, id) -> {
            String item = rows.get(position);
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete row?")
                    .setMessage("Remove: " + item + " ?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        rows.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(requireContext(), "Deleted: " + item, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return v;
    }

    private void generateTable(int n) {
        rows.clear();
        for (int i = 1; i <= 10; i++) {
            rows.add(n + " × " + i + " = " + (n * i));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle out) {
        super.onSaveInstanceState(out);
        out.putStringArrayList(KEY_ROWS, rows);
        out.putString(KEY_NUM, etNumber.getText().toString());
    }


    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.menu_table, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_clear_all) {
            if (rows.isEmpty()) {
                Toast.makeText(requireContext(), "Nothing to clear", Toast.LENGTH_SHORT).show();
                return true;
            }
            new AlertDialog.Builder(requireContext())
                    .setTitle("Clear all?")
                    .setMessage("Delete all rows from the current table?")
                    .setPositiveButton("Clear", (d, w) -> {
                        rows.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(requireContext(), "All rows cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }
        return false;
    }
}
