package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.view.BubbleSpeechView.FullyRoundedBubbleView;
import com.example.slurp.blackjackandroid.view.BubbleSpeechView.DefaultSpeechBubbleView;
import com.example.slurp.blackjackandroid.view.BubbleSpeechView.UserRankBubbleView;
import com.example.slurp.blackjackandroid.view.BubbleSpeechView.UserScoreBubbleView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder> {
    private List<ScoreListItem> listData;
    private Context context;

    public class ScoreViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public DefaultSpeechBubbleView mDefaultSpeechBubbleViewUserName;
        public UserRankBubbleView mUserRankBubbleView;
        public UserScoreBubbleView mUserScoreTimeBubbleView, mUserScoreDateBubbleView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.user_picture);

            // viewGroup is super class/base class of linearLayout I believe

            this.mDefaultSpeechBubbleViewUserName = new DefaultSpeechBubbleView(context);
            ViewGroup userNameContainerView = itemView.findViewById(R.id.username_container);
            userNameContainerView.addView(this.mDefaultSpeechBubbleViewUserName);

            ViewGroup userRankContainerView = itemView.findViewById(R.id.userrank_container);
            this.mUserRankBubbleView = new UserRankBubbleView(context);
            userRankContainerView.addView(this.mUserRankBubbleView);

            this.mUserScoreTimeBubbleView = new UserScoreBubbleView(context);
            ViewGroup userScoreContainer = itemView.findViewById(R.id.userscore_score_container);
            userScoreContainer.addView(this.mUserScoreTimeBubbleView);

            this.mUserScoreDateBubbleView= new UserScoreBubbleView(context);
            ViewGroup userScoreDateContainer = itemView.findViewById(R.id.userscore_date_container);
            userScoreDateContainer.addView(this.mUserScoreDateBubbleView);
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

//        viewHolder.userNameContainerView.setBackgroundColor(i % 2 == 0 ? Color.BLACK : Color.DKGRAY);
        viewHolder.mImageView.setImageBitmap(scoreListItem.getImageBitmap());

        viewHolder.mDefaultSpeechBubbleViewUserName.drawDialogueBox(this.listData.get(i).getUserName(),

                false);
        viewHolder.mUserScoreTimeBubbleView.drawDialogueBox("01:05:45",
                false);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String formattedDate = dateFormat.format(new Date());
        viewHolder.mUserScoreDateBubbleView.drawDialogueBox(formattedDate, false);

        viewHolder.mUserRankBubbleView.drawDialogueBox(
                '#' + Integer.toString(scoreListItem.getUserRankNumber()),
                false
        );

        Log.d("scoreListAdapter", "onBindViewHolder: " + i);

//        viewHolder.itemView.setHasTransientState(false); // is this needed?
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ScoreViewHolder holder) {
        super.onViewAttachedToWindow(holder);

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ScoreViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        holder.mSpeechScrollerView.stopDrawDialogueBox();
//            holder.userNameContainerView.getChildCount());
//        if(holder.userNameContainerView.getChildCount() > 0)
//            holder.userNameContainerView.removeViewAt(0);
//        if(holder.userNameContainerView.getChildCount() > 0)
//            holder.userNameContainerView.removeAllViews();
    }



    @Override
    public int getItemCount() {
        return this.listData.size();
    }
}
