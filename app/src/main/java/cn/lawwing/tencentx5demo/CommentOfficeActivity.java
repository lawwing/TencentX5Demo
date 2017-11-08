package cn.lawwing.tencentx5demo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentOfficeActivity extends AppCompatActivity
        implements PaintView.IPaintViewListener
{
    @BindView(R.id.pv)
    PaintView pv;
    
    @BindView(R.id.iv_comment_loadmorecolor)
    ImageView mLoadMoreColor;
    
    @BindView(R.id.iv_comment_loadmorerightmenu)
    ImageView mLoadMoreMenu;
    
    @BindView(R.id.iv_comment_redcolor)
    ImageView mRedColor;
    
    @BindView(R.id.iv_comment_greencolor)
    ImageView mGreenColor;
    
    @BindView(R.id.iv_comment_blackcolor)
    ImageView mBlackColor;
    
    @BindView(R.id.iv_comment_bluecolor)
    ImageView mBlueColor;
    
    @BindView(R.id.iv_comment_arrow)
    ImageView mArrow;
    
    @BindView(R.id.iv_comment_text)
    ImageView mText;
    
    @BindView(R.id.iv_comment_rectangle)
    ImageView mRectangle;
    
    @BindView(R.id.iv_cancle)
    ImageView mCancleIV;
    
    @BindView(R.id.iv_redo)
    ImageView mRedoIV;
    
    @BindView(R.id.exit)
    TextView mExit;
    
    @BindView(R.id.complete)
    TextView mComplete;
    
    @BindView(R.id.ll_leftcolor)
    LinearLayout mLeftColorLayout;
    
    @BindView(R.id.ll_rightmenu)
    LinearLayout mRightMenuLayout;
    
    private Bitmap bm;
    
    // 选择的颜色
    public static String color;
    
    // 画笔的粗细
    //
    
    // 当前的类型，箭头，文字，矩形框
    public static int mode;
    
    // 保存图片的路径
    private String path = "";
    
    // 保存图片的bitmap
    private Bitmap bitmap;
    
    public static Intent newIntance(Activity activity, byte[] bitmapByte)
    {
        Intent intent = new Intent(activity, CommentOfficeActivity.class);
        intent.putExtra("bitmap", bitmapByte);
        return intent;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getIntentData();
        initData();
        setContentView(R.layout.activity_comment_office);
        ButterKnife.bind(this);
        pv.drawBackground(bm);
        pv.setListener(this);
        // initView();
    }
    
    /**
     * 初始化数据
     */
    private void initData()
    {
        color = PaintView.COLOR_RED;
        mode = PaintView.COMMENT_ARROW;
    }
    
    // private void initView()
    // {
    // mBackgroundIV.setImageBitmap(bm);
    // }
    
    private void getIntentData()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            byte[] bis = intent.getByteArrayExtra("bitmap");
            bm = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        }
        
    }
    
    /**
     * 顶部菜单的相关点击事件
     *
     * @param view
     */
    @OnClick({ R.id.exit, R.id.complete, R.id.iv_redo, R.id.iv_cancle })
    public void onTopClick(View view)
    {
        switch (view.getId())
        {
            case R.id.exit:
                initDialog();
                break;
            case R.id.complete:
                // 完成的点击事件
                showMyDialog();
                break;
            case R.id.iv_redo:
                // 重做,有bug
                pv.redo();
                break;
            case R.id.iv_cancle:
                // 撤销
                pv.undo();
                break;
        }
    }
    
    /**
     * 取消的弹框
     */
    private void initDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("是否退出当前页面？退出将会丢失您的备注");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        builder.show();
        
    }
    
    private Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = null;
        try
        {
            int width = view.getWidth();
            int height = view.getHeight();
            if (width != 0 && height != 0)
            {
                bitmap = Bitmap
                        .createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.layout(0, 0, width, height);
                view.draw(canvas);
            }
        }
        catch (Exception e)
        {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }
    
    /**
     * 点击完成的操作
     */
    private void showMyDialog()
    {
        new AlertDialog.Builder(this).setTitle("菜单")
                .setItems(new String[] { "保存到本地相册" },
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                switch (which)
                                {
                                    case 0:
                                        // 保存到本地相册
                                        bitmap = getBitmapFromView(pv);
                                        path = saveImageFile();
                                        // 保存
                                        if (!TextUtils.isEmpty(path))
                                        {
                                            showShortToast("保存成功");
                                        }
                                        else
                                        {
                                            showShortToast("保存失败");
                                        }
                                        break;
                                }
                            }
                        })
                .show();
    }
    
    private void showShortToast(String content)
    {
        Toast.makeText(CommentOfficeActivity.this, content, Toast.LENGTH_LONG)
                .show();
    }
    
    /**
     * 颜色相关的点击事件
     *
     * @param view
     */
    @OnClick({ R.id.iv_comment_loadmorecolor, R.id.iv_comment_bluecolor,
            R.id.iv_comment_redcolor, R.id.iv_comment_greencolor,
            R.id.iv_comment_blackcolor })
    public void onColorClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_comment_loadmorecolor:
                // 收起颜色面板
                changeColorPanal();
                break;
            case R.id.iv_comment_bluecolor:
                color = PaintView.COLOR_BLUE;
                initColorView(color);
                pv.setColorOrType();
                break;
            case R.id.iv_comment_redcolor:
                color = PaintView.COLOR_RED;
                initColorView(color);
                pv.setColorOrType();
                break;
            case R.id.iv_comment_greencolor:
                color = PaintView.COLOR_GREEN;
                initColorView(color);
                pv.setColorOrType();
                break;
            case R.id.iv_comment_blackcolor:
                color = PaintView.COLOR_BLACK;
                initColorView(color);
                pv.setColorOrType();
                break;
        }
    }
    
    /**
     * 初始化颜色的选择
     *
     * @param color
     */
    private void initColorView(String color)
    {
        if (color.equals(PaintView.COLOR_BLACK))
        {
            mBlackColor.setImageResource(R.mipmap.comment_black_select);
            mBlueColor.setImageResource(R.mipmap.comment_blue_unselect);
            mRedColor.setImageResource(R.mipmap.comment_red_unselect);
            mGreenColor.setImageResource(R.mipmap.comment_green_unselect);
        }
        else if (color.equals(PaintView.COLOR_BLUE))
        {
            mBlackColor.setImageResource(R.mipmap.comment_black_unselect);
            mBlueColor.setImageResource(R.mipmap.comment_blue_select);
            mRedColor.setImageResource(R.mipmap.comment_red_unselect);
            mGreenColor.setImageResource(R.mipmap.comment_green_unselect);
        }
        else if (color.equals(PaintView.COLOR_RED))
        {
            mBlackColor.setImageResource(R.mipmap.comment_black_unselect);
            mBlueColor.setImageResource(R.mipmap.comment_blue_unselect);
            mRedColor.setImageResource(R.mipmap.comment_red_select);
            mGreenColor.setImageResource(R.mipmap.comment_green_unselect);
        }
        else if (color.equals(PaintView.COLOR_GREEN))
        {
            mBlackColor.setImageResource(R.mipmap.comment_black_unselect);
            mBlueColor.setImageResource(R.mipmap.comment_blue_unselect);
            mRedColor.setImageResource(R.mipmap.comment_red_unselect);
            mGreenColor.setImageResource(R.mipmap.comment_green_select);
        }
    }
    
    /**
     * 右侧菜单栏的点击事件
     *
     * @param view
     */
    @OnClick({ R.id.iv_comment_loadmorerightmenu, R.id.iv_comment_arrow,
            R.id.iv_comment_rectangle, R.id.iv_comment_text })
    public void onRightMenuClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_comment_loadmorerightmenu:
                // 收起面板
                changeRightPanal();
                break;
            case R.id.iv_comment_arrow:
                mode = PaintView.COMMENT_ARROW;
                initRMenuView(mode);
                pv.setColorOrType();
                break;
            case R.id.iv_comment_rectangle:
                mode = PaintView.COMMENT_RECTANGLE;
                initRMenuView(mode);
                pv.setColorOrType();
                break;
            case R.id.iv_comment_text:
                mode = PaintView.COMMENT_TEXT;
                initRMenuView(mode);
                pv.setColorOrType();
                break;
        }
    }
    
    /**
     * 点击不同的模式（矩形，文字，箭头）
     *
     * @param mode
     */
    private void initRMenuView(int mode)
    {
        switch (mode)
        {
            case PaintView.COMMENT_ARROW:
                mArrow.setImageResource(R.mipmap.comment_arrow_select);
                mText.setImageResource(R.mipmap.comment_text_unselect);
                mRectangle
                        .setImageResource(R.mipmap.comment_rectangle_unselect);
                break;
            case PaintView.COMMENT_TEXT:
                mArrow.setImageResource(R.mipmap.comment_arrow_unselect);
                mText.setImageResource(R.mipmap.comment_text_select);
                mRectangle
                        .setImageResource(R.mipmap.comment_rectangle_unselect);
                break;
            case PaintView.COMMENT_RECTANGLE:
                mArrow.setImageResource(R.mipmap.comment_arrow_unselect);
                mText.setImageResource(R.mipmap.comment_text_unselect);
                mRectangle.setImageResource(R.mipmap.comment_rectangle_select);
                break;
        }
    }
    
    /**
     * 控制右边菜单栏的显示隐藏
     */
    private void changeRightPanal()
    {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation animation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(500);
        set.addAnimation(animation);
        mLoadMoreMenu.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                if (mRightMenuLayout.getVisibility() == View.VISIBLE)
                {
                    TranslateAnimation animation1 = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 1f);
                    LayoutAnimation(animation1, mRightMenuLayout);
                }
                else
                {
                    mRightMenuLayout.setVisibility(View.INVISIBLE);
                    TranslateAnimation animation2 = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 1f,
                            Animation.RELATIVE_TO_SELF, 0f);
                    LayoutAnimation(animation2, mRightMenuLayout);
                }
            }
            
            @Override
            public void onAnimationEnd(Animation animation)
            {
                if (mRightMenuLayout.getVisibility() == View.VISIBLE)
                {
                    mLoadMoreMenu.setImageResource(R.mipmap.comment_opentools);
                    mRightMenuLayout.setVisibility(View.GONE);
                }
                else
                {
                    mLoadMoreMenu.setImageResource(R.mipmap.comment_closetools);
                    mRightMenuLayout.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onAnimationRepeat(Animation animation)
            {
                
            }
        });
        
    }
    
    /**
     * 控制上下的动画
     *
     * @param animation1
     * @param view
     */
    private void LayoutAnimation(TranslateAnimation animation1, View view)
    {
        AnimationSet set1 = new AnimationSet(true);
        animation1.setDuration(500);
        set1.addAnimation(animation1);
        view.startAnimation(animation1);
    }
    
    /**
     * 控制颜色面板收
     */
    private void changeColorPanal()
    {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation animation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(500);
        set.addAnimation(animation);
        mLoadMoreColor.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                if (mLeftColorLayout.getVisibility() == View.VISIBLE)
                {
                    TranslateAnimation animation1 = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 1f);
                    LayoutAnimation(animation1, mLeftColorLayout);
                }
                else
                {
                    mLeftColorLayout.setVisibility(View.INVISIBLE);
                    TranslateAnimation animation2 = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 1f,
                            Animation.RELATIVE_TO_SELF, 0f);
                    LayoutAnimation(animation2, mLeftColorLayout);
                }
            }
            
            @Override
            public void onAnimationEnd(Animation animation)
            {
                if (mLeftColorLayout.getVisibility() == View.VISIBLE)
                {
                    mLoadMoreColor
                            .setImageResource(R.mipmap.comment_colour_pop);
                    mLeftColorLayout.setVisibility(View.GONE);
                }
                else
                {
                    mLoadMoreColor
                            .setImageResource(R.mipmap.comment_colour_back);
                    mLeftColorLayout.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onAnimationRepeat(Animation animation)
            {
                
            }
        });
        
    }
    
    // 转发消息给别人,转跳到选择联系人页面
    private void sendToOther()
    {
        /*
         * Intent intent = ChooseUserCardInfoActivity
         * .newInstance(CommentOfficeActivity.this, "Transmit");
         * startActivityForResult(intent, CHOOSE_SENDOTHER_REQUEST_INFO);
         * overridePendingTransitionEnter();
         */
    }
    
    /**
     * 保存bitmap到文件，返回路径
     *
     * @return
     */
    private String saveImageFile()
    {
        if (null != bitmap)
        {
            long time = TimeUtils.getCurTimeMills();
            if (ImageUtils.save(bitmap,
                    FileManager.getPhotoFolder().getPath() + "/lawwing/" + time
                            + ".jpg",
                    Bitmap.CompressFormat.JPEG))
            {
                // showShortToast("保存图片成功");
                return FileManager.getPhotoFolder().getPath() + "/lawwing/"
                        + time + ".jpg";
            }
            else
            {
                // showShortToast("保存图片失败");
                return "";
            }
        }
        else
        {
            // showShortToast("保存图片失败null");
            return "";
        }
        
    }
    
    @Override
    public void onClickText(final float x, final float y)
    {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入批注内容")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (!TextUtils.isEmpty(editText.getText().toString()))
                        {
                            pv.setTextArea(editText.getText().toString(), x, y);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    
    @Override
    public void onRefluse(int num, int deletenum)
    {
        if (num == 0)
        {
            mCancleIV.setImageResource(R.mipmap.comment_cancle_cannot);
        }
        else
        {
            mCancleIV.setImageResource(R.mipmap.comment_cancle);
        }
        
        if (deletenum == 0)
        {
            mRedoIV.setImageResource(R.mipmap.comment_redo_cannot);
        }
        else
        {
            mRedoIV.setImageResource(R.mipmap.comment_redo);
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // 拦截返回键
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            initDialog();
        }
        return super.onKeyDown(keyCode, event);
    }
}
