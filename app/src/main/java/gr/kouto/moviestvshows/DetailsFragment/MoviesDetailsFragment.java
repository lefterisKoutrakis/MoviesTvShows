package gr.kouto.moviestvshows.DetailsFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.kouto.moviestvshows.GlideApp;
import gr.kouto.moviestvshows.Objects.MovieDetail;
import gr.kouto.moviestvshows.R;
import gr.kouto.moviestvshows.SQLiteDataBase.DBAdapter;

public class MoviesDetailsFragment extends Fragment implements View.OnClickListener {

    private int position;
    private MovieDetail movieDetail;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt("position",0);
        movieDetail = getArguments().getParcelable("movieDetail");
    }

    @BindView(R.id.tv_movie_poster)
    ImageView poster;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.summary_txt)
    TextView summary;
    @BindView(R.id.genre_txt)
    TextView genre;
    @BindView(R.id.watchList)
    AppCompatImageView watchList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tv_movie_detail,container,false);
        ButterKnife.bind(this, view);

        toolbar.setNavigationIcon(getActivity().getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(this);
        if(movieDetail != null) {
            toolbar.setTitle(movieDetail.getTitle());
            summary.setText(movieDetail.getSummary());

            try {
                JSONArray jsonArray = new JSONArray(movieDetail.getGenre());
                genre.setText(jsonArray.getJSONObject(0).getString("name"));
                movieDetail.setGenre(genre.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String urlPosterPath = getActivity().getResources().getString(R.string.uri_poster_path)
                    + movieDetail.getPoster();

            GlideApp.with(getActivity())
                    .load(urlPosterPath)
                    .centerCrop()
                    .dontAnimate()
                    .into(poster);

            chechWatchList();
        }

        watchList.setOnClickListener(this);

        return view;

    }

    private void chechWatchList() {
        DBAdapter adapter = new DBAdapter(getActivity());
        adapter.open();
        if(adapter.sqlDataBaseWatchList.checkIfExist(movieDetail.getId())){
            watchList.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_watch_list_in_black_24dp));
        }
        else
            watchList.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_watch_list_not_black_24dp));
        adapter.close();
    }

    @Override
    public void onClick(View v) {
        if(v instanceof AppCompatImageView) {
            DBAdapter adapter = new DBAdapter(getActivity());
            adapter.open();
            adapter.sqlDataBaseWatchList.insert(movieDetail);
            adapter.close();
            chechWatchList();
        }
        else{
            Navigation.findNavController(view).navigateUp();
        }
    }
}
