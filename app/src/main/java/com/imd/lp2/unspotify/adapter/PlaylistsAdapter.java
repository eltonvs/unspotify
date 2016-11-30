package com.imd.lp2.unspotify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imd.lp2.unspotify.R;
import com.imd.lp2.unspotify.model.Playlist;

import java.util.List;

/**
 * Created by johnnylee on 30/11/16.
 */

public class PlaylistsAdapter extends BaseAdapter{
    private List<Playlist> listPlaylist;
    private Context context;
    public PlaylistsAdapter(List<Playlist> listPlaylist, Context context) {
        this.listPlaylist = listPlaylist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listPlaylist.size();
    }

    @Override
    public Object getItem(int i) {
        return listPlaylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_list_playlists, null);
        TextView txtPlaylistName = (TextView) view.findViewById(R.id.txtPlaylistName);
        TextView txtUserPlaylist = (TextView) view.findViewById(R.id.txtUserPlaylist);

        txtPlaylistName.setText(listPlaylist.get(i).getName());
        txtUserPlaylist.setText(listPlaylist.get(i).getUserName());
        return view;
    }
}
