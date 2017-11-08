package cn.lawwing.tencentx5demo;

import android.os.Environment;

import java.io.File;

/**
 * Created by lawwing on 2017/11/8.
 */

public class SDCardUtils
{
    
    private SDCardUtils()
    {
        throw new UnsupportedOperationException("u can't fuck me...");
    }
    
    /**
     * 判断SD卡是否可用
     *
     * @return true : 可用<br>
     *         false : 不可用
     */
    public static boolean isSDCardEnable()
    {
        return Environment.MEDIA_MOUNTED
                .equals(Environment.getExternalStorageState());
    }
    
    /**
     * 获取SD卡路径
     * <p>
     * 一般是/storage/emulated/0/
     * </p>
     *
     * @return SD卡路径
     */
    public static String getSDCardPath()
    {
        return Environment.getExternalStorageDirectory().getPath()
                + File.separator;
    }
}
