package gr.kouto.moviestvshows.SQLiteDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "TvMovies.db";


    private DBAdapterHelper helper;
    public SQLDataBaseWatchList sqlDataBaseWatchList;
    private SQLiteDatabase db;

    public DBAdapter(Context context){
        helper = new DBAdapterHelper(context);

    }

    public void open(){
        db = helper.getReadableDatabase();
        sqlDataBaseWatchList = new SQLDataBaseWatchList(db);
    }

    public void close(){
        db.close();
    }



    public class DBAdapterHelper extends SQLiteOpenHelper{


        private DBAdapterHelper(Context context) {
            super(context, DATABASE_NAME,null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQLDataBaseWatchList.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SQLDataBaseWatchList.DROP_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
}
