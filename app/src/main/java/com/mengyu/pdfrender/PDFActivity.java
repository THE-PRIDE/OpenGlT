package com.mengyu.pdfrender;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.meng.openglt.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * PDF文件展示
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PDFActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PdfRenderer mPdfRenderer;
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer.Page mCurrentPage;
    private NormalAdapter recycleAdapter;
    private List<Bitmap> bitmapList;
    private String mFilePath;

    private int width = 0;
    private int height = 0;
    Bitmap bitmap;

    private ImageView mImgShowPDF;
    private int mCurrentPager = 0;

    private float mCurrentX;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf_main);
        initView();
        initData();
        initListener();
    }

    void initView() {
        recyclerView = findViewById(R.id.recycler);
        mImgShowPDF = findViewById(R.id.img_pdf_show);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
//设置Adapter
        //设置分隔线
//        recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
//设置增加或删除条目的动画
//        recyclerView.setItemAnimator( new DefaultItemAnimator());

//        recycleAdapter.notifyDataSetChanged();

    }

    void initData() {
//        mFilePath = Environment.getExternalStorageDirectory() + "/lmy/PDF/newpdf.pdf";
        mFilePath = Environment.getExternalStorageDirectory() + "/A5C002ht.pdf";
        //下载文件方法
//        downMenuServerFile(url ,mFilePath,handler);
        initPDF();
    }

    @SuppressLint("ClickableViewAccessibility")
    void initListener() {
//        mImgShowPDF.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//
//                    case MotionEvent.ACTION_DOWN:
//                        mCurrentX = event.getX();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if ((event.getX() - mCurrentX) < 0 && Math.abs(event.getX() - mCurrentX) > 10 && (mCurrentPager > 0)) {
//                            mCurrentPager = mCurrentPager - 1;
//                            loadPrePager();
//                        } else if ((event.getX() - mCurrentX) < 0 && Math.abs(event.getX() - mCurrentX) > 10 && (mCurrentPager < mPdfRenderer.getPageCount() - 1)) {
//                            mCurrentPager = mCurrentPager + 1;
//                            loadPrePager();
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                RecyclerView.SCROLL_STATE_IDLE  滚动停止
//                RecyclerView.SCROLL_STATE_DRAGGING 正在被外部拖拽，一般为用户手动滚动
//                RecyclerView.SCROLL_STATE_SETTLING 自动滚动开始
                Log.e("TEST", "AAA" + recyclerView.getScrollState());
                Log.e("TEST", "BBB" + newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    handleRemainData(bitmapList.size());
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 初始化PDF，当PDF存在时
     */
    private void initPDF() {
        try {
            bitmapList = new ArrayList<>();
            File file = new File(mFilePath);
            mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
            loadPrePager();
        } catch (IOException e) {
//            return;
        }
//        int size = mPdfRenderer.getPageCount() - 1 >= 3 ? 3 : mPdfRenderer.getPageCount() - 1;
//        int i = 0;
//        mCurrentPage = mPdfRenderer.openPage(i);
//        width = mCurrentPage.getWidth();
//        height = mCurrentPage.getHeight();
//        mCurrentPage.close();
//         while (i <= 3) {
//            mCurrentPage = mPdfRenderer.openPage(i);
//            //Bitmap必须是ARGB，不可以是RGB
//            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            /*
//             * 调用PdfRender.Page的render方法渲染bitmap
//             *
//             * render的参数说明：
//             * destination : 要渲染的bitmap对象
//             * destClip ：传一个矩形过去 矩形的尺寸不能大于bitmap的尺寸 最后渲染的pdf会是rect的大小 可为null
//             * transform : 一个Matrix bitmap根据该Matrix图像进行转换
//             * renderMode ：渲染模式 可选2种 RENDER_MODE_FOR_DISPLAY 和 RENDER_MODE_FOR_PRINT
//             */
//            mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//
//            Bitmap bitmap1 = bitmap;
//            bitmap.recycle();
//            bitmapList.add(bitmap1);
//            if (null != mCurrentPage) {
//                mCurrentPage.close();
//            }
//            i++;
//        }
//        recycleAdapter = new NormalAdapter(bitmapList);
//        recyclerView.setAdapter(recycleAdapter);
    }

    void handleRemainData(int position) {
        if (position > mPdfRenderer.getPageCount() - 1) {
//            if (bitmap != null) bitmap.recycle();
            return;
        }
        Bitmap bitmap = null;
        int size = position + 1;//每次加载6页
        while (position <= mPdfRenderer.getPageCount() - 1 && position < size) {//当前加载页，不大于总页数，且不大于分页加载数
            mCurrentPage = mPdfRenderer.openPage(position);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //Bitmap必须是ARGB，不可以是RGB
            mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            bitmapList.add(bitmap);
            if (null != mCurrentPage) {
                mCurrentPage.close();
            }
            position++;
        }
        if (null != bitmap) {
            bitmap.recycle();
        }
//        bitmap.recycle();
//        bitmap = null;
        recycleAdapter.notifyData(bitmapList);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mCurrentX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if ((event.getX() - mCurrentX) > 0 && Math.abs(event.getX() - mCurrentX) > 10 && (mCurrentPager > 0)) {
                    mCurrentPager = mCurrentPager - 1;
                    loadPrePager();
                } else if ((event.getX() - mCurrentX) < 0 && Math.abs(event.getX() - mCurrentX) > 10 && (mCurrentPager < mPdfRenderer.getPageCount() - 1)) {
                    mCurrentPager = mCurrentPager + 1;
                    loadPrePager();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void loadPrePager() {
        if (null != mCurrentPage) {//加载前，先回收
            bitmap.recycle();
            mCurrentPage.close();
        }
        mCurrentPage = mPdfRenderer.openPage(mCurrentPager);
        width = mCurrentPage.getWidth();
        height = mCurrentPage.getHeight();
        //Bitmap必须是ARGB，不可以是RGB
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        mImgShowPDF.setImageBitmap(bitmap);
    }

    private void loadNextPager() {

    }

    private void closeRenderer() throws IOException {
        if (null != mCurrentPage) {//加载前，先回收
            bitmap.recycle();
            mCurrentPage.close();
        }
        if (null != mPdfRenderer) {
            mPdfRenderer.close();
        }
        if (null != mFileDescriptor) {
            mFileDescriptor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        for (Bitmap bitmap : bitmapList) {
//            bitmap.recycle();
//        }
//        bitmapList.clear();
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
