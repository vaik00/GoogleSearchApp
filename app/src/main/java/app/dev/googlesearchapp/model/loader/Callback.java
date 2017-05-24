package app.dev.googlesearchapp.model.loader;

/**
 * Created by vaik00 on 22.05.2017.
 */

public interface Callback<D> {
    void onFailure(Exception ex);

    void onSuccess(D result);
}
