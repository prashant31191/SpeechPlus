package com.shavkunov.razvitie.samo;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.NativeExpressAdView;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.shavkunov.razvitie.samo.entity.Patter;
import com.shavkunov.razvitie.samo.entity.PatterLab;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SPEECH_HOLDER = 0;
    private static final int AD_HOLDER = 1;

    private Context context;
    private List<Object> listItems;

    public RecyclerViewAdapter(Context context, List<Object> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    public class SpeechHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_speech)
        ImageView imageSpeech;

        @BindView(R.id.title_speech)
        TextView titleSpeech;

        @BindView(R.id.favorite_button_speech)
        ShineButton favoriteButtonSpeech;

        public SpeechHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            favoriteButtonSpeech.init((Activity) context);
        }
    }

    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

        public NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = listItems.get(position);

        if (o instanceof Patter) {
            return SPEECH_HOLDER;
        } else {
            return AD_HOLDER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case SPEECH_HOLDER:
            default:
                View speechView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.card_view_speech, viewGroup, false);
                return new SpeechHolder(speechView);
            case AD_HOLDER:
                View adView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.ad_container, viewGroup, false);
                return new NativeExpressAdViewHolder(adView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case SPEECH_HOLDER:
            default:
                final SpeechHolder speechHolder = (SpeechHolder) holder;
                final Patter patter = (Patter) listItems.get(position);

                Glide.with(speechHolder.itemView.getContext())
                        .load(patter.getImageUrl())
                        .into(speechHolder.imageSpeech);
                speechHolder.titleSpeech.setText(patter.getTitle());
                speechHolder.favoriteButtonSpeech.setChecked(patter.isFavorite());
                speechHolder.favoriteButtonSpeech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        patter.setFavorite(speechHolder.favoriteButtonSpeech.isChecked());
                        PatterLab.getInstance(context).updateFavorite(patter);
                    }
                });
                break;
            case AD_HOLDER:
                NativeExpressAdViewHolder adViewHolder =
                        (NativeExpressAdViewHolder) holder;
                NativeExpressAdView adView = (NativeExpressAdView)
                        listItems.get(position);

                ViewGroup adCardView = (ViewGroup) adViewHolder.itemView;

                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                adCardView.addView(adView);
                break;
        }
    }
}
