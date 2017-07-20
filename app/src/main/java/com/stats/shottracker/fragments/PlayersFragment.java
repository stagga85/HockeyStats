package com.stats.shottracker.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.stats.shottracker.R;
import com.stats.shottracker.models.Player;
import com.stats.shottracker.models.Team;
import com.stats.shottracker.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static android.R.attr.category;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersFragment extends Fragment {

    private View v;

    private List<Player> players;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    PlayerRecyclerAdapter adapter;

    public PlayersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_players, container, false);

        // Setup recycler for teams.
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerviewPlayers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Setup team list adapter.
        players = new ArrayList<>();
        adapter = new PlayerRecyclerAdapter(players);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new SlideInLeftAnimator());

        adapter.setOnItemClickListener(new PlayerRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickEdit(View itemView, int position) {
                showSnackbar(players.get(position).firstName);
            }

            @Override
            public void onItemClickDelete(View itemView, int position) {
                showSnackbar("Remove - " + players.get(position).firstName);
                new DatabaseHelper(getContext()).removePlayer(players.get(position));
                players.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        initializeSwipeToRefresh();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillPlayerRecyclerAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        fillPlayerRecyclerAdapter();
    }

    private void initializeSwipeToRefresh () {
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillPlayerRecyclerAdapter();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void fillPlayerRecyclerAdapter() {
        players.clear();
        List<Player> allPlayers = new DatabaseHelper(getContext()).getAllPlayers();

        for (Player player : allPlayers) {
            players.add(player);
        }

        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);

        //showSnackbar("Player data loaded.");
    }


    private void showSnackbar (String message) {
        Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
    }
}
