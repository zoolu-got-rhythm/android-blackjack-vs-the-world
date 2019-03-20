package com.example.slurp.blackjackandroid.view;

import com.example.slurp.blackjackandroid.model.blackjack.Game;

public interface ViewComponent {
    boolean shouldComponentUpdate(Game newModelState);
}
