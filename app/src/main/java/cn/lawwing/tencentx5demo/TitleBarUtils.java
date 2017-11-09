package cn.lawwing.tencentx5demo;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by lawwing on 2017/11/9.
 */

public class TitleBarUtils
{
    private Activity activity;
    
    public TitleBarUtils(Activity activity)
    {
        this.activity = activity;
    }
    
    public void initTitle(String title)
    {
        TextView titleText = (TextView) activity.findViewById(R.id.title_text);
        ImageView back_btn = (ImageView) activity.findViewById(R.id.back_btn);
        
        titleText.setText(title);
        back_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                activity.finish();
            }
        });
    }
    
}
