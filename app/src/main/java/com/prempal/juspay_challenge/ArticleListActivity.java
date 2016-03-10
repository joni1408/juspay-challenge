package com.prempal.juspay_challenge;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArticleListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private boolean mTwoPane;
    private RecyclerView mRecyclerView;
    private List<Article> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra("title"));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.article_list);

        if (findViewById(R.id.article_detail_container) != null) {
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        new GetArticlesTask().execute(getIntent().getStringExtra("link"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article_list_activity, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Article> filteredModelList = filter(mList, newText);
        ((ArticlesAdapter) mRecyclerView.getAdapter()).update(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    private List<Article> filter(List<Article> articles, String query) {
        query = query.toLowerCase();

        final List<Article> filteredModelList = new ArrayList<>();
        for (Article article : articles) {
            final String text = article.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(article);
            }
        }
        return filteredModelList;
    }

    public class ArticlesAdapter
            extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

        List<Article> articles;

        ArticlesAdapter(List<Article> articles) {
            this.articles = articles;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.mContentView.setText(articles.get(position).getTitle());
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link = articles.get(position).getLink();
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ArticleDetailFragment.ARG_ITEM_URL, link);
                        ArticleDetailFragment fragment = new ArticleDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.article_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ArticleDetailActivity.class);
                        intent.putExtra(ArticleDetailFragment.ARG_ITEM_URL, link);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return articles.size();
        }

        public void update(List<Article> articles) {
            this.articles = articles;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    private class GetArticlesTask extends AsyncTask<String, Void, List> {

        @Override
        protected List doInBackground(String... urls) {
            try {

                RSSParser parser = new RSSParser();

                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                return parser.parse(conn.getInputStream());

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List articles) {
            if (articles != null) {
                mList = articles;
                mRecyclerView.setAdapter(new ArticlesAdapter(mList));
            }
        }
    }
}