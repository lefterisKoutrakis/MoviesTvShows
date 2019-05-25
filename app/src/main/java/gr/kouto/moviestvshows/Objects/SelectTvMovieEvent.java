package gr.kouto.moviestvshows.Objects;

public class SelectTvMovieEvent {
    public int position;
    public TvMoviesShow tvMoviesShow;

    public SelectTvMovieEvent(TvMoviesShow tvMoviesShow, int position){
        this.tvMoviesShow = tvMoviesShow;
        this.position = position;
    }
}
