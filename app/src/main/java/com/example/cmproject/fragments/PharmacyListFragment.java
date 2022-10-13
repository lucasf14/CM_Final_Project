package com.example.cmproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmproject.R;
import com.example.cmproject.objects.Pharmacy;
import com.example.cmproject.tasks.TaskManager;
import com.example.cmproject.databinding.FragmentPharmacyListBinding;

import java.util.ArrayList;

public class PharmacyListFragment extends Fragment implements TaskManager.Callback {

    private FragmentPharmacyListBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<Pharmacy> pharmacyArrayList = new ArrayList<>();
    ;
    private PharmacyAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Needs Google API Key (Removed for safety)
        String apiKey = "YOUR_API_KEY_HERE";
   
        binding = FragmentPharmacyListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TaskManager taskManager = new TaskManager(getContext());
        taskManager.executeGetAPIInfo(this);

        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new PharmacyAdapter(root.getContext(), pharmacyArrayList);
        recyclerView.setAdapter(adapter);



        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onComplete(ArrayList<Pharmacy> pharmacies) {
        pharmacyArrayList = pharmacies;
        adapter.notifyDataSetChanged();
        PharmacyAdapter adapter = new PharmacyAdapter(this.getContext(), pharmacies);
        recyclerView.setAdapter(adapter);
        System.out.println("primeiro aqui");
    }


}
