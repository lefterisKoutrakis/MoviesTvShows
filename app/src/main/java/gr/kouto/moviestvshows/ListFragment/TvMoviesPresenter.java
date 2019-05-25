package gr.kouto.moviestvshows.ListFragment;

import gr.kouto.moviestvshows.Model.AppRepository;
import gr.kouto.moviestvshows.Model.TvMoviesDataSourceHelper;
import gr.kouto.moviestvshows.Objects.ListedTvMoviesShows;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

public class TvMoviesPresenter implements TvMoviesContract.Presenter {

    private final TvMoviesContract.View tvMoviesView;
    AppRepository appRepository;
    private String searchQuery;
    private int pageNum;

    public TvMoviesPresenter(AppRepository appRepository, TvMoviesContract.View tvMoviesView){
        this.appRepository = appRepository;
        this.tvMoviesView = tvMoviesView;
        tvMoviesView.setPresenter(this);
    }

    @Override
    public Observable<ListedTvMoviesShows> getTvMoviesOnSearch(String searchQuery, int pageNum) {
        this.searchQuery = searchQuery;
        this.pageNum = pageNum;

        return Observable.just(true)
                .map(new Function<Boolean,ListedTvMoviesShows>(){
                    @Override
                    public ListedTvMoviesShows apply(Boolean value) throws Exception{
                        if(!TvMoviesPresenter.this.searchQuery.equalsIgnoreCase(searchQuery))
                            TvMoviesPresenter.this.pageNum = 1;
                        TvMoviesPresenter.this.searchQuery = searchQuery;

                        return appRepository.getTvMoviesSearch(searchQuery,String.valueOf(pageNum));
                    }
                })
                .onErrorReturn(error -> new ListedTvMoviesShows())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getTvMovies(String searchQuery, int pageNum) {
        appRepository.getTvMovies(new TvMoviesDataSourceHelper.LoadTvMoviesCallback() {
            @Override
            public void onLoadTvMovies(ListedTvMoviesShows listedTvMoviesShows) {
                tvMoviesView.addTvMoviesToList(listedTvMoviesShows);
            }
        }, searchQuery,String.valueOf(pageNum));
    }
}
