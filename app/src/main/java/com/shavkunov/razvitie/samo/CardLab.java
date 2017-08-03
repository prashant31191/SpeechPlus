package com.shavkunov.razvitie.samo;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class CardLab {

    private List<Patter> cards;

    private static CardLab cardLab;

    public static CardLab getInstance(Context context) {
        if (cardLab == null) {
            cardLab = new CardLab(context);
        }

        return cardLab;
    }

    private CardLab(Context context) {
        cards = new ArrayList<>();
        cards.add(new Patter(1,
                "https://oficinadecontos2013.files.wordpress.com/2013/12/chuva-agnes.jpg",
                "У нас во дворе-подворье погода размокропогодилась"));
        cards.add(new Patter(2, "http://www.walesoncraic.com/wp-content/uploads/2016/08/Wasp.jpg",
                "У осы не усы, не усища, а усики"));
    }

    public List<Patter> getCards() {
        return cards;
    }
}
