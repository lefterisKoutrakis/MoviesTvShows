package gr.kouto.moviestvshows.DetailsFragment;

import gr.kouto.moviestvshows.BaseView;
import gr.kouto.moviestvshows.Objects.MovieDetail;

public interface MovieDetailsContract {

    interface View extends BaseView<Presenter>{
        void refreshMovieDetails(MovieDetail movieDetail);
    }

    interface Presenter{
        void getMovieDetails(String movieId, boolean isMovie);
    }
}
