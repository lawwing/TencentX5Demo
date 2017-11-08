package cn.lawwing.tencentx5demo;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lawwing on 2017/11/8.
 */

public class ImageUtils
{
    private ImageUtils()
    {
        throw new UnsupportedOperationException("u can't fuck me...");
    }
    
    /**
     * 保存图片
     *
     * @param src 源图片
     * @param filePath 要保存到的文件路径
     * @param format 格式
     * @return {@code true}: 成功<br>
     *         {@code false}: 失败
     */
    public static boolean save(Bitmap src, String filePath,
            Bitmap.CompressFormat format)
    {
        return save(src, FileUtils.getFileByPath(filePath), format, false);
    }
    
    /**
     * 保存图片
     *
     * @param src 源图片
     * @param file 要保存到的文件
     * @param format 格式
     * @param recycle 是否回收
     * @return {@code true}: 成功<br>
     *         {@code false}: 失败
     */
    public static boolean save(Bitmap src, File file,
            Bitmap.CompressFormat format, boolean recycle)
    {
        if (isEmptyBitmap(src) || !FileUtils.createOrExistsFile(file))
            return false;
        System.out.println(src.getWidth() + ", " + src.getHeight());
        OutputStream os = null;
        boolean ret = false;
        try
        {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled())
                src.recycle();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            FileUtils.closeIO(os);
        }
        return ret;
    }
    
    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>
     *         {@code false}: 否
     */
    private static boolean isEmptyBitmap(Bitmap src)
    {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }
    
}
