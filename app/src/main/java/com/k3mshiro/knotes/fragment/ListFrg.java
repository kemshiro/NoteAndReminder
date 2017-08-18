package com.k3mshiro.knotes.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.k3mshiro.knotes.R;
import com.k3mshiro.knotes.adapter.NoteAdapter;
import com.k3mshiro.knotes.customview.VerticalItemSpace;
import com.k3mshiro.knotes.dao.NoteDAO;
import com.k3mshiro.knotes.dto.NoteDTO;

import java.util.List;

/**
 * Created by k3mshiro on 7/21/17.
 */

public class ListFrg extends Fragment implements View.OnClickListener {
    private static final String TAG = ListFrg.class.getName();
    private static final int VERTICAL_ITEM_SPACE = 40;

    private View view;
    private RecyclerView rvList;
    private FloatingActionButton fab;
    private FloatingActionButton fabNewNote;
    private FloatingActionButton fabNewPhoto;
    private FloatingActionButton fabNewAlarm;
    private Animation goNanimation, goNWanimation, goWanimation, retNanimation, retNWanimation, retWanimation;

    private NoteAdapter mNoteAdapter;
    private List<NoteDTO> noteDTOs;
    private NoteDAO noteDAO;

    boolean isFloatingActionButonShow = false;

    public ListFrg() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        initViews();
        initNotes();
        return view;
    }

    private void initNotes() {
        noteDAO = new NoteDAO(getActivity());
        noteDTOs = noteDAO.getListNotes();
        for (int i = 0; i < noteDTOs.size(); i++) {
            Log.d(TAG, noteDTOs.get(i).getTitle() + "\n");
        }
        mNoteAdapter = new NoteAdapter(getActivity(), noteDTOs);
        rvList.setAdapter(mNoteAdapter);
        mNoteAdapter.notifyDataSetChanged();
    }

    private void initViews() {

        rvList = (RecyclerView) view.findViewById(R.id.cardList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(llm);
        //add ItemDecoration - them khoang cach
        rvList.addItemDecoration(new VerticalItemSpace(VERTICAL_ITEM_SPACE));

        /* them divider
        rvList.addItemDecoration(new DividerItem(getActivity()));
        //or
        rvList.addItemDecoration(
                new DividerItem(getActivity(), R.drawable.divider));
         */

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fabNewNote = (FloatingActionButton) view.findViewById(R.id.fab_new_note);
        fabNewPhoto = (FloatingActionButton) view.findViewById(R.id.fab_new_photo);
        fabNewAlarm = (FloatingActionButton) view.findViewById(R.id.fab_new_alarm);

        fab.setOnClickListener(this);
        fabNewNote.setOnClickListener(this);
        fabNewPhoto.setOnClickListener(this);
        fabNewAlarm.setOnClickListener(this);

    }

    private void showAllFloatingActionButon() {
        fabNewPhoto.show();
        fabNewNote.show();
        fabNewAlarm.show();
    }

    private void hideAllFloatingActionButon() {
        fabNewPhoto.hide();
        fabNewNote.hide();
        fabNewAlarm.hide();
    }

    private void goToW() {
        FrameLayout.LayoutParams paramsW = (FrameLayout.LayoutParams) fabNewAlarm.getLayoutParams();
        goWanimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_go_w);
        paramsW.rightMargin = (int) (fabNewAlarm.getWidth() * 1.8);
        fabNewAlarm.setLayoutParams(paramsW);
        fabNewAlarm.startAnimation(goWanimation);
    }

    private void returnW() {
        FrameLayout.LayoutParams paramsW = (FrameLayout.LayoutParams) fabNewAlarm.getLayoutParams();
        retWanimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_return_w);
        paramsW.rightMargin = (int) (fabNewAlarm.getWidth() * 1.8);
        fabNewAlarm.setLayoutParams(paramsW);
        fabNewAlarm.startAnimation(retWanimation);
    }

    private void goToNW() {
        FrameLayout.LayoutParams paramsNW = (FrameLayout.LayoutParams) fabNewPhoto.getLayoutParams();
        goNWanimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_go_nw);
        paramsNW.rightMargin = (int) (fabNewPhoto.getWidth() * 1.5);
        paramsNW.bottomMargin = (int) (fabNewPhoto.getHeight() * 1.5);
        fabNewPhoto.setLayoutParams(paramsNW);
        fabNewPhoto.startAnimation(goNWanimation);
    }

    private void returnNW() {
        FrameLayout.LayoutParams paramsNW = (FrameLayout.LayoutParams) fabNewPhoto.getLayoutParams();
        retNWanimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_return_nw);
        paramsNW.rightMargin = (int) (fabNewPhoto.getWidth() * 1.5);
        paramsNW.bottomMargin = (int) (fabNewPhoto.getHeight() * 1.5);
        fabNewPhoto.setLayoutParams(paramsNW);
        fabNewPhoto.startAnimation(retNWanimation);
    }

    private void goToN() {
        FrameLayout.LayoutParams paramsW = (FrameLayout.LayoutParams) fabNewNote.getLayoutParams();
        goNanimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_go_n);
        paramsW.bottomMargin = (int) (fabNewNote.getWidth() * 1.8);
        fabNewNote.setLayoutParams(paramsW);
        fabNewNote.startAnimation(goNanimation);
    }

    private void returnN() {
        FrameLayout.LayoutParams paramsW = (FrameLayout.LayoutParams) fabNewNote.getLayoutParams();
        retNanimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_return_n);
        paramsW.bottomMargin = (int) (fabNewNote.getWidth() * 1.8);
        fabNewNote.setLayoutParams(paramsW);
        fabNewNote.startAnimation(retNanimation);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (isFloatingActionButonShow == false) {
                    showAllFloatingActionButon();
                    goToW();
                    goToNW();
                    goToN();
                    isFloatingActionButonShow = true;
                } else {
                    hideAllFloatingActionButon();
                    returnN();
                    returnNW();
                    returnW();
                    isFloatingActionButonShow = false;
                }
                break;

            case R.id.fab_new_note:
                hideAllFloatingActionButon();
                returnN();
                returnNW();
                returnW();
                isFloatingActionButonShow = false;
                break;

            case R.id.fab_new_alarm:
                Toast.makeText(getActivity(), "New alarm click", Toast.LENGTH_SHORT).show();
                hideAllFloatingActionButon();
                returnN();
                returnNW();
                returnW();
                isFloatingActionButonShow = false;
                break;

            case R.id.fab_new_photo:
                Toast.makeText(getActivity(), "New photo click", Toast.LENGTH_SHORT).show();
                hideAllFloatingActionButon();
                returnN();
                returnNW();
                returnW();
                isFloatingActionButonShow = false;
                break;
        }
    }


}