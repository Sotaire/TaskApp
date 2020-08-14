package com.example.taskapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.ImageView;

import com.example.taskapp.ui.ElementLIstener;
import com.example.taskapp.ui.dashboard.ImageListener;
import com.example.taskapp.ui.home.AdapterListener;
import com.example.taskapp.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ImageListener, ElementLIstener, AdapterListener {
    public static final String ITEM = "item";
    public static final String ID = "id";
    boolean isTrue;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    ImageView imageView;
    TaskAdapter taskAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_dashboard ||destination.getId() == R.id.navigation_notifications ||destination.getId() == R.id.navigation_home || destination.getId() == R.id.navigation_profile ){
                    navView.setVisibility(View.VISIBLE);
                } else {navView.setVisibility(View.GONE);}
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration)  || super.onSupportNavigateUp();
    }

    @Override
    public void setImage(ImageView image) {
        Intent imageIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent,42);
        imageView = image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        switch(requestCode) {
            case 42:
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onItemClick(String s,int position) {
        Bundle bundle = new Bundle();
        bundle.putString(ITEM, s);
        bundle.putInt(ID,position);
        navController.navigate(R.id.action_navigation_home_to_formFragment,bundle);
    }

    @Override
    public void fragmentOpen(int position) {
        showDialog(1);
        if (isTrue)
        taskAdapter.deleteElement(position);
        taskAdapter.notifyDataSetChanged();
    }
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(R.string.exit);
            // сообщение
            adb.setMessage(R.string.save_data);
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            adb.setPositiveButton(R.string.yes, myClickListener);
            // кнопка отрицательного ответа
            adb.setNegativeButton(R.string.no, myClickListener);
            // кнопка нейтрального ответа
            adb.setNeutralButton(R.string.cancel, myClickListener);
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    isTrue = true;
                    break;
                // негативная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
                // нейтральная кнопка
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };

    @Override
    public void delete(TaskAdapter taskAdapter) {
        this.taskAdapter = taskAdapter;
    }
}