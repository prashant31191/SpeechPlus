package com.shavkunov.razvitie.samo;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;
import com.google.android.gms.ads.NativeExpressAdView;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.shavkunov.razvitie.samo.entity.Patter;
import com.shavkunov.razvitie.samo.entity.CardLab;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SPEECH_HOLDER = 0;
    private static final int AD_HOLDER = 1;

    private FragmentActivity fragmentActivity;
    private List<Object> listItems;
    private CardLab cardLab;
    private boolean isDelete;

    public RecyclerViewAdapter(FragmentActivity fragmentActivity, List<Object> listItems,
                               boolean isDelete) {
        this.fragmentActivity = fragmentActivity;
        this.listItems = listItems;
        this.isDelete = isDelete;
        cardLab = new CardLab(fragmentActivity);
    }

    public class SpeechHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_speech)
        ImageView imageSpeech;

        @BindView(R.id.title_speech)
        TextView titleSpeech;

        @BindView(R.id.favorite_button_speech)
        ShineButton favoriteButtonSpeech;

        @BindView(R.id.delete_button_speech)
        ShineButton deleteButtonSpeech;

        public SpeechHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            favoriteButtonSpeech.init(fragmentActivity);
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

                setDeleteButton(speechHolder);
                setImageView(speechHolder, patter);
                speechHolder.titleSpeech.setText(patter.getTitle());
                speechHolder.favoriteButtonSpeech.setChecked(patter.isFavorite());
                speechHolder.favoriteButtonSpeech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        patter.setFavorite(speechHolder.favoriteButtonSpeech.isChecked());
                        cardLab.updateFavorite(patter);
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

    private void setDeleteButton(final SpeechHolder speechHolder) {
        if (isDelete) {
            speechHolder.deleteButtonSpeech.setVisibility(View.VISIBLE);
            speechHolder.deleteButtonSpeech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    speechHolder.deleteButtonSpeech.showAnim();

                    new DroidDialog.Builder(fragmentActivity)
                            .icon(R.drawable.close)
                            .title(fragmentActivity.getString(R.string.delete))
                            .content(fragmentActivity.getString(R.string.delete_dialog_text))
                            .color(ContextCompat.getColor(fragmentActivity, R.color.colorAccent),
                                    ContextCompat.getColor(fragmentActivity, R.color.white),
                                    ContextCompat.getColor(fragmentActivity, R.color.colorAccent))
                            .cancelable(true, true)
                            .animation(AnimUtils.AnimFadeInOut)
                            .positiveButton(fragmentActivity.getString(R.string.ok),
                                    new DroidDialog.onPositiveListener() {
                                        @Override
                                        public void onPositive(Dialog droidDialog) {

                                        }
                                    })
                            .negativeButton(fragmentActivity.getString(R.string.back),
                                    new DroidDialog.onNegativeListener() {
                                        @Override
                                        public void onNegative(Dialog droidDialog) {
                                            droidDialog.cancel();
                                        }
                                    }).show();
                }
            });
        }
    }

    private void setImageView(SpeechHolder speechHolder, Patter patter) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();
        requestOptions.error(R.drawable.no_connection);

        Glide.with(speechHolder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(patter.getImageUrl())
                .into(speechHolder.imageSpeech);
    }
}
