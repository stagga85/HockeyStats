package com.stats.shottracker.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stats.shottracker.R;
import com.stats.shottracker.activities.GameActivity;
import com.stats.shottracker.models.Game;
import com.stats.shottracker.models.Player;
import com.stats.shottracker.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GamesFragment extends Fragment {

    private View v;
    private List<Game> games;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private GameRecyclerAdapter adapter;

    public static final String GAME_ID = "GAME_ID";

    public GamesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_games, container, false);

        // Setup recycler for teams.
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerviewGames);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Setup team list adapter.
        games = new ArrayList<>();
        adapter = new GameRecyclerAdapter(games);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new GameRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClickEdit(View itemView, int position) {
                Intent i = new Intent(getContext(), GameActivity.class);

                // Passing over the game ID.
                i.putExtra(GAME_ID, games.get(position).id);
                startActivity(i);
            }

            @Override
            public void onItemClickDelete(View itemView, int position) {
                showSnackbar("Remove Game");
                new DatabaseHelper(getContext()).removeGame(games.get(position));
                games.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        initializeSwipeToRefresh();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillGameRecyclerAdapter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillGameRecyclerAdapter();
    }

    private void initializeSwipeToRefresh () {
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillGameRecyclerAdapter();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void fillGameRecyclerAdapter() {
        games.clear();
        List<Game> allGames = new DatabaseHelper(getContext()).getAllGames();

        for (Game game : allGames) {
            games.add(game);
        }

        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);

        //showSnackbar("Player data loaded.");
    }


    private void showSnackbar (String message) {
        Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
    }
}
