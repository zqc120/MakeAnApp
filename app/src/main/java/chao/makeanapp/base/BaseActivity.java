package chao.makeanapp.base;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Created by Chao on 2017/7/27.
 */

public class BaseActivity extends FragmentActivity {
    long first = 0;
    long second = 0;
    @Override
    public void onBackPressed() {
        second = System.currentTimeMillis();
        if (second - first > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            first = second;
        } else {
            System.exit(0);
        }
    }
}

