package app.dev.googlesearchapp.model.loader;

/**
 * Created by vaik00 on 22.05.2017.
 */

public class Response<D> {
    private Exception mException;

    private D mResult;

    static <D> Response<D> ok(D data){

        Response<D> response = new Response<>();
        response.mResult = data;

        return  response;
    }

    static <D> Response<D> error(Exception ex){

        Response<D> response = new Response<>();
        response.mException = ex;

        return  response;
    }

    public boolean hasError() {

        return mException != null;
    }

    public Exception getException() {

        return mException;
    }

    public D getResult() {

        return mResult;
    }
}
