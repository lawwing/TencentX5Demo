package cn.lawwing.tencentx5demo;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
{
    private OfficeView mOfficeView;
    
    private TextView pizhu;
    
    public static Intent newInstance(Activity activity, String filePath)
    {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("filePath", filePath);
        return intent;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(R.layout.activity_main);
        mOfficeView = (OfficeView) findViewById(R.id.mOfficeView);
        pizhu = (TextView) findViewById(R.id.pizhu);
        pizhu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bitmapByte = baos.toByteArray();
                startActivity(CommentOfficeActivity
                        .newIntance(MainActivity.this, bitmapByte));
            }
        });
        
        String[] perms = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE };
        if (!EasyPermissions.hasPermissions(MainActivity.this, perms))
        {
            EasyPermissions.requestPermissions(MainActivity.this,
                    "需要访问手机存储权限！",
                    10086,
                    perms);
        }
        else
        {
            mOfficeView.setOnGetFilePathListener(
                    new OfficeView.OnGetFilePathListener()
                    {
                        @Override
                        public void onGetFilePath(OfficeView officeView)
                        {
                            getFilePathAndShowFile(officeView);
                        }
                    });
            Log.e("test", filePath);
            mOfficeView.show();
        }
        
    }
    
    // 获取当前的页面的bitmap
    private Bitmap bmp;
    
    /**
     * 初始化bitmap
     */
    private void initBitmap()
    {
        pizhu.setVisibility(View.GONE);
        bmp = myShot(this);
        pizhu.setVisibility(View.VISIBLE);
    }
    
    /**
     * 获取当前屏幕的bitmap
     *
     * @param activity
     * @return
     */
    public Bitmap myShot(Activity activity)
    {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();
        
        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();
        
        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();
        
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(),
                0,
                statusBarHeights,
                widths,
                heights - statusBarHeights);
        
        // 销毁缓存信息
        view.destroyDrawingCache();
        
        return bmp;
    }
    
    private void getIntentData()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            if (intent.hasExtra("filePath"))
            {
                filePath = intent.getStringExtra("filePath");
            }
        }
    }
    
    public void setFilePath(String fileUrl)
    {
        this.filePath = fileUrl;
    }
    
    String filePath = "/storage/emulated/0/test.docx";
    
    private String getFilePath()
    {
        return filePath;
    }
    
    private void getFilePathAndShowFile(OfficeView officeView)
    {
        
        officeView.displayFile(new File(getFilePath()));
        
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mOfficeView != null)
        {
            mOfficeView.onStopDisplay();
        }
    }
}
