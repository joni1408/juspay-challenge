package com.prempal.juspay_challenge;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ArticleDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    public ArticleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            String id = getArguments().getString(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Title");
            }

            ImageView backdrop = (ImageView) activity.findViewById(R.id.backdrop);
            if (backdrop != null) {
                Picasso.with(activity).load("http://blogs-images.forbes.com/scottmendelson/files/2015/04/deadpool-movie-costume-pic-2.jpg")
                        .into(backdrop);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_detail, container, false);
        ((TextView) rootView.findViewById(R.id.article_detail)).setText("News article goes here");
        return rootView;
    }
}
