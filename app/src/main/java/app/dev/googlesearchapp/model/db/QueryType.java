package app.dev.googlesearchapp.model.db;

/**
 * Created by vaik00 on 24.05.2017.
 */

public enum QueryType {
    INSERT(1), DELETE(2), SELECT(3);

    int value;

    private QueryType(int value) {
        this.value = value;
    }
}
