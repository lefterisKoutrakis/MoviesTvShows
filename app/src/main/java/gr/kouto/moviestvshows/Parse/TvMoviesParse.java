package gr.kouto.moviestvshows.Parse;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import gr.kouto.moviestvshows.AppConnection.HttpConnection;
import gr.kouto.moviestvshows.Objects.ListedTvMoviesShows;
import gr.kouto.moviestvshows.Objects.MovieDetail;
import gr.kouto.moviestvshows.Objects.TvMoviesShow;
import gr.kouto.moviestvshows.R;

public class TvMoviesParse {

    static ListedTvMoviesShows retVal = null;
    static MovieDetail movieDetail = null;

    public static MovieDetail movieDetail(Activity activity, String movieId, boolean isMovie){
        movieDetail = new MovieDetail();

        String url = validateUrl(activity.getResources().getString(R.string.url) +
                activity.getResources().getString(R.string.net_act) +
                activity.getResources().getString(R.string.url_pre) +
                activity.getResources().getString(R.string.net_act)
                + (isMovie ? activity.getResources().getString(R.string.url_movie) : activity.getResources().getString(R.string.url_tv))
                + "/" + movieId
                + "?api_key="
                + activity.getResources().getString(R.string.api_key));

        try{
            String readData = HttpConnection.HttpsRequest(url ,15000);
            System.out.println(readData);
            parseNetDataForMovieDetails(readData, isMovie, movieId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return movieDetail;
    }

    public static ListedTvMoviesShows TvMoviesParse(Activity activity, String...params){

        retVal = new ListedTvMoviesShows();

        final String url = validateUrl(
                activity.getResources().getString(R.string.url) +
                activity.getResources().getString(R.string.net_act) +
                activity.getResources().getString(R.string.url_pre) +
                activity.getResources().getString(R.string.net_act) +
                activity.getResources().getString(R.string.url_search) +
                activity.getResources().getString(R.string.net_act));

        try{
            String readData = HttpConnection.HttpsRequest(url + getParamsString(activity,params), 15000);
            System.out.println(readData);
            parseNetData(readData);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return retVal;
    }

    private static void parseNetData(String readData) throws Exception{
        JSONObject obj = new JSONObject(readData);
        JSONArray jsonArray = obj.getJSONArray("results");
        for(int i = 0; i< jsonArray.length(); i++){
            TvMoviesShow tvMoviesShow = new TvMoviesShow();
            try{ tvMoviesShow.setVoteAverage(jsonArray.getJSONObject(i).getDouble("vote_average"));             } catch (Exception ex){ tvMoviesShow.setVoteAverage(0d); }
            try{ tvMoviesShow.setVoteCount(jsonArray.getJSONObject(i).getInt("vote_count"));                    } catch (Exception ex){ tvMoviesShow.setVoteCount(0);}
            try{ tvMoviesShow.setMovieId(jsonArray.getJSONObject(i).getString("id"));                           } catch (Exception ex){ tvMoviesShow.setMovieId("");}
            try{ tvMoviesShow.setHasVideo(jsonArray.getJSONObject(i).getBoolean("video"));                      } catch (Exception ex) { tvMoviesShow.setHasVideo(false); }
            try{ tvMoviesShow.setMediaType(jsonArray.getJSONObject(i).getString("media_type"));                 } catch (Exception ex) { tvMoviesShow.setMediaType("");}
            try{ tvMoviesShow.setTitle(jsonArray.getJSONObject(i).getString("title"));                          } catch (Exception ex) { tvMoviesShow.setTitle("");}
            try{ tvMoviesShow.setName(jsonArray.getJSONObject(i).getString("name"));                            } catch (Exception ex) { tvMoviesShow.setTitle("");}
            try{ tvMoviesShow.setPopularity(jsonArray.getJSONObject(i).getInt("popularity"));                   } catch (Exception ex) { tvMoviesShow.setPopularity(0);}
            try{ tvMoviesShow.setPosterPath(jsonArray.getJSONObject(i).getString("poster_path"));               } catch (Exception ex) { tvMoviesShow.setPosterPath("");}
            try{ tvMoviesShow.setOriginalLanguage(jsonArray.getJSONObject(i).getString("original_language"));   } catch (Exception ex) { tvMoviesShow.setOriginalLanguage("");}
            try{ tvMoviesShow.setOriginalTitle(jsonArray.getJSONObject(i).getString("original_title"));         } catch (Exception ex) { tvMoviesShow.setOriginalTitle("");}
            try{ tvMoviesShow.setGenreIds(jsonArray.getJSONObject(i).getString("genre_ids"));                   } catch (Exception ex) { tvMoviesShow.setGenreIds("");}
            try{ tvMoviesShow.setBackDropPath(jsonArray.getJSONObject(i).getString("backdrop_path"));           } catch (Exception ex) { tvMoviesShow.setBackDropPath("");}
            try{ tvMoviesShow.setAdult(jsonArray.getJSONObject(i).getBoolean("adult"));                         } catch (Exception ex) { tvMoviesShow.setAdult(false);}
            try{ tvMoviesShow.setOverview(jsonArray.getJSONObject(i).getString("overview"));                    } catch (Exception ex) {tvMoviesShow.setOverview("");}
            try{ tvMoviesShow.setReleaseDate(jsonArray.getJSONObject(i).getString("release_date"));             } catch (Exception ex) {tvMoviesShow.setReleaseDate("");}
            if(tvMoviesShow.getMediaType().equalsIgnoreCase("movie") ||
                tvMoviesShow.getMediaType().equalsIgnoreCase("tv"))
            retVal.add(tvMoviesShow);
        }
        System.out.println("END");
    }

    private static void parseNetDataForMovieDetails(String readData, boolean isMovie, String movieId) throws Exception{
        JSONObject obj = new JSONObject(readData);
        try{ movieDetail.setGenre(obj.getString("genres"));   } catch (Exception ex){ movieDetail.setGenre(""); }
        if(isMovie)
            try{ movieDetail.setTitle(obj.getString("original_title")); } catch (Exception ex){ movieDetail.setTitle("");}
        else
            try{ movieDetail.setTitle(obj.getString("original_name")); } catch (Exception ex){ movieDetail.setTitle("");}
        try{ movieDetail.setSummary(obj.getString("overview")); } catch (Exception ex){ movieDetail.setSummary("");}
        try{ movieDetail.setPoster(obj.getString("poster_path")); } catch (Exception ex) { movieDetail.setPoster(""); }
        try{ movieDetail.setRatings(obj.getString("vote_average")); } catch (Exception ex) { movieDetail.setRatings("0.0");}
        try{ movieDetail.setReleaseDate(obj.getString("release_date")); } catch (Exception ex) { movieDetail.setReleaseDate("");}
        movieDetail.setId(movieId);
    }

    private static String getParamsString(Activity activity, String... param){
        String params = activity.getResources().getString(R.string.url_api_key) + "="
                + activity.getResources().getString(R.string.api_key)
                + "&" + activity.getResources().getString(R.string.url_query) + "="
                + param[0]
                + "&" + activity.getResources().getString(R.string.url_page) + "="
                + param[1];
        return params;
    }

    static String validateUrl(String url)
    {
        if (!url.startsWith("https://"))
            url = ("https://" + url);
        return url.replace(" ", "");
    }
}
