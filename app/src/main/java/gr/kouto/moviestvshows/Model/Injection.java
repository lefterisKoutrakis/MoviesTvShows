package gr.kouto.moviestvshows.Model;

import android.app.Activity;

import java.lang.reflect.Method;

public class Injection {

    public static <T> AppRepository provideAppRepository(Activity activity, Class<T> tClass){
        AppRepository appRepository = null;
        Class[] cArray =  new Class[1];
        cArray[0] = Activity.class;
        Method method = null;

        try {
            method = tClass.getMethod("getInstance",cArray);
            if(method.invoke(null,new Object[]{activity}) instanceof TvMoviesDataSource){
                appRepository = AppRepository.getInstance((TvMoviesDataSource)method.invoke(null, new Object[]{ activity}));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    return appRepository;
    }
}
