package gr.kouto.moviestvshows.ListFragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.kouto.moviestvshows.GlideApp;
import gr.kouto.moviestvshows.GlideRequests;
import gr.kouto.moviestvshows.Objects.ListedTvMoviesShows;
import gr.kouto.moviestvshows.Objects.SelectTvMovieEvent;
import gr.kouto.moviestvshows.Objects.TvMoviesShow;
import gr.kouto.moviestvshows.OnLoadMore;
import gr.kouto.moviestvshows.R;

public class MoviesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_DATA = 1;
    private final int TYPE_PROGRESS= 2;

    private Activity activity;
    private GlideRequests glideApp;
    public ListedTvMoviesShows listedTvMoviesShows;
    private RecyclerView recyclerView;
    private MoviesListFragment moviesListFragment;
    private OnLoadMore onLoadMore;
    private int totalItemCount;
    private int lastVisibleItem;
    public int currentVisiblePosition = 0;
    private boolean isLoading = false;

    MoviesListAdapter(Activity activity, GlideRequests glideApp, ListedTvMoviesShows listedTvMoviesShows,
                      RecyclerView recyclerView, MoviesListFragment moviesListFragment){
        this.activity = activity;
        this.glideApp = glideApp;
        this.listedTvMoviesShows = listedTvMoviesShows;
        this.recyclerView = recyclerView;
        this.moviesListFragment = moviesListFragment;
        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && dy > 0 && totalItemCount <= (lastVisibleItem +2 )){
                    if(onLoadMore != null) {
                        moviesListFragment.pageNum++;
                        onLoadMore.onLoad();
                    }
                    isLoading = true;
                }

            }
        });

    }

    protected class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tvMoviePoster)
        ImageView poster;
        @BindView(R.id.tvMovieTitle)        TextView title;
        @BindView(R.id.tvMovieRatings)      TextView ratings;
        @BindView(R.id.tvMovieReleaseDate)  TextView releaseDate;

        MyViewHolder(View view){
            super(view);

            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new SelectTvMovieEvent((TvMoviesShow)listedTvMoviesShows.get(getAdapterPosition()),
                    getAdapterPosition()));
        }
    }

    protected class MyLoadingView extends RecyclerView.ViewHolder{

        @BindView(R.id.progressBar)        ProgressBar progressBar;

        MyLoadingView(View view){
            super(view);

            ButterKnife.bind(this, view);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == TYPE_DATA){
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tv_movies_list_row,viewGroup,false);
            return new MyViewHolder(itemView);
        }
       else {
           View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_loading,viewGroup,false);
           return new MyLoadingView(itemView);
       }
    }

    @Override
    public int getItemViewType(int position) {
       if(listedTvMoviesShows.get(position)instanceof TvMoviesShow)
           return TYPE_DATA;
       else
           return TYPE_PROGRESS;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof MyViewHolder){
            TvMoviesShow tvMoviesShow = (TvMoviesShow) listedTvMoviesShows.get(i);
            if(tvMoviesShow.getMediaType().equalsIgnoreCase("movie"))
                ((MyViewHolder)viewHolder).title.setText(tvMoviesShow.getOriginalTitle());
            else
                ((MyViewHolder)viewHolder).title.setText(tvMoviesShow.getName());

            ((MyViewHolder)viewHolder).releaseDate.setText(tvMoviesShow.getReleaseDate());
            ((MyViewHolder)viewHolder).ratings.setText(tvMoviesShow.getVoteAverage().toString());
            String urlPosterPath = activity.getResources().getString(R.string.uri_poster_path) + tvMoviesShow.getPosterPath();
            GlideApp.with(activity)
                    .load(urlPosterPath)
                    .centerCrop()
                    .dontAnimate()
                    .into(((MyViewHolder)viewHolder).poster);
        }
        else{
            ((MyLoadingView)viewHolder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return listedTvMoviesShows.size();
    }

    public void setFilter(ListedTvMoviesShows listedTvMoviesShows, boolean addToList){
        if(addToList)
            this.listedTvMoviesShows.addAll(listedTvMoviesShows);
        else
            this.listedTvMoviesShows = listedTvMoviesShows;
        moviesListFragment.progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
    }

    public void clear(){
        listedTvMoviesShows.clear();
    }

    public boolean isLoading() { return isLoading; }

    public void setLoaded(boolean value) { isLoading = value; }

    void setOnLoadMoreListener(OnLoadMore mOnLoadMore) {
        this.onLoadMore = mOnLoadMore;
    }
}
