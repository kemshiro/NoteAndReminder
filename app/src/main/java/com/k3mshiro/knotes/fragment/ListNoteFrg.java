package com.k3mshiro.knotes.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.k3mshiro.knotes.R;
import com.k3mshiro.knotes.activity.MainAct;
import com.k3mshiro.knotes.adapter.NoteAdapter;
import com.k3mshiro.knotes.customview.VerticalItemSpace;
import com.k3mshiro.knotes.dao.NoteDAO;
import com.k3mshiro.knotes.dto.NoteDTO;

import java.util.List;

/**
 * Created by k3mshiro on 7/21/17.
 */

public class ListNoteFrg extends Fragment implements View.OnClickListener, NoteAdapter.OnItemClickListener {
    private static final String TAG = ListNoteFrg.class.getName();
    private static final int VERTICAL_ITEM_SPACE = 30;

    private View view;
    private RecyclerView rvList;
    private FloatingActionButton fab;
    private FloatingActionButton fabNewNote;
    private FloatingActionButton fabNewPhoto;
    private FloatingActionButton fabNewAlarm;
    private Animation goNanimation, goNWanimation, goWanimation, retNanimation, retNWanimation, retWanimation;
    private MainAct mainAct;

    private NoteAdapter mNoteAdapter;
    private List<NoteDTO> noteDTOs;
    private NoteDAO noteDAO;
    private boolean isFloatingActionButonShow = false;

    private FragmentManager fragmentManager;
    private Fragment createNoteFrg;
    private IFragmentConnection connection;


    public interface IFragmentConnection {
        void sendDataToEditNoteFrg(NoteDTO note);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        connection = (IFragmentConnection) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        initViews();
        initNotes();
        return view;
    }


    /***************** Animation Setting **********************************/
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


    /********************** View Setting ********************/

    private void initViews() {

        mainAct = (MainAct) getActivity();

        rvList = (RecyclerView) view.findViewById(R.id.cardList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(llm);

        rvList.setHasFixedSize(true); // nang cao hieu suat khi cac item cung do rong va chieu cao
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

    private void initNotes() {
        noteDAO = mainAct.getNoteDAO();
        noteDTOs = noteDAO.getListNotes();

        mNoteAdapter = new NoteAdapter(getActivity(), noteDTOs);
        rvList.setAdapter(mNoteAdapter);

        mNoteAdapter.setOnItemClickListener(this);
        mNoteAdapter.notifyDataSetChanged();

        initSwipe();
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(getActivity(), "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int position = viewHolder.getAdapterPosition();
                switch (swipeDirection) {
                    case ItemTouchHelper.LEFT:
                        //Remove swiped item from list and notify the RecyclerView
                        deleteNote(position);
                        break;
                    case ItemTouchHelper.RIGHT:
                        //Edit swiped item from list and notify the RecyclerView
                        NoteDTO note = noteDTOs.get(position);
                        connection.sendDataToEditNoteFrg(note);
                    default:
                        break;
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvList);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        NoteDTO note = noteDTOs.get(position);
        Log.d(TAG, "ID: " + note.getId() + "\n"
                + "Title: " + note.getTitle() + "\n");

        connection.sendDataToEditNoteFrg(note);
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

                showCreateNoteScreen();
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

    private void showCreateNoteScreen() {
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        createNoteFrg = new CreateNoteFrg();

        fragmentTransaction.replace(R.id.act_main, createNoteFrg, CreateNoteFrg.class.getName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void deleteNote(int position) {
        NoteDTO deletedNote = noteDTOs.get(position);
        boolean result = noteDAO.deleteNote(deletedNote);
        noteDTOs.remove(position);
        mNoteAdapter.notifyDataSetChanged();
        if (result == true) {
            Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
        }
    }

}
