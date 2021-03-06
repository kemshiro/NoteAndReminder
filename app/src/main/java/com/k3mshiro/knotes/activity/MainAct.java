package com.k3mshiro.knotes.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.k3mshiro.knotes.R;
import com.k3mshiro.knotes.dao.Constant;
import com.k3mshiro.knotes.dao.NoteDAO;
import com.k3mshiro.knotes.dto.NoteDTO;
import com.k3mshiro.knotes.fragment.EditNoteFrg;
import com.k3mshiro.knotes.fragment.ListNoteFrg;

/**
 * Created by k3mshiro on 8/18/17.
 */

public class MainAct extends AppCompatActivity implements ListNoteFrg.IFragmentConnection {

    private static final int REQUEST_CODE_PERMISSIONS = 100;

    private FragmentManager fragmentManager;
    private Fragment listNotesFrg;
    private NoteDAO noteDAO;

    public NoteDAO getNoteDAO() {
        return noteDAO;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                initializeComponents();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSIONS);
            }
        } else {
            initializeComponents();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            initializeComponents();
        } else {
            Toast.makeText(this,
                    "Cấp quyền cho ứng dụng, vui lòng thử lại!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initializeComponents() {
        setContentView(R.layout.act_main);
        noteDAO = new NoteDAO(this);
        noteDAO.openDatabase();
        showListNotesScreen();
    }


    private void showListNotesScreen() {
        fragmentManager = getFragmentManager();
        listNotesFrg = new ListNoteFrg();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.act_main, listNotesFrg, ListNoteFrg.class.getName());
        fragmentTransaction.commit();
    }

    @Override
    public void sendDataToEditNoteFrg(NoteDTO note) {
        EditNoteFrg editNoteFrg = new EditNoteFrg();
        Bundle bundle = new Bundle();

        bundle.putSerializable(Constant.KEY_EDIT_NOTE, note);
        editNoteFrg.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.act_main, editNoteFrg, EditNoteFrg.class.getName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
