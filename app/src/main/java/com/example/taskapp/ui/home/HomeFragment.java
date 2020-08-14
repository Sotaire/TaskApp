package com.example.taskapp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.MainActivity;
import com.example.taskapp.R;
import com.example.taskapp.TaskAdapter;
import com.example.taskapp.models.Task;
import com.example.taskapp.ui.ElementLIstener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private TaskAdapter taskAdapter;
    ArrayList<Task> list;
    int position = -1;
    ElementLIstener elementLIstener;
    boolean isTrue;
    int posit;
    AdapterListener adapterListener;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        elementLIstener = (MainActivity) context;
        adapterListener = (MainActivity) context;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home,menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            initList(view);
            initResultlistener();

    }

    private void initResultlistener() {
        taskAdapter.listener = elementLIstener;
        getParentFragmentManager().setFragmentResultListener("form", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e("home","onFragmentResult");
                Task task = (Task) result.getSerializable("task");
                position = result.getInt("position");
                if (position != -1){
                    taskAdapter.setElement(position,task);
                }else{
                list.add(0,task);}
                adapterListener.delete(taskAdapter);
                taskAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        if (list == null){
        list = new ArrayList<>();
        list.add(new Task("Talgar",0L));
        list.add(new Task("Tilek",0L));
        list.add(new Task("Aza",0L));
        list.add(new Task("fgj",0L));
        list.add(new Task("Talfdhfgar",0L));
        list.add(new Task("Talfhntrggar",0L));
        }
        taskAdapter = new TaskAdapter(list);
        recyclerView.setAdapter(taskAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add){
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_navigation_home_to_formFragment);
        }
        return super.onOptionsItemSelected(item);
    }
    }
