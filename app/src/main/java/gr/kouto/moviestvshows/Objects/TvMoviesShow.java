package gr.kouto.moviestvshows.Objects;

import android.os.Parcel;
import android.os.Parcelable;

public class TvMoviesShow implements Parcelable {

    private String posterPath;
    private String title;
    private String name;
    private String releaseDate;
    private double voteAverage;
    private int voteCount;
    private String movieId;
    private boolean hasVideo;
    private String mediaType;
    private int popularity;
    private String originalLanguage;
    private String originalTitle;
    private String  genreIds;
    private String backDropPath;
    private boolean adult;
    private String overview;

    public TvMoviesShow(){}

    public static final Creator<TvMoviesShow> CREATOR = new Creator<TvMoviesShow>() {
        @Override
        public TvMoviesShow createFromParcel(Parcel in) {
            return new TvMoviesShow(in);
        }

        @Override
        public TvMoviesShow[] newArray(int size) {
            return new TvMoviesShow[size];
        }
    };

    public void setPosterPath(String value)         { posterPath = value; }
    public void setTitle(String value)              { title = value; }
    public void setName(String value)               { name = value; }
    public void setReleaseDate(String value)        { releaseDate = value; }
    public void setVoteAverage(Double value)        { voteAverage = value; }
    public void setMediaType(String value)          { mediaType = value; }
    public void setOriginalLanguage(String value)   { originalLanguage = value; }
    public void setOriginalTitle(String value)      { originalTitle = value; }
    public void setBackDropPath(String value)       { backDropPath = value; }
    public void setOverview(String value)           { overview = value; }
    public void setGenreIds(String value)           { genreIds = value; }
    public void setMovieId(String value)            { movieId = value; }
    public void setPopularity(int value)            { popularity = value; }
    public void setVoteCount(int value)             { voteCount = value; }
    public void setHasVideo(boolean value)          { hasVideo = value; }
    public void setAdult(boolean value)             { adult = value; }

    public String getPosterPath()                   { return posterPath; }
    public String getTitle()                        { return title; }
    public String getName()                         { return name; }
    public String getReleaseDate()                  { return releaseDate; }
    public Double getVoteAverage()                  { return voteAverage; }
    public String getMediaType()                    { return mediaType; }
    public String getOriginalLanguage()             { return originalLanguage; }
    public String getOriginalTitle()                { return originalTitle; }
    public String getBackDropPath()                 { return backDropPath; }
    public String getOverview()                     { return overview; }
    public String getGenreIds()                     { return genreIds; }
    public String getMovieId()                      { return movieId; }
    public int getVoteCount()                       { return voteCount; }
    public int getPopularity()                      { return popularity; }
    public boolean getHasVideo()                    { return hasVideo; }
    public boolean getAdult()                       { return adult; }

    protected TvMoviesShow(Parcel in) {
        posterPath = in.readString();
        title = in.readString();
        name = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readInt();
        movieId = in.readString();
        hasVideo = in.readByte() != 0;
        mediaType = in.readString();
        popularity = in.readInt();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        genreIds = in.readString();
        backDropPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(name);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
        dest.writeString(movieId);
        dest.writeByte((byte) (hasVideo ? 1 : 0));
        dest.writeString(mediaType);
        dest.writeInt(popularity);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(genreIds);
        dest.writeString(backDropPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
    }
}
