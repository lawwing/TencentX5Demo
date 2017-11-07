package cn.lawwing.tencentx5demo;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import pub.devrel.easypermissions.EasyPermissions;

public class SelectFileActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private OfficeFileAdapter adapter;
    private ArrayList<FileBean> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        datas = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new OfficeFileAdapter(datas, SelectFileActivity.this);
        recyclerView.setAdapter(adapter);
        String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(SelectFileActivity.this, perms)) {
            EasyPermissions.requestPermissions(SelectFileActivity.this, "需要访问手机存储权限！", 10086, perms);
        } else {
            getLocalOffice();
        }
    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };

    private void getLocalOffice() {
        new Thread() {
            @Override
            public void run() {
                datas.addAll(FileUtils.getSpecificTypeOfFile(SelectFileActivity.this, new String[]{".docx", ".doc", ".xlsx", "xls", "ppt", "pptx"}));
                handle.sendMessage(new Message());
            }
        }.start();
    }
}
