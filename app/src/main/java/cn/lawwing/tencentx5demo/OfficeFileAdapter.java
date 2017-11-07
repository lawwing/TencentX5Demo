package cn.lawwing.tencentx5demo;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lawwing on 2017/11/7.
 */
public class OfficeFileAdapter extends RecyclerView.Adapter<OfficeFileAdapter.OfficeFileHolder> {
    private ArrayList<FileBean> datas;
    private Activity activity;
    private LayoutInflater inflater;

    public OfficeFileAdapter(ArrayList<FileBean> datas, Activity activity) {
        this.datas = datas;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public OfficeFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OfficeFileHolder(inflater
                .inflate(R.layout.file_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(OfficeFileHolder holder, int position) {
        final FileBean model = datas.get(position);
        if (model != null) {
            holder.fileName.setText(model.getFilename());
            holder.bossLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.startActivity(
                            MainActivity.newInstance(activity, model.getPath()));
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class OfficeFileHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        LinearLayout bossLayout;

        public OfficeFileHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            bossLayout = (LinearLayout) itemView.findViewById(R.id.bossLayout);
        }
    }
}
