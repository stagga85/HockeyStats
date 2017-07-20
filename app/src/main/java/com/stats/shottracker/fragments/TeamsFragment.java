package com.stats.shottracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.stats.shottracker.R;
import com.stats.shottracker.api.ApiClient;
import com.stats.shottracker.models.Team;
import com.stats.shottracker.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamsFragment extends Fragment {

    private static final String TAB_POSITION = "tab_position";
    public static final String BASE_URL = "http://andrewjstagg.com:3000/";

    private final String TAG = "TeamsFragment";

    private List<String> teamNames;

    private ApiClient client;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View v;

    RecyclerView recyclerView;
    TeamRecyclerAdapter adapter;

    public TeamsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        fillTeamRecyclerAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v =  inflater.inflate(R.layout.fragment_teams, container, false);

        // Setup recycler for teams.
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Setup team list adapter.
        teamNames = new ArrayList<>();
        adapter = new TeamRecyclerAdapter(teamNames);
        recyclerView.setAdapter(adapter);


        initializeApi();

        initializeSwipeToRefreash();

        // Initial team list populate.
        //populateTeamRecyclerAdapter();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillTeamRecyclerAdapter();
    }

    private void initializeSwipeToRefreash () {
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillTeamRecyclerAdapter();

                //Log.d(TAG, getAllTeams().toString());

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void fillTeamRecyclerAdapter() {
        teamNames.clear();
        List<Team> teams = new DatabaseHelper(getContext()).getAllTeams();

        for (Team team : teams) {
            teamNames.add(team.teamName);
        }

        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);

        //showSnackbar("Team data loaded.");
    }

    private void initializeApi () {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        client = retrofit.create(ApiClient.class);
    }

//    private void populateTeamRecyclerAdapter () {
//        Call<List<Team>> call = client.listAllTeams();
//        call.enqueue(new Callback<List<Team>>() {
//            @Override
//            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
//
//                teamNames.clear();
//
//                int statusCode = response.code();
//                List<Team> teams = response.body();
//                //showConfirmation("Status Code: " + statusCode);
//
//                for (Team team : teams) {
//                    teamNames.add(team.getName());
//                }
//
//                adapter.notifyDataSetChanged();
//                mSwipeRefreshLayout.setRefreshing(false);
//
//                showSnackbar("Team data loaded.");
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Team>> call, Throwable t) {
//                Log.e(TAG, t.toString());
//            }
//        });
//    }

    private void showConfirmation (String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar (String message) {
        Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
    }

}
