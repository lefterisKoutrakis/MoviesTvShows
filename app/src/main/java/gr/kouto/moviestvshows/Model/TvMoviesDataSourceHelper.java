package gr.kouto.moviestvshows.Model;


import gr.kouto.moviestvshows.Objects.ListedTvMoviesShows;
import gr.kouto.moviestvshows.Objects.MovieDetail;

public interface TvMoviesDataSourceHelper {

    interface LoadTvMoviesCallback{
        void onLoadTvMovies(ListedTvMoviesShows listedTvMoviesShows);
    }

    interface LoadMovieDetailsCallback{
        void onLoadDetailMovies(MovieDetail movieDetail);
    }
    void getTvMovies(LoadTvMoviesCallback callback, String... params);
    ListedTvMoviesShows getTvMoviesSearch(String... params);
    void getMovieDetails(LoadMovieDetailsCallback callback, String movieId, boolean isMovie);

}
