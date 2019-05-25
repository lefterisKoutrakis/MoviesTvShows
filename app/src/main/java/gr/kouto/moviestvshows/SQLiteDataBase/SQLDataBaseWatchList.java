package gr.kouto.moviestvshows.SQLiteDataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import gr.kouto.moviestvshows.Objects.ListedTvMoviesShows;
import gr.kouto.moviestvshows.Objects.MovieDetail;
import gr.kouto.moviestvshows.Objects.TvMoviesShow;

public class SQLDataBaseWatchList {

    private SQLiteDatabase db;

    SQLDataBaseWatchList(SQLiteDatabase db) { this.db = db; }

    private static final String TABLE_NAME = "watchList";
    private static final String COLUMN_MASTER_ID = "master_id";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_SUMMARY = "summary";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_POSTER = "poster";
    private static final String COLUMN_RATINGS = "ratings";
    private static final String COLUMN_RELEASE_DATE = "release_date";
    private static final String COLUMN_MEDIA_TYPE = "media_type";

    static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME +    " ( "
            + COLUMN_MASTER_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_ID  +    " TEXT, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_SUMMARY + " TEXT, "
            + COLUMN_GENRE + " TEXT, "
            + COLUMN_POSTER + " TEXT, "
            + COLUMN_RATINGS + " TEXT, "
            + COLUMN_RELEASE_DATE + " TEXT, "
            + COLUMN_MEDIA_TYPE + " TEXT " +
            ")";

    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public int countRows(){
        Cursor cursor;
        String selectQuery = "SELECT id FROM " + TABLE_NAME;
        cursor = db.rawQuery(selectQuery,null);
        if(cursor != null){
            return cursor.getCount();
        }else
            return 0;
    }

    public void insert(MovieDetail movieDetail){
        if(db.update(TABLE_NAME, getValues(movieDetail),COLUMN_ID + " = " + movieDetail.getId(),null) == 0)
            db.insert(TABLE_NAME, null, getValues(movieDetail));
        else
            delete(movieDetail.getId());
    }

    public void delete(String id){
        db.delete(TABLE_NAME,COLUMN_ID + " = " + id,null);
    }

    private ContentValues getValues(MovieDetail movieDetail) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, movieDetail.getId());
        values.put(COLUMN_TITLE, movieDetail.getTitle());
        values.put(COLUMN_SUMMARY, movieDetail.getSummary());
        values.put(COLUMN_GENRE, movieDetail.getGenre());
        values.put(COLUMN_POSTER, movieDetail.getPoster());
        values.put(COLUMN_RATINGS, movieDetail.getRatings());
        values.put(COLUMN_RELEASE_DATE, movieDetail.getReleaseDate());
        values.put(COLUMN_MEDIA_TYPE, movieDetail.getMediaType());
        return values;
    }

    public boolean checkIfExist(String id){
       Cursor cursor;
       boolean exists = false;
       String selectQuery = "SELECT * FROM " + TABLE_NAME
               + " WHERE " + COLUMN_ID + " = " + id;

       cursor = db.rawQuery(selectQuery,null);
       if (cursor != null){
           try {
               while (cursor.moveToNext()){
                   exists = true;
               }
           }finally {
               cursor.close();
           }
       }
       return exists;
    }

   public ListedTvMoviesShows getWatchList(){
       ListedTvMoviesShows arrayList = new ListedTvMoviesShows();
        Cursor cursor;
        String selectQuery = "SELECT * "
                + " FROM " + TABLE_NAME ;

        cursor = db.rawQuery(selectQuery, null);
        if (cursor != null){
            try {
                while (cursor.moveToNext()) {
                    MovieDetail movieDetail1 = new MovieDetail();
                    TvMoviesShow tvMoviesShow = new TvMoviesShow();
                    movieDetail1 = setValues(cursor);
                    tvMoviesShow.setMovieId(movieDetail1.getId());
                    tvMoviesShow.setOriginalTitle(movieDetail1.getTitle());
                    tvMoviesShow.setReleaseDate(movieDetail1.getReleaseDate());
                    tvMoviesShow.setVoteAverage(Double.valueOf(movieDetail1.getRatings()));
                    tvMoviesShow.setPosterPath(movieDetail1.getPoster());
                    tvMoviesShow.setMediaType(movieDetail1.getMediaType());
                    arrayList.add(tvMoviesShow);
                }
            } finally {
                cursor.close();
            }
        }
        return arrayList;
    }

    public ListedTvMoviesShows searchLocal(String filter){
        ListedTvMoviesShows arrayList = new ListedTvMoviesShows();
        Cursor cursor;
        String selectQuery = "SELECT * "
                + " FROM " + TABLE_NAME
                + " WHERE " + COLUMN_TITLE + " LIKE '%" + filter + "%'";

        cursor = db.rawQuery(selectQuery, null);
        if (cursor != null){
            try {
                while (cursor.moveToNext()) {
                    MovieDetail movieDetail1 = new MovieDetail();
                    TvMoviesShow tvMoviesShow = new TvMoviesShow();
                    movieDetail1 = setValues(cursor);
                    tvMoviesShow.setMovieId(movieDetail1.getId());
                    tvMoviesShow.setOriginalTitle(movieDetail1.getTitle());
                    tvMoviesShow.setReleaseDate(movieDetail1.getReleaseDate());
                    tvMoviesShow.setVoteAverage(Double.valueOf(movieDetail1.getRatings()));
                    tvMoviesShow.setPosterPath(movieDetail1.getPoster());
                    tvMoviesShow.setMediaType(movieDetail1.getMediaType());
                    arrayList.add(tvMoviesShow);
                }
            } finally {
                cursor.close();
            }
        }
        return arrayList;
    }

    private MovieDetail setValues(Cursor cursor){
        MovieDetail movieDetail = new MovieDetail();
        try{ movieDetail.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID))); }                catch (Exception ex) {}
        try{ movieDetail.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))); } catch (Exception ex) {}
        try{ movieDetail.setSummary(cursor.getString(cursor.getColumnIndex(COLUMN_SUMMARY))); } catch (Exception ex) {}
        try{ movieDetail.setGenre(cursor.getString(cursor.getColumnIndex(COLUMN_GENRE))); } catch (Exception ex) {}
        try{ movieDetail.setPoster(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER))); } catch (Exception ex) {}
        try{ movieDetail.setRatings(cursor.getString(cursor.getColumnIndex(COLUMN_RATINGS))); } catch (Exception ex) {}
        try{ movieDetail.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE))); } catch (Exception ex) {}
        try{ movieDetail.setMediaType(cursor.getString(cursor.getColumnIndex(COLUMN_MEDIA_TYPE))); } catch (Exception ex) {}
        return movieDetail;
    }

}
