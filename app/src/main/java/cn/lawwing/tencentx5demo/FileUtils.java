package cn.lawwing.tencentx5demo;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.text.TextUtils;
import android.util.Log;

public class FileUtils
{
    
    public static ArrayList<FileBean> getSpecificTypeOfFile(Context context,
            String[] extension)
    {
        ArrayList<FileBean> fileBeens = new ArrayList<>();
        // 从外存中获取
        Uri fileUri = Files.getContentUri("external");
        // 筛选列，这里只筛选了：文件路径和不含后缀的文件名
        String[] projection = new String[] { FileColumns.DATA,
                FileColumns.TITLE };
        // 构造筛选语句
        String selection = "";
        for (int i = 0; i < extension.length; i++)
        {
            if (i != 0)
            {
                selection = selection + " OR ";
            }
            selection = selection + FileColumns.DATA + " LIKE '%" + extension[i]
                    + "'";
        }
        // 按时间递增顺序对结果进行排序;待会从后往前移动游标就可实现时间递减
        String sortOrder = FileColumns.DATE_MODIFIED;
        // 获取内容解析器对象
        ContentResolver resolver = context.getContentResolver();
        // 获取游标
        Cursor cursor = resolver
                .query(fileUri, projection, selection, null, sortOrder);
        if (cursor == null)
        {
            return fileBeens;
        }
        // 游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
        if (cursor.moveToLast())
        {
            do
            {
                // 输出文件的完整路径
                String data = cursor.getString(0);
                String name = cursor.getString(1);
                FileBean fileBean = new FileBean();
                fileBean.setFilename(name);
                fileBean.setPath(data);
                fileBeens.add(fileBean);
                Log.d("tag", data);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return fileBeens;
    }
    
    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath)
    {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }
    
    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>
     *         {@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(File file)
    {
        if (file == null)
            return false;
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists())
            return file.isFile();
        if (!createOrExistsDir(file.getParentFile()))
            return false;
        try
        {
            return file.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>
     *         {@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file)
    {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null
                && (file.exists() ? file.isDirectory() : file.mkdirs());
    }
    
    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables)
    {
        if (closeables == null)
            return;
        try
        {
            for (Closeable closeable : closeables)
            {
                if (closeable != null)
                {
                    closeable.close();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}