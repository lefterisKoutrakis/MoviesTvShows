package gr.kouto.moviestvshows.DetailsFragment;

import gr.kouto.moviestvshows.Model.AppRepository;
import gr.kouto.moviestvshows.Model.TvMoviesDataSourceHelper;
import gr.kouto.moviestvshows.Objects.MovieDetail;

public class MovieDetailsPresenter implements MovieDetailsContract.Presenter{

    private final MovieDetailsContract.View movieDetailsView;
    public MovieDetail movieDetail;
    AppRepository appRepository;
    private String movieId;

    public MovieDetailsPresenter(AppRepository appRepository, MovieDetailsContract.View movieDetailsView){
        this.appRepository = appRepository;
        this.movieDetailsView = movieDetailsView;
        movieDetailsView.setPresenter(this);
    }

    @Override
    public void getMovieDetails(String movieId,boolean isMovie) {
        appRepository.getMovieDetails(new TvMoviesDataSourceHelper.LoadMovieDetailsCallback() {
            @Override
            public void onLoadDetailMovies(MovieDetail movieDetail) {
                movieDetailsView.refreshMovieDetails(movieDetail);
            }
        },movieId, isMovie);
    }
}
