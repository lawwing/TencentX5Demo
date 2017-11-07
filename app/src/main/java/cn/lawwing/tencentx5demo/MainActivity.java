package cn.lawwing.tencentx5demo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private OfficeView mOfficeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(MainActivity.this, perms)) {
            EasyPermissions.requestPermissions(MainActivity.this, "需要访问手机存储权限！", 10086, perms);
        } else {
            mOfficeView = (OfficeView) findViewById(R.id.mOfficeView);
            mOfficeView.setOnGetFilePathListener(new OfficeView.OnGetFilePathListener() {
                @Override
                public void onGetFilePath(OfficeView officeView) {
                    getFilePathAndShowFile(officeView);
                }
            });
            Log.e("test", filePath);
            mOfficeView.show();
        }

    }

    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

    String filePath = "/storage/emulated/0/test.docx";

    private String getFilePath() {
        return filePath;
    }

    private void getFilePathAndShowFile(OfficeView officeView) {

        officeView.displayFile(new File(getFilePath()));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOfficeView != null) {
            mOfficeView.onStopDisplay();
        }
    }
}
