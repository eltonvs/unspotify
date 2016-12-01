package com.imd.lp2.unspotify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imd.lp2.unspotify.R;
import com.imd.lp2.unspotify.model.Music;

import java.util.List;

/**
 * Created by johnnylee on 01/12/16.
 */

public class MusicAdapter extends BaseAdapter {
    private List<Music> listMusics;
    private Context context;
    public MusicAdapter(List<Music> listMusics, Context context) {
        this.listMusics = listMusics;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listMusics.size();
    }

    @Override
    public Object getItem(int i) {
        return listMusics.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_list_music, null);
        TextView txtMusicName = (TextView) view.findViewById(R.id.txtMusicName);

        txtMusicName.setText(listMusics.get(i).getName());
        return view;
    }
}
