package gr.kouto.moviestvshows.ListFragment;


import gr.kouto.moviestvshows.BaseView;
import gr.kouto.moviestvshows.Objects.ListedTvMoviesShows;
import io.reactivex.Observable;

public interface TvMoviesContract {

    interface View extends BaseView<Presenter> {
        void addTvMoviesToList(ListedTvMoviesShows listedTvMoviesShows);
    }

    interface Presenter{
        Observable<ListedTvMoviesShows> getTvMoviesOnSearch(String searchQuery, int pageNum);
        void getTvMovies(String searchQuery, int pageNum);
    }
}
