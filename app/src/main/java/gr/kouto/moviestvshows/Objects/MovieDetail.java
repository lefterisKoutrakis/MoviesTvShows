package gr.kouto.moviestvshows.Objects;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDetail implements Parcelable {

    private String id;
    private String title;
    private String summary;
    private String poster;
    private String genre;
    private String trailer;
    private String ratings;
    private String releaseDate;
    private String mediaType;

    public MovieDetail(){}

    protected MovieDetail(Parcel in) {
        id = in.readString();
        title = in.readString();
        summary = in.readString();
        poster = in.readString();
        genre = in.readString();
        trailer = in.readString();
        ratings = in.readString();
        releaseDate = in.readString();
        mediaType = in.readString();
    }

    public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };

    public void setId(String value)     { id = value; }
    public void setTitle(String value) { title = value; }
    public void setSummary(String value) { summary = value; }
    public void setPoster(String value)    { poster = value; }
    public void setGenre(String value)      { genre = value; }
    public void setTrailer(String value)    { trailer = value;}
    public void setRatings(String value)    { ratings = value; }
    public void setReleaseDate(String value){ releaseDate = value; }
    public void setMediaType(String value)  { mediaType = value; }

    public String getId()       { return id; }
    public String getTitle()  { return title; }
    public String getSummary()  { return summary; }
    public String getPoster()   { return poster; }
    public String getGenre()    { return genre; }
    public String getTrailer()  { return trailer; }
    public String getRatings() {  return ratings;  }
    public String getReleaseDate() { return releaseDate; }
    public String getMediaType()    { return mediaType; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeString(poster);
        dest.writeString(genre);
        dest.writeString(trailer);
        dest.writeString(ratings);
        dest.writeString(releaseDate);
        dest.writeString(mediaType);
    }
}
