package gr.kouto.moviestvshows.Model;

import gr.kouto.moviestvshows.Objects.ListedTvMoviesShows;
import gr.kouto.moviestvshows.Objects.MovieDetail;

public class AppRepository implements TvMoviesDataSourceHelper {

    TvMoviesDataSource tvMoviesDataSource;
    public static AppRepository INSTANCE = null;

    public AppRepository(TvMoviesDataSource tvMoviesDataSource){
        this.tvMoviesDataSource = tvMoviesDataSource;
    }

    public static AppRepository getInstance(TvMoviesDataSource tvMoviesDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(tvMoviesDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getTvMovies(LoadTvMoviesCallback callback, String... params) {
        tvMoviesDataSource.getTvMovies(new LoadTvMoviesCallback() {
            @Override
            public void onLoadTvMovies(ListedTvMoviesShows listedTvMoviesShows) {
                callback.onLoadTvMovies(listedTvMoviesShows);
            }
        },params);
    }

    @Override
    public ListedTvMoviesShows getTvMoviesSearch(String... params) {
        return tvMoviesDataSource.getTvMoviesSearch(params);
    }

    @Override
    public void getMovieDetails(LoadMovieDetailsCallback callback, String movieId, boolean isMovie) {
        tvMoviesDataSource.getMovieDetails(new LoadMovieDetailsCallback() {
            @Override
            public void onLoadDetailMovies(MovieDetail movieDetail) {
                callback.onLoadDetailMovies(movieDetail);
            }
        }, movieId,isMovie);

    }
}
