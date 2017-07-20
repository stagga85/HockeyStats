package com.stats.shottracker.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.stats.shottracker.R;

public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter.TeamEntry> {

    private List<String> mItems;

    TeamRecyclerAdapter(List<String> items) {
        mItems = items;
    }

    @Override
    public TeamEntry onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);

        return new TeamEntry(v);
    }

    @Override
    public void onBindViewHolder(TeamEntry viewHolder, int i) {
        String item = mItems.get(i);
        viewHolder.mTextView.setText(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class TeamEntry extends RecyclerView.ViewHolder {

        private int teamImageId;
        private final TextView mTextView;

        TeamEntry(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.list_item);
        }
    }

}