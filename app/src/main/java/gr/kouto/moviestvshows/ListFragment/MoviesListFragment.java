package gr.kouto.moviestvshows.ListFragment;

import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.TimeUnit;

import gr.kouto.moviestvshows.DividerItemDecoration;
import gr.kouto.moviestvshows.GlideApp;
import gr.kouto.moviestvshows.Model.Injection;
import gr.kouto.moviestvshows.Model.TvMoviesDataSource;
import gr.kouto.moviestvshows.Objects.ListedTvMoviesShows;
import gr.kouto.moviestvshows.OnLoadMore;
import gr.kouto.moviestvshows.R;
import gr.kouto.moviestvshows.SQLiteDataBase.DBAdapter;
import gr.kouto.moviestvshows.WrapContentLinearLayoutManager;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MoviesListFragment extends Fragment implements TvMoviesContract.View,
        MenuItem.OnMenuItemClickListener, SearchView.OnQueryTextListener{
    View view;
    Toolbar toolbar;
    MenuItem searchview;
    SearchView searchView2;
    private PublishSubject<String> subject;
    Disposable disposable;
    public int pageNum = 1;
    RecyclerView recyclerView;
    private TvMoviesContract.Presenter presenter;
    MoviesListAdapter mAdapter;
    ListedTvMoviesShows listedTvMoviesShows;
    public ProgressBar progressBar;
    String searchQuery = "";
    Bundle saveState;
    boolean searchOffline = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new TvMoviesPresenter(Injection.provideAppRepository(getActivity(),
                TvMoviesDataSource.class),this);
        listedTvMoviesShows = new ListedTvMoviesShows();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.movies_list_fragment, container,false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.the_movie);
        toolbar.inflateMenu(R.menu.tv_movies_menu);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
        progressBar = view.findViewById(R.id.listProgressBar);
        toolbar.getMenu().findItem(R.id.watchList).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(searchOffline){
                    searchOffline = false;
                }
                else {
                    showSaveList();
                    searchOffline = true;
                }
                return false;
            }
        });
        if(saveState != null){
            listedTvMoviesShows = saveState.getParcelable("listedObjects");
        }
        initializeToolbarMenu();
        setSearchListener("");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager mLayoutManager = new WrapContentLinearLayoutManager(getActivity(), 1, false);
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(10, "VERTICAL");
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MoviesListAdapter(getActivity(), GlideApp.with(this), listedTvMoviesShows, recyclerView, this);
        recyclerView.setAdapter(mAdapter);

        if(savedInstanceState != null) {
            listedTvMoviesShows = savedInstanceState.getParcelable("list");
            searchQuery = savedInstanceState.getString("SearchQuery");
            mAdapter.setFilter(listedTvMoviesShows,false);
            pageNum = savedInstanceState.getInt("pageNum");
        }

        mAdapter.setOnLoadMoreListener(new OnLoadMore() {
            @Override
            public void onLoad() {
               mAdapter.listedTvMoviesShows.add(null);
               mAdapter.notifyItemInserted(mAdapter.listedTvMoviesShows.size() -1);
               presenter.getTvMovies(searchQuery,pageNum);
            }
        });

        return view;
    }

    private void initializeToolbarMenu() {
        DBAdapter adapter = new DBAdapter(getActivity());
        adapter.open();
        if(adapter.sqlDataBaseWatchList.countRows() > 0 ){
            toolbar.getMenu().findItem(R.id.watchList).setVisible(true);
        }
        else {
            toolbar.getMenu().findItem(R.id.watchList).setVisible(false);
        }
        adapter.close();
    }

    private void setSearchListener(String s) {
        searchview = toolbar.getMenu().findItem(R.id.search);
        searchView2 = (SearchView) MenuItemCompat.getActionView(searchview);
        searchView2.setOnQueryTextListener(this);

        subject = PublishSubject.create();
        disposable = subject
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(v -> {
                    pageNum = 1;
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    mAdapter.clear();
                })
                .observeOn(Schedulers.io())
                .switchMap(newSearchQuery -> dataFromNetwork(newSearchQuery))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listedTvMovies -> {
                    System.out.println("Success MVP");
                    mAdapter.setFilter(listedTvMovies,false);
                    recyclerView.smoothScrollToPosition(0);
                });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            listedTvMoviesShows = savedInstanceState.getParcelable("list");
            searchQuery = savedInstanceState.getString("SearchQuery");
            mAdapter.setFilter(listedTvMoviesShows,false);
            pageNum = savedInstanceState.getInt("pageNum");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState = new Bundle();
        saveState.putString("searchQuery", searchQuery);
        if(mAdapter != null)
            saveState.putParcelable("listedObjects", mAdapter.listedTvMoviesShows);
        setArguments(saveState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("list", mAdapter.listedTvMoviesShows);
        outState.putString("SearchQuery", searchQuery);
        outState.putInt("ScrollPosition", mAdapter.currentVisiblePosition);
        outState.putInt("pageNum",pageNum);
    }

    private ObservableSource<ListedTvMoviesShows> dataFromNetwork(String newSearchQuery) {
        if(searchOffline){

            Observable<ListedTvMoviesShows> observable = Observable.just(true)
                    .map(new Function<Boolean,ListedTvMoviesShows>(){
                        @Override
                        public ListedTvMoviesShows apply(Boolean value) throws Exception{
                            DBAdapter adapter = new DBAdapter(getActivity());
                            adapter.open();
                            ListedTvMoviesShows listedTvMoviesShows = adapter.sqlDataBaseWatchList.searchLocal(newSearchQuery);
                            adapter.close();
                            return listedTvMoviesShows;
                        }
                    })
                    .onErrorReturn(error -> new ListedTvMoviesShows())
                    .observeOn(AndroidSchedulers.mainThread());

            return observable;
        }
        else {
            return presenter.getTvMoviesOnSearch(newSearchQuery, pageNum);
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                break;
            default:
                return false;
        }
        return false;
    }

    private void showSaveList() {
        DBAdapter adapter = new DBAdapter(getActivity());
        adapter.open();
        listedTvMoviesShows = adapter.sqlDataBaseWatchList.getWatchList();
        mAdapter.setFilter(listedTvMoviesShows,false);
        adapter.close();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if(!s.equalsIgnoreCase("") && !searchQuery.equals(s)) {
            subject.onNext(s);
            searchQuery = s;
        }
        return true;
    }

    @Override
    public void addTvMoviesToList(ListedTvMoviesShows listedTvMoviesShows) {
        mAdapter.listedTvMoviesShows.remove(mAdapter.listedTvMoviesShows.size() -1);
        mAdapter.notifyItemRemoved(mAdapter.listedTvMoviesShows.size());
        mAdapter.setFilter(listedTvMoviesShows,true);
        mAdapter.setLoaded(false);

    }

    @Override
    public void setPresenter(TvMoviesContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
