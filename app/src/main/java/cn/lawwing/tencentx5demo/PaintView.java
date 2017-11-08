package cn.lawwing.tencentx5demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/***
 * author lawwing time 2017/3/21 15:27 describe
 **/

public class PaintView extends View
{
    public static final int COMMENT_ARROW = 0;
    
    public static final int COMMENT_TEXT = 1;
    
    public static final int COMMENT_RECTANGLE = 2;
    
    public static final String COLOR_RED = "#ff4351";
    
    public static final String COLOR_BLUE = "#007aff";
    
    public static final String COLOR_GREEN = "#4bd754";
    
    public static final String COLOR_BLACK = "#323232";
    
    private Canvas mCanvas;
    
    private Path mPath;
    
    private Paint mBitmapPaint;
    
    private Bitmap mBitmap;
    
    private Paint mPaint;
    
    private ArrayList<DrawPath> savePath;
    
    private ArrayList<DrawPath> deletePath;
    
    private DrawPath dp;
    
    private float mX, mY;
    
    private static final float TOUCH_TOLERANCE = 4;
    
    private int bitmapWidth;
    
    private int bitmapHeight;
    
    private Bitmap bm;
    
    // 画笔的粗细
    public static int width = 10;
    
    public static int type = COMMENT_ARROW;
    
    public PaintView(Context c)
    {
        super(c);
        // 得到屏幕的分辨率
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        bitmapWidth = dm.widthPixels;
        bitmapHeight = dm.heightPixels;
        
        initCanvas(Color.parseColor(CommentOfficeActivity.color), width);
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
        
    }
    
    public PaintView(Context c, AttributeSet attrs)
    {
        super(c, attrs);
        // 得到屏幕的分辨率
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        bitmapWidth = dm.widthPixels;
        bitmapHeight = dm.heightPixels;
        initCanvas(Color.parseColor(CommentOfficeActivity.color), width);
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
    }
    
    // 初始化画布
    public void initCanvas(int color, float width)
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(width);
        
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        
        // 画布大小
        mBitmap = Bitmap
                .createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
        mCanvas = new Canvas(mBitmap); // 所有mCanvas画的东西都被保存在了mBitmap中
        
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }
    
    public void drawBackground(Bitmap bm)
    {
        this.bm = bm;
        mCanvas.drawBitmap(bm, 0, 0, mPaint);
    }
    
    public void setColorOrType()
    {
        mPaint.setColor(Color.parseColor(CommentOfficeActivity.color));
        type = CommentOfficeActivity.mode;
        // 字体用实心画，矩形和箭头用空心
        if (type == COMMENT_TEXT)
        {
            mPaint.setTextSize(42);
            mPaint.setStrokeWidth(3);
            mPaint.setStyle(Paint.Style.FILL);
        }
        else
        {
            mPaint.setStrokeWidth(width);
            mPaint.setStyle(Paint.Style.STROKE);
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint); // 显示旧的画布
        if (mPath != null)
        {
            // 实时的显示
            canvas.drawPath(mPath, mPaint);
        }
    }
    
    /**
     * 添加文字
     *
     * @param text
     */
    public void setTextArea(String text, float x, float y)
    {
        dp = new DrawPath(mPath, mPaint,
                Color.parseColor(CommentOfficeActivity.color), COMMENT_TEXT,
                text, x, y, 0, 0);
        dp.setDtype(COMMENT_TEXT);
        mPaint.setTextSize(42);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawText(text, x, y, mPaint);
        mPaint.setStrokeWidth(width);
        mPaint.setStyle(Paint.Style.STROKE);
        mCanvas.drawPath(mPath, mPaint);
        savePath.add(dp);
        listener.onRefluse(savePath.size(), deletePath.size());
        mPath = null;
    }
    
    // 路径对象
    class DrawPath
    {
        private Path path;
        
        private Paint paint;
        
        private String dtext;
        
        private int color;
        
        private int dtype;
        
        private float sX;// 开始的x坐标
        
        private float eX;// 结束的x坐标
        
        private float sY;// 开始的y坐标
        
        private float eY;// 结束的y坐标
        
        public DrawPath(Path dpath, Paint dpaint, int dcolor, int dtype,
                String dtext, float sX, float sY, float eX, float eY)
        {
            this.path = dpath;
            this.paint = dpaint;
            this.color = dcolor;
            this.dtype = dtype;
            this.dtext = dtext;
            this.sX = sX;
            this.sY = sY;
            this.eX = eX;
            this.eY = eY;
        }
        
        public int getColor()
        {
            return color;
        }
        
        public void setColor(int color)
        {
            this.color = color;
        }
        
        public String getDtext()
        {
            return dtext;
        }
        
        public void setDtext(String dtext)
        {
            this.dtext = dtext;
        }
        
        public int getDtype()
        {
            return dtype;
        }
        
        public void setDtype(int dtype)
        {
            this.dtype = dtype;
        }
        
        public float geteX()
        {
            return eX;
        }
        
        public void seteX(float eX)
        {
            this.eX = eX;
        }
        
        public float geteY()
        {
            return eY;
        }
        
        public void seteY(float eY)
        {
            this.eY = eY;
        }
        
        public Paint getPaint()
        {
            return paint;
        }
        
        public void setPaint(Paint paint)
        {
            this.paint = paint;
        }
        
        public Path getPath()
        {
            return path;
        }
        
        public void setPath(Path path)
        {
            this.path = path;
        }
        
        public float getsX()
        {
            return sX;
        }
        
        public void setsX(float sX)
        {
            this.sX = sX;
        }
        
        public float getsY()
        {
            return sY;
        }
        
        public void setsY(float sY)
        {
            this.sY = sY;
        }
    }
    
    /**
     * 撤销的核心思想就是将画布清空， 将保存下来的Path路径最后一个移除掉， 重新将路径画在画布上面。
     */
    public void undo()
    {
        if (savePath != null && savePath.size() > 0)
        {
            // 调用初始化画布函数以清空画布
            initCanvas(Color.parseColor(CommentOfficeActivity.color), width);
            drawBackground(bm);
            // 将路径保存列表中的最后一个元素删除 ,并将其保存在路径删除列表中
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            deletePath.add(drawPath);
            savePath.remove(savePath.size() - 1);
            
            listener.onRefluse(savePath.size(), deletePath.size());
            // 将路径保存列表中的路径重绘在画布上
            for (DrawPath dp : savePath)
            {
                if (dp.getDtype() == COMMENT_TEXT)
                {
                    mPaint.setTextSize(42);
                    mPaint.setStrokeWidth(3);
                    mPaint.setColor(dp.getColor());
                    mPaint.setStyle(Paint.Style.FILL);
                    mCanvas.drawText(dp.getDtext(),
                            dp.getsX(),
                            dp.getsY(),
                            mPaint);
                }
                else
                {
                    mPaint.setColor(dp.getColor());
                    mPaint.setStrokeWidth(width);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mCanvas.drawPath(dp.getPath(), mPaint);
                }
            }
            invalidate();// 刷新
            
            setColorOrType();
        }
    }
    
    /**
     * 恢复的核心思想就是将撤销的路径保存到另外一个列表里面(栈)， 然后从redo的列表里面取出最顶端对象， 画在画布上面即可
     */
    public void redo()
    {
        if (deletePath.size() > 0)
        {
            // 将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp1 = deletePath.get(deletePath.size() - 1);
            drawBackground(bm);
            savePath.add(dp1);
            // 将路径保存列表中的路径重绘在画布上
            for (DrawPath dp : savePath)
            {
                if (dp.getDtype() == COMMENT_TEXT)
                {
                    mPaint.setTextSize(42);
                    mPaint.setStrokeWidth(3);
                    mPaint.setColor(dp.getColor());
                    mPaint.setStyle(Paint.Style.FILL);
                    mCanvas.drawText(dp.getDtext(),
                            dp.getsX(),
                            dp.getsY(),
                            mPaint);
                }
                else
                {
                    mPaint.setColor(dp.getColor());
                    mPaint.setStrokeWidth(width);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mCanvas.drawPath(dp.getPath(), mPaint);
                }
            }
            // 将该路径从删除的路径列表中去除
            deletePath.remove(deletePath.size() - 1);
            listener.onRefluse(savePath.size(), deletePath.size());
            invalidate();
            setColorOrType();
        }
    }
    
    /*
     * 清空的主要思想就是初始化画布 将保存路径的两个List清空
     */
    public void removeAllPaint()
    {
        // 调用初始化画布函数以清空画布
        initCanvas(Color.parseColor(CommentOfficeActivity.color), width);
        drawBackground(bm);
        invalidate();// 刷新
        savePath.clear();
        deletePath.clear();
    }
    
    /*
     * 保存所绘图形 返回绘图文件的存储路径
     */
    public String saveBitmap()
    {
        // 获得系统当前时间，并以该时间作为文件名
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss",
                java.util.Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        String paintPath = "";
        str = str + "paint.png";
        File dir = new File("/sdcard/notes/");
        File file = new File("/sdcard/notes/", str);
        if (!dir.exists())
        {
            dir.mkdir();
        }
        else
        {
            if (file.exists())
            {
                file.delete();
            }
        }
        
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            // 保存绘图文件路径
            paintPath = "/sdcard/notes/" + str;
            
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return paintPath;
    }
    
    private void touch_start(float x, float y)
    {
        mPath.reset();// 清空path
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                switch (type)
                {
                    case COMMENT_ARROW:
                        mPath = new Path();
                        touch_start(x, y);
                        invalidate(); // 清屏
                        break;
                    case COMMENT_RECTANGLE:
                        mPath = new Path();
                        touch_start(x, y);
                        invalidate(); // 清屏
                        break;
                    case COMMENT_TEXT:
                        mPath = new Path();
                        touch_start(x, y);
                        invalidate(); // 清屏
                        break;
                }
                
                break;
            case MotionEvent.ACTION_MOVE:
                switch (type)
                {
                    case COMMENT_ARROW:
                        toach_move_arrow(x, y);
                        invalidate();
                        break;
                    case COMMENT_RECTANGLE:
                        toach_move_rectangle(x, y);
                        invalidate();
                        break;
                    case COMMENT_TEXT:
                        
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                switch (type)
                {
                    case COMMENT_ARROW:
                        toach_up_arrow(x, y);
                        invalidate();
                        break;
                    case COMMENT_RECTANGLE:
                        toach_up_rectangle(x, y);
                        invalidate();
                        break;
                    case COMMENT_TEXT:
                        listener.onClickText(x, y);
                        break;
                }
                break;
        }
        return true;
    }
    
    /**
     * 过程中的箭头
     */
    private void toach_move_arrow(float ex, float ey)
    {
        mPath.reset();
        double H = 20; // 箭头高度
        double L = 8; // 底边的一半
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        double awrad = Math.atan(L / H); // 箭头角度
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] arrXY_1 = rotateVec(ex - mX, ey - mY, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(ex
                - mX, ey - mY, -awrad, true, arraow_len);
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
        double y_3 = ey - arrXY_1[1];
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
        double y_4 = ey - arrXY_2[1];
        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        // 画线
        mPath.moveTo(mX, mY);
        mPath.lineTo(ex, ey);
        // 画
        mPath.moveTo(ex, ey);
        mPath.lineTo(x3, y3);
        mPath.lineTo(x4, y4);
        mPath.lineTo(ex, ey);
    }
    
    /**
     * 过程中的矩形
     *
     * @param x
     * @param y
     */
    private void toach_move_rectangle(float x, float y)
    {
        mPath.reset();
        mPath.moveTo(mX, mY);
        mPath.lineTo(x, mY);
        mPath.lineTo(x, y);
        mPath.lineTo(mX, y);
        mPath.lineTo(mX, mY);
    }
    
    /**
     * 画矩形
     *
     * @param x
     * @param y
     */
    private void toach_up_rectangle(float x, float y)
    {
        dp = new DrawPath(mPath, mPaint,
                Color.parseColor(CommentOfficeActivity.color),
                COMMENT_RECTANGLE, "", mX, mY, x, y);
        dp.setDtype(COMMENT_RECTANGLE);
        mCanvas.drawPath(mPath, mPaint);
        savePath.add(dp);
        listener.onRefluse(savePath.size(), deletePath.size());
        mPath = null;
    }
    
    /**
     * 离开屏幕画一个箭头
     */
    private void toach_up_arrow(float ex, float ey)
    {
        dp = new DrawPath(mPath, mPaint,
                Color.parseColor(CommentOfficeActivity.color), COMMENT_ARROW,
                "", mX, mY, ex, ey);
        dp.setDtype(COMMENT_ARROW);
        mCanvas.drawPath(mPath, mPaint);
        savePath.add(dp);
        listener.onRefluse(savePath.size(), deletePath.size());
        mPath = null;
    }
    
    // 计算
    public double[] rotateVec(float px, float py, double ang, boolean isChLen,
            double newLen)
    {
        double mathstr[] = new double[2];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen)
        {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }
    
    private IPaintViewListener listener;
    
    public void setListener(IPaintViewListener listener)
    {
        this.listener = listener;
    }
    
    public interface IPaintViewListener
    {
        void onClickText(float x, float y);
        
        // 当path有变化时候，用于控制撤销重做图标,num为当前的条数，deletenum为删除的条数
        void onRefluse(int num, int deletenum);
    }
    
}
