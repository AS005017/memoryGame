package com.labs.lab1;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private Integer mCols, mRows;
    private ArrayList<String> arrPict; // массив картинок
    private String PictureCollection; // Префикс набора картинок
    private Resources mRes; // Ресурсы приложения
    private enum Status {CARD_OPEN, CARD_CLOSE, CARD_DELETE};//массив с вариациями состояний
    private ArrayList<Status> arrStatus; // состояние ячеек

    public CustomAdapter(Context context, int cols, int rows)
    {
        mContext = context;
        mCols = cols;
        mRows = rows;
        arrPict = new ArrayList<>();
        arrStatus = new ArrayList<>();
        PictureCollection = "animal";
        mRes = mContext.getResources();

        makePictArray ();
        closeAllCards();
    }

    private void closeAllCards () {
        arrStatus.clear();
        for (int i = 0; i < mCols * mRows; i++)
            arrStatus.add(Status.CARD_CLOSE);
    }

    private void clearAllCards () {
        arrStatus.clear();
        for (int i = 0; i < mCols * mRows; i++)
            arrStatus.add(Status.CARD_DELETE);
    }

    private void makePictArray () {
        arrPict.clear();
        for (int i = 0; i < ((mCols * mRows) / 2); i++)
        {
            arrPict.add (PictureCollection + Integer.toString (i));
            arrPict.add (PictureCollection + Integer.toString (i));
        }
        Collections.shuffle(arrPict);
    }
    public void checkOpenCards() {
        int first = arrStatus.indexOf(Status.CARD_OPEN);
        int second = arrStatus.lastIndexOf(Status.CARD_OPEN);
        if (first == second)
            return;
        if (arrPict.get(first).equals (arrPict.get(second)))
        {
            arrStatus.set(first, Status.CARD_DELETE);
            arrStatus.set(second, Status.CARD_DELETE);
        }
        else
        {
            arrStatus.set(first, Status.CARD_CLOSE);
            arrStatus.set(second, Status.CARD_CLOSE);
        }
    }

    public void openCard(int position) {
        if (arrStatus.get(position) != Status.CARD_DELETE)
            arrStatus.set(position, Status.CARD_OPEN);

        notifyDataSetChanged();
    }

    public boolean checkGameOver() {
        if (arrStatus.indexOf(Status.CARD_CLOSE) < 0) {
            clearAllCards();
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return mCols * mRows;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView view;

        if (convertView == null) {
            view = new ImageView(mContext);
            view.setLayoutParams(new GridView.LayoutParams(300, 300));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
            view = (ImageView)convertView;
        switch (arrStatus.get(position))
        {
            case CARD_OPEN:
                int drawableId = mRes.getIdentifier(arrPict.get(position), "drawable", mContext.getPackageName());
                view.setImageResource(drawableId);

                break;
            case CARD_CLOSE:
                view.setImageResource(R.drawable.closecard);
                break;
            case CARD_DELETE:
                view.setVisibility(View.INVISIBLE);
                break;
        }

        return view;
    }
}
