package com.shavkunov.razvitie.samo;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class CardLab {

    private List<CardData> cards;

    private static CardLab cardLab;

    public static CardLab getInstance(Context context) {
        if (cardLab == null) {
            cardLab = new CardLab(context);
        }

        return cardLab;
    }

    private CardLab(Context context) {
        cards = new ArrayList<>();
        cards.add(new CardData(R.drawable.bear,
                context.getString(R.string.test), false));
        cards.add(new CardData(R.drawable.bear,
                context.getString(R.string.test1), false));
        cards.add(new CardData(R.drawable.bear,
                context.getString(R.string.test2), false));
    }

    public List<CardData> getCards() {
        return cards;
    }
}
