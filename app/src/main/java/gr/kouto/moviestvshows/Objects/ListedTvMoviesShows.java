package gr.kouto.moviestvshows.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ListedTvMoviesShows extends ArrayList<Object> implements Parcelable {


    public ListedTvMoviesShows(){}
    protected ListedTvMoviesShows(Parcel in) {
    }

    public static final Creator<ListedTvMoviesShows> CREATOR = new Creator<ListedTvMoviesShows>() {
        @Override
        public ListedTvMoviesShows createFromParcel(Parcel in) {
            return new ListedTvMoviesShows(in);
        }

        @Override
        public ListedTvMoviesShows[] newArray(int size) {
            return new ListedTvMoviesShows[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
