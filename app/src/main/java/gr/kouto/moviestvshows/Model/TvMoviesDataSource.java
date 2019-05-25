package gr.kouto.moviestvshows.Model;

import android.app.Activity;

import gr.kouto.moviestvshows.Objects.ListedTvMoviesShows;
import gr.kouto.moviestvshows.Objects.MovieDetail;
import gr.kouto.moviestvshows.Parse.TvMoviesParse;

public class TvMoviesDataSource implements TvMoviesDataSourceHelper{

    Activity activity;
    private static volatile TvMoviesDataSource INSTANCE;

    public TvMoviesDataSource(Activity activity){
        this.activity = activity;
    }

    public static TvMoviesDataSource getInstance(Activity activity){
        if (INSTANCE == null){
            synchronized (TvMoviesDataSource.class){
                if(INSTANCE == null){
                    INSTANCE = new TvMoviesDataSource(activity);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getTvMovies(final LoadTvMoviesCallback callback, String... params) {
        final Runnable runnable = () -> {
            final ListedTvMoviesShows listedTvMoviesShows = TvMoviesParse.TvMoviesParse(activity,params);
            activity.runOnUiThread(() -> {
                callback.onLoadTvMovies(listedTvMoviesShows);
            });
        };
        new Thread(runnable).start();
    }

    @Override
    public ListedTvMoviesShows getTvMoviesSearch(String... params) {
        ListedTvMoviesShows listedTvMoviesShows = TvMoviesParse.TvMoviesParse(activity,params);
        return listedTvMoviesShows;
    }

    @Override
    public void getMovieDetails(LoadMovieDetailsCallback callback, String movieId, boolean isMovie) {
        final Runnable runnable = ()->{
            final MovieDetail movieDetal = TvMoviesParse.movieDetail(activity,movieId, isMovie);
            activity.runOnUiThread(() -> callback.onLoadDetailMovies(movieDetal));
        };
        new Thread(runnable).start();
    }
}
