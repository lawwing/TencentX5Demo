package cn.lawwing.tencentx5demo;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lawwing on 2017/11/8.
 */

public class FileManager
{
    
    /**
     * 应用程序在SD卡上的主目录名称
     */
    private final static String APP_FOLDER_NAME = "OfficeDemo";
    
    /**
     * 存放图片目录名
     */
    private final static String PHOTO_FOLDER_NAME = "Photos";
    
    private final static String EDIT_PHOTO_FOLDER_NAME = "EditPhotos";
    
    /**
     * 存放录音目录名
     */
    private final static String VOICE_FOLDER_NAME = "Voices";
    
    /**
     * 存放视频
     */
    private final static String VIDEO_FOLDER_NAME = "Videos";
    
    /**
     * 存放下载文件目录名
     */
    private final static String RECEIVE_FILE_FOLDER_NAME = "receiveFiles";
    
    private static String DEFAULT_DISK_CACHE_DIR = "surfond_disk_cache";
    
    public static File getPhotoCacheDir(Context context)
    {
        return getPhotoCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }
    
    /**
     * @param context
     * @param cacheName 缓存文件夹名称
     * @return
     */
    private static File getPhotoCacheDir(Context context, String cacheName)
    {
        File cacheDir = context.getCacheDir();
        if (cacheDir != null)
        {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory()))
            {
                return null;
            }
            
            File noMedia = new File(cacheDir + "/.nomedia");
            if (!noMedia.mkdirs()
                    && (!noMedia.exists() || !noMedia.isDirectory()))
            {
                return null;
            }
            
            return result;
        }
        
        return null;
    }
    
    /**
     * 获取app在sd卡上的主目录
     *
     * @return 成功则返回目录，失败则返回null
     */
    public static File getAppFolder()
    {
        if (SDCardUtils.isSDCardEnable())
        {
            
            File appFolder = new File(Environment.getExternalStorageDirectory(),
                    APP_FOLDER_NAME);
            return createOnNotFound(appFolder);
            
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 获取应用存放图片的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getPhotoFolder()
    {
        File appFolder = getAppFolder();
        if (appFolder != null)
        {
            
            File photoFolder = new File(appFolder, PHOTO_FOLDER_NAME);
            return createOnNotFound(photoFolder);
            
        }
        else
        {
            return null;
        }
    }
    
    public static File createNewFolder(String result)
    {
        File appFolder = getAppFolder();
        if (appFolder != null)
        {
            
            File photoFolder = new File(appFolder, result);
            return createOnNotFound(photoFolder);
            
        }
        else
        {
            return null;
        }
    }
    
    /**
     * @param path 父目录
     * @param result 新建文件夹名称
     * @return
     */
    public static File createNewFolder(String path, String result)
    {
        if (TextUtils.isEmpty(path))
        {
            return null;
        }
        
        File appFolder = new File(path);
        File photoFolder = new File(appFolder, result);
        return createOnNotFound(photoFolder);
    }
    
    /**
     * 获取应用存放编辑（裁剪和旋转）功能产生的cache文件
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getEditPhotoFolder()
    {
        File appFolder = getAppFolder();
        if (appFolder != null)
        {
            
            File photoFolder = new File(appFolder, EDIT_PHOTO_FOLDER_NAME);
            return createOnNotFound(photoFolder);
            
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 创建目录
     *
     * @param folder
     * @return 创建成功则返回目录，失败则返回null
     */
    private static File createOnNotFound(File folder)
    {
        if (folder == null)
        {
            return null;
        }
        
        if (!folder.exists())
        {
            folder.mkdirs();
        }
        
        if (folder.exists())
        {
            return folder;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 获取语音消息的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getVoiceFolder()
    {
        File appFolder = getAppFolder();
        if (appFolder != null)
        {
            
            File photoFolder = new File(appFolder, VOICE_FOLDER_NAME);
            return createOnNotFound(photoFolder);
            
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 获取视频的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getVideoFolder()
    {
        File appFolder = getAppFolder();
        if (appFolder != null)
        {
            
            File photoFolder = new File(appFolder, VIDEO_FOLDER_NAME);
            return createOnNotFound(photoFolder);
            
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 获取接收的附件的目，下载的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getReceiveFolder()
    {
        File appFolder = getAppFolder();
        if (appFolder != null)
        {
            File photoFolder = new File(appFolder, RECEIVE_FILE_FOLDER_NAME);
            return createOnNotFound(photoFolder);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * @param srcPath 源文件夹的路径
     * @param destDir 目标文件所在路径
     * @return true代表复制成功
     */
    public static boolean copyFile(String srcPath, String destDir)
    {
        File file = new File(srcPath);
        if (!file.exists())
        {
            return false;
        }
        
        if (file.isFile())
        {
            /** 源文件是文件 */
            return copyFileLogic(srcPath, destDir, true);
        }
        else if (file.isDirectory())
        {
            /** 源文件是文件夹 */
            return copyDirectory(srcPath, destDir, true);
        }
        
        return false;
    }
    
    /**
     * 对文件的复制操作
     *
     * @param srcPath 源文件的文件路径
     * @param destDir 目标文件的文件路径
     * @param overwriteExistFile 默认覆盖同名文件
     * @return true表示文件复制成功
     */
    public static boolean copyFileLogic(String srcPath, String destDir,
            boolean overwriteExistFile)
    {
        File srcFile = new File(srcPath);
        String fileName = srcFile.getName();
        String destPath = destDir + File.separator + fileName;
        File destFile = new File(destPath);
        if (destFile.getAbsolutePath().equals(srcFile.getAbsolutePath()))
        {
            /** 源文件路径和目标文件路径重复 */
            return false;
        }
        
        if (destFile.exists() && !overwriteExistFile)
        {
            /** 如果目标目录下已有同名文件且不允许覆盖 */
            return false;
        }
        
        File destFileDir = new File(destDir);
        if (!destFileDir.exists() && !destFileDir.mkdirs())
        {
            /** 如果目标目录不存在并且创建目录失败直接返回 */
            return false;
        }
        
        try
        {
            FileInputStream fis = new FileInputStream(srcPath);
            FileOutputStream fos = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int c;
            while ((c = fis.read(buf)) != -1)
            {
                fos.write(buf, 0, c);
            }
            fos.flush();
            fis.close();
            fos.close();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * 对文件夹的复制操作
     *
     * @param srcPath 源文件夹路径
     * @param destDir 目标文件夹路径
     * @param overwriteExistDir 默认是true代表覆盖源文件夹
     * @return true表示文件夹复制成功
     */
    public static boolean copyDirectory(String srcPath, String destDir,
            boolean overwriteExistDir)
    {
        if (destDir.contains(srcPath))
        {
            return false;
        }
        
        File srcFile = new File(srcPath);
        if (!srcFile.exists() || !srcFile.isDirectory())
        {
            return false;
        }
        
        // 获得待复制的文件夹的名字，比如待复制的文件夹为"E:\\dir\\"则获取的名字为"dir"
        String dirName = srcFile.getName();
        
        // 目标文件夹的完整路径
        String destDirPath = destDir + File.separator + dirName
                + File.separator;
        File destDirFile = new File(destDirPath);
        if (destDirFile.getAbsolutePath().equals(srcFile.getAbsolutePath()))
        {
            return false;
        }
        
        if (destDirFile.exists() && destDirFile.isDirectory()
                && !overwriteExistDir)
        { // 目标位置有一个同名文件夹且不允许覆盖同名文件夹，则直接返回false
            return false;
        }
        
        if (!destDirFile.exists() && !destDirFile.mkdirs())
        { // 如果目标目录不存在并且创建目录失败
            return false;
        }
        
        File[] fileList = srcFile.listFiles(); // 获取源文件夹下的子文件和子文件夹
        if (fileList.length == 0)
        { // 如果源文件夹为空目录则直接设置flag为true，这一步非常隐蔽，debug了很久
            return true;
        }
        else
        {
            for (File temp : fileList)
            {
                if (temp.isFile())
                { // 文件
                    return copyFileLogic(temp.getAbsolutePath(),
                            destDirPath,
                            overwriteExistDir); // 递归复制时也继承覆盖属性
                }
                else if (temp.isDirectory())
                { // 文件夹
                    return copyDirectory(temp.getAbsolutePath(),
                            destDirPath,
                            overwriteExistDir); // 递归复制时也继承覆盖属性
                }
                
            }
        }
        
        return false;
    }
    
    /**
     * 删除文件或者文件夹
     *
     * @param path 删除对象的路径
     * @return
     */
    public static boolean deleteFile(String path)
    {
        File file = new File(path);
        if (!file.exists())
        {
            return false;
        }
        
        return file.delete();
    }
    
    public static boolean cropFile(String srcPath, String destDir)
    {
        return copyFile(srcPath, destDir) && deleteFile(srcPath);
    }
    
    public static String getFileSize(Activity activity, File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
            return Formatter.formatFileSize(activity, fis.available());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
