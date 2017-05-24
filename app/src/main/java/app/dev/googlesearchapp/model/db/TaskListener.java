package app.dev.googlesearchapp.model.db;

/**
 * Created by vaik00 on 24.05.2017.
 */

public interface TaskListener {
    void onTaskCompleted(QueryType queryType, Object result);
}
