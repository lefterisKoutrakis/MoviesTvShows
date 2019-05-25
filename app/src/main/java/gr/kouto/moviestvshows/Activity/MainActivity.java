package gr.kouto.moviestvshows.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import gr.kouto.moviestvshows.DetailsFragment.MovieDetailsContract;
import gr.kouto.moviestvshows.DetailsFragment.MovieDetailsPresenter;
import gr.kouto.moviestvshows.Model.Injection;
import gr.kouto.moviestvshows.Model.TvMoviesDataSource;
import gr.kouto.moviestvshows.Objects.MovieDetail;
import gr.kouto.moviestvshows.Objects.SelectTvMovieEvent;
import gr.kouto.moviestvshows.Objects.TvMoviesShow;
import gr.kouto.moviestvshows.R;

public class MainActivity extends AppCompatActivity implements MovieDetailsContract.View {

    private MovieDetailsContract.Presenter presenter;
    Bundle args = new Bundle();
    TvMoviesShow tvMoviesShow;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MovieDetailsPresenter(Injection.provideAppRepository(this,
                TvMoviesDataSource.class),this);

        if(savedInstanceState != null){
            tvMoviesShow = savedInstanceState.getParcelable("tvMoviesShow");
            position = savedInstanceState.getInt("position");
        }
        else{
            Navigation.findNavController(findViewById(R.id.fragment))
                    .navigate(R.id.action_placeholder_to_moviesListFragment);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("tvMoviesShow",tvMoviesShow);
        outState.putInt("position", position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSelectedTvMovie(SelectTvMovieEvent event){
        args.putInt("position",event.position);
        tvMoviesShow = event.tvMoviesShow;
        position = event.position;
        presenter.getMovieDetails(event.tvMoviesShow.getMovieId(),event.tvMoviesShow.getMediaType()
                .equalsIgnoreCase("movie"));
    }

    @Override
    public void refreshMovieDetails(MovieDetail movieDetail) {
        movieDetail.setMediaType(tvMoviesShow.getMediaType());
        args.putParcelable("movieDetail", movieDetail);
        Navigation.findNavController(findViewById(R.id.fragment))
                .navigate(R.id.action_moviesListFragment_to_moviesDetailsFragment, args);
    }

    @Override
    public void setPresenter(MovieDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
