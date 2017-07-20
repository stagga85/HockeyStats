package com.stats.shottracker.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.stats.shottracker.R;
import com.stats.shottracker.models.Game;
import com.stats.shottracker.models.Player;

public class GameRecyclerAdapter extends RecyclerView.Adapter<GameRecyclerAdapter.GameEntry> {

    private List<Game> games;

    private OnItemClickListener listener;

    GameRecyclerAdapter(List<Game> games) {
        this.games = games;
    }

    public interface OnItemClickListener {
        void onItemClickEdit(View itemView, int position);
        void onItemClickDelete(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public GameEntry onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.game_card, viewGroup, false);

        return new GameEntry(v);
    }

    @Override
    public void onBindViewHolder(GameEntry viewHolder, int i) {
        Game game = games.get(i);
        viewHolder.gameNameTextView.setText(game.home.abbreviation + " vs. " + game.away.abbreviation);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String date = formatter.format(game.date);
        viewHolder.gameDateTextView.setText(date);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class GameEntry extends RecyclerView.ViewHolder {

        private final TextView gameNameTextView;
        private final TextView gameDateTextView;

        private Button gameEditButton;
        private android.widget.Button gameDeleteButton;

        GameEntry(View v) {
            super(v);
            gameNameTextView = (TextView)v.findViewById(R.id.game_name);
            gameDateTextView = (TextView)v.findViewById(R.id.game_date);
            gameEditButton = (Button)v.findViewById(R.id.edit_game_button);
            gameDeleteButton = (Button)v.findViewById(R.id.delete_game_button);


            gameEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClickEdit(itemView, position);
                        }
                    }
                }
            });

            gameDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClickDelete(itemView, position);
                        }
                    }
                }
            });

        }
    }

}