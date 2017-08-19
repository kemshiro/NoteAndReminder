package com.k3mshiro.knotes.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.k3mshiro.knotes.R;
import com.k3mshiro.knotes.activity.MainAct;
import com.k3mshiro.knotes.dao.NoteDAO;
import com.k3mshiro.knotes.dto.NoteDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by k3mshiro on 8/16/17.
 */

public class CreateNoteFrg extends Fragment implements View.OnClickListener {

    public static final CharSequence RED = "red";
    public static final CharSequence ORANGE = "orange";
    public static final CharSequence YELLOW = "yellow";
    public static final CharSequence GREEN = "green";
    public static final CharSequence BLUE = "blue";
    public static final CharSequence INDIGO = "indigo";
    public static final CharSequence VIOLET = "violet";
    private static final String TAG = CreateNoteFrg.class.getName();

    private View view;
    private Button btnSave, btnAlarmSet, btnInfoLook;
    private FloatingActionButton fabEditNote;
    private EditText edtTitle;
    private EditText edtContent;
    private Button btnRed, btnOrange, btnYellow, btnGreen, btnBlue, btnIndigo, btnViolet;
    private MainAct mainAct;

    private NoteDAO noteDAO;
    private String parseColor = "#4CAF50";

    private FragmentManager fragmentMrg;

    public CreateNoteFrg() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edited_note, container, false);
        initViews();
        initNotes();
        return view;
    }

    private void initViews() {
        mainAct = (MainAct) getActivity();
        fragmentMrg = getFragmentManager();

        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnAlarmSet = (Button) view.findViewById(R.id.btn_alarm_set);
        btnInfoLook = (Button) view.findViewById(R.id.btn_info_look);

        View colorSetBar = view.findViewById(R.id.color_set_bar);

        btnRed = (Button) colorSetBar.findViewById(R.id.btn_red);
        btnRed.setContentDescription(RED);

        btnOrange = (Button) colorSetBar.findViewById(R.id.btn_orange);
        btnOrange.setContentDescription(ORANGE);

        btnYellow = (Button) colorSetBar.findViewById(R.id.btn_yellow);
        btnYellow.setContentDescription(YELLOW);

        btnGreen = (Button) colorSetBar.findViewById(R.id.btn_green);
        btnGreen.setContentDescription(GREEN);

        btnBlue = (Button) colorSetBar.findViewById(R.id.btn_blue);
        btnBlue.setContentDescription(BLUE);

        btnIndigo = (Button) colorSetBar.findViewById(R.id.btn_indigo);
        btnIndigo.setContentDescription(INDIGO);

        btnViolet = (Button) colorSetBar.findViewById(R.id.btn_violet);
        btnViolet.setContentDescription(VIOLET);

        edtTitle = (EditText) view.findViewById(R.id.edt_note_title);
        edtContent = (EditText) view.findViewById(R.id.edt_note_content);
        fabEditNote = (FloatingActionButton) view.findViewById(R.id.fab_edit_note);

        btnSave.setOnClickListener(this);
        btnAlarmSet.setOnClickListener(this);
        btnInfoLook.setOnClickListener(this);
        fabEditNote.setOnClickListener(this);

        btnRed.setOnClickListener(this);
        btnOrange.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnIndigo.setOnClickListener(this);
        btnViolet.setOnClickListener(this);
    }

    private void initNotes() {
        noteDAO = mainAct.getNoteDAO();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                createNewNote();
                break;
            case R.id.btn_alarm_set:
                break;
            case R.id.btn_info_look:
                break;
            case R.id.fab_edit_note:
                break;
            default:
                break;
        }

        if (v.getContentDescription() == RED) {
            parseColor = "#F44336";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == ORANGE) {
            parseColor = "#FB8C00";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == YELLOW) {
            parseColor = "#FFEA00";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == GREEN) {
            parseColor = "#4CAF50";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == BLUE) {
            parseColor = "#2196F3";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == INDIGO) {
            parseColor = "#9C27B0";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        } else if (v.getContentDescription() == VIOLET) {
            parseColor = "#673AB7";
            edtTitle.setTextColor(Color.parseColor(parseColor));

        }

    }

    private void createNewNote() {
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(c.getTime()).toString();
        String color = parseColor;

        NoteDTO newNote = new NoteDTO(title, date, content, color);
        boolean result = noteDAO.createNewNote(newNote);

        if (result) {
            Toast.makeText(getActivity(), "Successed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
        }
    }


}
