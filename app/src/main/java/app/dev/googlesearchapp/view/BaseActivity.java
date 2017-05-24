package app.dev.googlesearchapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by vaik00 on 22.05.2017.
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    protected abstract int getLayoutId();
}
