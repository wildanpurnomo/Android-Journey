package com.example.eiga.ui.favorite;


import android.app.SearchManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.eiga.R;
import com.example.eiga.model.Show;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.eiga.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.eiga.helper.MappingHelper.mapCursorToArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteDisplayer extends Fragment implements LoadFavoriteCallback, SearchView.OnQueryTextListener {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String EXTRA_STATE = "extra_state";

    private FavoriteAdapter favoriteAdapter = new FavoriteAdapter();
    private ProgressBar pbFavorit;
    private Toolbar toolbarSearch;

    private DataObserver dataObserver;

    private int indexPage;

    public FavoriteDisplayer() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            indexPage = getArguments().getInt(ARG_SECTION_NUMBER, 0);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_favorite_displayer, container, false);

        toolbarSearch = root.findViewById(R.id.toolbar_search);
        pbFavorit = root.findViewById(R.id.pb_fav);
        RecyclerView rvFavorit = root.findViewById(R.id.rv_favorit);

        InitToolbarSearch();

        favoriteAdapter.setActivity(getActivity());
        favoriteAdapter.setIndexPage(indexPage);

        rvFavorit.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorit.setAdapter(favoriteAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        dataObserver = new DataObserver(handler, getContext(), this);

        if (savedInstanceState == null) {
            new LoadFavoriteAsync(getContext(), this).execute();
        } else {
            ArrayList<Show> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                favoriteAdapter.setListShows(list);
            }
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, favoriteAdapter.getListShows());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dataObserver != null && getContext() != null) {
            getContext().getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dataObserver != null && getContext() != null) {
            getContext().getContentResolver().unregisterContentObserver(dataObserver);
        }
    }

    @Override
    public void preExecute() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pbFavorit.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void postExecute(Cursor shows) {
        pbFavorit.setVisibility(View.GONE);

        ArrayList<Show> cleanShows = mapCursorToArrayList(shows);
        favoriteAdapter.setListShows(cleanShows);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        favoriteAdapter.getFilter().filter(s);
        return true;
    }

    private void InitToolbarSearch() {
        toolbarSearch.inflateMenu(R.menu.search_menu);
        if (indexPage == 0) {
            toolbarSearch.setTitle(getResources().getString(R.string.search_fav_mov));
        } else {
            toolbarSearch.setTitle(getResources().getString(R.string.search_fav_tv));
        }

        final MenuItem itemSearch = toolbarSearch.getMenu().getItem(0);
        itemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        toolbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSearch.expandActionView();
            }
        });

        if (getActivity() != null) {
            SearchView searchView = (SearchView) itemSearch.getActionView();
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

            if (searchManager != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                searchView.setOnQueryTextListener(this);
                searchView.clearFocus();
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
            return weakContext.get().getContentResolver().query(
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

    public static class DataObserver extends ContentObserver {
        final Context context;
        final LoadFavoriteCallback callback;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
            this.callback = null;
        }

        public DataObserver(Handler handler, Context context, LoadFavoriteCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (callback == null) {
                new LoadFavoriteAsync(context, (LoadFavoriteCallback) context).execute();
            } else {
                new LoadFavoriteAsync(context, callback).execute();
            }
        }
    }
}
