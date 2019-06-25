package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slurp.blackjackandroid.R;

import java.util.List;


public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder> {
    private List<ScoreListItem> listData;
    private Context context;

    public static class ScoreViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mUserNameTextView, mUserRankTextView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.user_picture);
//            mUserNameTextView = itemView.findViewById(R.id.username);
//            mUserRankTextView = itemView.findViewById(R.id.rank_number);

        }
    }

    public ScoreListAdapter(Context context, List<ScoreListItem> listData){
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        return null;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_score_list_item,
                viewGroup, false);


        ScoreViewHolder scoreViewHolder = new ScoreViewHolder(v);
        return scoreViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder viewHolder, int i) {
        ScoreListItem scoreListItem = this.listData.get(i);
        viewHolder.mImageView.setImageBitmap(scoreListItem.getImageBitmap());
//        viewHolder.mUserRankTextView.setText(Integer.toString(scoreListItem.getUserRankNumber()));
//        viewHolder.mUserNameTextView.setText(scoreListItem.getUserName());
        viewHolder.itemView.setHasTransientState(true); // is this needed?
        ViewGroup v = (ViewGroup) viewHolder.itemView;

        SpeechScrollerView speechScrollerView = new SpeechScrollerView(this.context);

        ViewGroup userNameContainerView = v.findViewById(R.id.username_container);
        userNameContainerView.addView(speechScrollerView);
        speechScrollerView.drawDialogueBox(scoreListItem.getUserName());
        viewHolder.itemView.setHasTransientState(false); // is this needed?
    }

    @Override
    public int getItemCount() {
        return this.listData.size();
    }
}
