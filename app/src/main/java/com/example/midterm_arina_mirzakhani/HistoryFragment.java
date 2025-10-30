package com.example.midterm_arina_mirzakhani;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class HistoryFragment extends Fragment implements MenuProvider {

    private ArrayAdapter<Integer> adapter;
    private List<Integer> numbers;

    public HistoryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        ListView lv = v.findViewById(R.id.lvHistory);
        numbers = DataStore.getHistory();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, numbers);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((p, vw, pos, id) ->
                Toast.makeText(requireContext(), "Table generated for " + numbers.get(pos), Toast.LENGTH_SHORT).show()
        );

        // Attach menu for this tab
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return v;
    }

    // Toolbar menu for History tab
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.menu_history, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_clear_history) {
            if (numbers.isEmpty()) {
                Toast.makeText(requireContext(), "History is already empty", Toast.LENGTH_SHORT).show();
                return true;
            }
            new AlertDialog.Builder(requireContext())
                    .setTitle("Clear history?")
                    .setMessage("Remove all saved numbers from history?")
                    .setPositiveButton("Clear", (d, w) -> {
                        DataStore.clearHistory();
                        numbers.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(requireContext(), "History cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }
        return false;
    }
}
