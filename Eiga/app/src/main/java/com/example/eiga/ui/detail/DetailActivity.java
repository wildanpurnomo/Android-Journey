package com.example.eiga.ui.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.eiga.R;
import com.example.eiga.model.Genre;
import com.example.eiga.model.Show;
import com.example.eiga.ui.favorite.LoadFavoriteCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.view.View.VISIBLE;
import static com.example.eiga.db.DatabaseContract.FavoriteColumns.CATEGORY;
import static com.example.eiga.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.eiga.db.DatabaseContract.FavoriteColumns.IDTMDB;
import static com.example.eiga.db.DatabaseContract.FavoriteColumns.IMGURL;
import static com.example.eiga.db.DatabaseContract.FavoriteColumns.RATE;
import static com.example.eiga.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.example.eiga.helper.MappingHelper.mapCursorToArrayList;

public class DetailActivity extends AppCompatActivity implements LoadFavoriteCallback {
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_INDEX = "extra_index";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_FAV = "extra_fav";
    private static final String EXTRA_INITIAL_FAVORITE = "extra_init_fav";


    public static final int REQUEST_CODE = 100;
    public static final int RESULT_ADD = 101;
    public static final int RESULT_REMOVE = 102;

    private MenuItem favoriteChecked, favoriteUnchecked;
    private ProgressBar pb;
    private ScrollView svContainer;
    private ImageView imgPoster;
    private TextView tvTitle, tvReleaseDate, tvGenres, tvRating, tvLanguage, tvPopularity, tvOverview;

    private Show toBeAdded = new Show();
    private boolean isFavorite = false;
    private boolean initialIsFavorite = false;
    private int position;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(getResources().getString(R.string.detail_title));

        pb = findViewById(R.id.pb_detail);
        svContainer = findViewById(R.id.sv_content_detail);
        imgPoster = findViewById(R.id.img_detail_poster);
        tvTitle = findViewById(R.id.tv_detail_title_value);
        tvReleaseDate = findViewById(R.id.tv_detail_release_date_value);
        tvGenres = findViewById(R.id.tv_detail_genre_value);
        tvRating = findViewById(R.id.tv_detail_rating_value);
        tvLanguage = findViewById(R.id.tv_detail_language_value);
        tvPopularity = findViewById(R.id.tv_detail_popularity_value);
        tvOverview = findViewById(R.id.tv_detail_overview_value);

        HandlerThread handlerThread = new HandlerThread("IsFavoriteObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        IsFavoriteObserver isFavoriteObserver = new IsFavoriteObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, isFavoriteObserver);

        position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        int callerIndex = getIntent().getIntExtra(EXTRA_INDEX, 0);
        createBackIcon();

        DetailViewModel detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.setDetailShow(callerIndex, getIntent().getLongExtra(EXTRA_ID, 0));
        displayLoadingBar(true);

        detailViewModel.getDetailShow().observe(this, new Observer<Show>() {
            @Override
            public void onChanged(@Nullable Show show) {
                if (show != null) {
                    setViewImage(show.getPosterPath());
                    setViewTitle(show.getTitle());
                    setViewReleaseDate(show.getReleaseDate());
                    setViewGenre(show.getGenres());
                    setViewRating(show.getVoteAverage());
                    setViewLanguage(show.getLanguage());
                    setViewPopularity(show.getPopularity());
                    setViewOverview(show.getOverview());

                    if (show.getPosterPath() == null) {
                        show.setPosterPath("N/A");
                    }

                    if (show.getVoteAverage() == null) {
                        show.setVoteAverage(0.0);
                    }

                    if (getIntent().getIntExtra(EXTRA_INDEX, 0) == 0) {
                        show.setType("movie");
                    } else {
                        show.setType("tv");
                    }

                    toBeAdded = show;
                }

                displayLoadingBar(false);
            }
        });

        determineFavorite(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav_menu, menu);

        favoriteUnchecked = menu.findItem(R.id.fav_false);
        favoriteChecked = menu.findItem(R.id.fav_true);

        if (isFavorite) {
            favoriteUnchecked.setVisible(false);
            favoriteChecked.setVisible(true);
        } else {
            favoriteUnchecked.setVisible(true);
            favoriteChecked.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (initialIsFavorite != isFavorite) {
                Intent resultIntent = new Intent();
                if (isFavorite) {
                    resultIntent.putExtra(EXTRA_FAV, toBeAdded);
                    setResult(RESULT_ADD, resultIntent);
                } else {
                    resultIntent.putExtra(EXTRA_POSITION, position);
                    setResult(RESULT_REMOVE, resultIntent);
                }
            }
            finish();
        } else if (item.getItemId() == R.id.fav_false) {
            isFavorite = true;
            favoriteUnchecked.setVisible(false);
            favoriteChecked.setVisible(true);
            invalidateOptionsMenu();

            if (toBeAdded != null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(IDTMDB, toBeAdded.getId());
                contentValues.put(IMGURL, toBeAdded.getPosterPath());
                contentValues.put(TITLE, toBeAdded.getTitle());
                contentValues.put(CATEGORY, toBeAdded.getType());
                contentValues.put(RATE, toBeAdded.getVoteAverage());

                getContentResolver().insert(CONTENT_URI, contentValues);
                Toast.makeText(this, getResources().getString(R.string.add_fav), Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.fav_true) {
            isFavorite = false;
            favoriteUnchecked.setVisible(true);
            favoriteChecked.setVisible(false);
            invalidateOptionsMenu();

            if (getIntent().getData() != null) {
                getContentResolver().delete(getIntent().getData(), null, null);
                Toast.makeText(this, getResources().getString(R.string.remove_fav), Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (initialIsFavorite != isFavorite) {
            Intent resultIntent = new Intent();
            if (isFavorite) {
                resultIntent.putExtra(EXTRA_FAV, toBeAdded);
                setResult(RESULT_ADD, resultIntent);
            } else {
                resultIntent.putExtra(EXTRA_POSITION, position);
                setResult(RESULT_REMOVE, resultIntent);
            }
        }
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_INITIAL_FAVORITE, initialIsFavorite);
    }

    private void setViewOverview(String overview) {
        tvOverview.setText(overview);
    }

    private void setViewPopularity(Double popularity) {
        tvPopularity.setText(String.valueOf(popularity));
    }

    private void setViewLanguage(String language) {
        tvLanguage.setText(language);
    }

    private void setViewRating(Double voteAverage) {
        tvRating.setText(String.valueOf(voteAverage));
    }

    private void setViewGenre(ArrayList<Genre> genres) {
        if (genres != null) {
            StringBuilder setThisToGenre = new StringBuilder();
            for (Genre item : genres) {
                setThisToGenre.append(item.getName()).append("\n\n");
            }

            tvGenres.setText(setThisToGenre);
        }

    }

    private void setViewReleaseDate(String releaseDate) {
        tvReleaseDate.setText(releaseDate);
    }

    private void setViewTitle(String title) {
        tvTitle.setText(title);
    }

    private void setViewImage(String posterPath) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + posterPath).apply(options).into(imgPoster);
    }

    private void createBackIcon() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void displayLoadingBar(boolean isLoading) {
        if (isLoading) {
            pb.setVisibility(VISIBLE);
            svContainer.setVisibility(View.GONE);

        } else {
            pb.setVisibility(View.GONE);
            svContainer.setVisibility(VISIBLE);
            svContainer.setVisibility(VISIBLE);
        }
    }

    private void determineFavorite(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            new LoadFavoriteAsync(this, this).execute();
        } else {
            initialIsFavorite = savedInstanceState.getBoolean(EXTRA_INITIAL_FAVORITE);
        }
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Cursor shows) {
        ArrayList<Show> listShowsInDB = mapCursorToArrayList(shows);
        for (Show item : listShowsInDB) {
            if (item.getId() == getIntent().getLongExtra(EXTRA_ID, 0)) {
                isFavorite = true;
                initialIsFavorite = true;
                break;
            }
        }
    }

    private static class LoadFavoriteAsync extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteCallback> callbackWeakReference;

        public LoadFavoriteAsync(Context context, LoadFavoriteCallback loadFavoriteCallback) {
            weakContext = new WeakReference<>(context);
            callbackWeakReference = new WeakReference<>(loadFavoriteCallback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callbackWeakReference.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        protected void onPostExecute(Cursor shows) {
            super.onPostExecute(shows);
            callbackWeakReference.get().postExecute(shows);
        }
    }

    public static class IsFavoriteObserver extends ContentObserver {
        final Context context;

        public IsFavoriteObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavoriteAsync(context, (LoadFavoriteCallback) context).execute();
        }
    }
}
