package com.stats.shottracker.fragments;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.stats.shottracker.R;
import com.stats.shottracker.models.Player;

public class PlayerRecyclerAdapter extends RecyclerView.Adapter<PlayerRecyclerAdapter.PlayerEntry> {

    private List<Player> players;

    private OnItemClickListener listener;

    PlayerRecyclerAdapter(List<Player> players) {
        this.players = players;
    }

    public interface OnItemClickListener {
        void onItemClickEdit(View itemView, int position);
        void onItemClickDelete(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public PlayerEntry onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_card, viewGroup, false);

        return new PlayerEntry(v);
    }

    @Override
    public void onBindViewHolder(PlayerEntry viewHolder, int i) {
        Player player = players.get(i);
        viewHolder.playerName.setText(player.firstName + " " + player.lastName);
        viewHolder.playerTeam.setText(player.team.teamName);
        viewHolder.playerPhoto.setImageResource(R.drawable.holder);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class PlayerEntry extends RecyclerView.ViewHolder {

        private ImageView playerPhoto;
        private TextView playerName;
        private TextView playerTeam;
        private Button playerEditButton;
        private Button playerDeleteButton;

        PlayerEntry(View v) {
            super(v);
            playerName = (TextView)v.findViewById(R.id.person_name);
            playerTeam = (TextView)v.findViewById(R.id.person_team);
            playerPhoto = (ImageView)v.findViewById(R.id.person_photo);
            playerEditButton = (Button)v.findViewById(R.id.edit_player_button);
            playerDeleteButton = (Button)v.findViewById(R.id.delete_player_button);

            playerEditButton.setOnClickListener(new View.OnClickListener() {
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

            playerDeleteButton.setOnClickListener(new View.OnClickListener() {
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