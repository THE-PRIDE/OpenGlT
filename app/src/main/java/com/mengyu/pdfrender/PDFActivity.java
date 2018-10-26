package com.mengyu.pdfrender;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meng.openglt.R;

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
        mFilePath = Environment.getExternalStorageDirectory() + "/lmy/PDF/newpdf.pdf";
        //下载文件方法
//        downMenuServerFile(url ,mFilePath,handler);
        initPDF();
    }

    void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    handleRemainData(bitmapList.size());
                }
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
        } catch (IOException e) {
            return;
        }
        int size = mPdfRenderer.getPageCount() - 1 >= 6 ? 6 : mPdfRenderer.getPageCount() - 1;
        int i = 0;
        while (i <= size) {
            mCurrentPage = mPdfRenderer.openPage(i);
            //Bitmap必须是ARGB，不可以是RGB
            Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(),
                    Bitmap.Config.ARGB_8888);
            /*
             * 调用PdfRender.Page的render方法渲染bitmap
             *
             * render的参数说明：
             * destination : 要渲染的bitmap对象
             * destClip ：传一个矩形过去 矩形的尺寸不能大于bitmap的尺寸 最后渲染的pdf会是rect的大小 可为null
             * transform : 一个Matrix bitmap根据该Matrix图像进行转换
             * renderMode ：渲染模式 可选2种 RENDER_MODE_FOR_DISPLAY 和 RENDER_MODE_FOR_PRINT
             */
            mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            bitmapList.add(bitmap);
            if (null != mCurrentPage) {
                mCurrentPage.close();
            }
            i++;
        }
        recycleAdapter = new NormalAdapter(bitmapList);
        recyclerView.setAdapter(recycleAdapter);
    }

    void handleRemainData(int position) {
        if (position > mPdfRenderer.getPageCount() - 1) {
            return;
        }
        int size = position + 6;//每次加载6页
        while (position <= mPdfRenderer.getPageCount() - 1 && position < size) {//当前加载页，不大于总页数，且不大于分页加载数
            mCurrentPage = mPdfRenderer.openPage(position);
            //Bitmap必须是ARGB，不可以是RGB
            Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(),
                    Bitmap.Config.ARGB_8888);
            mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            bitmapList.add(bitmap);
            if (null != mCurrentPage) {
                mCurrentPage.close();
            }
            position++;
        }
        recycleAdapter.notifyData(bitmapList);
    }


    private void closeRenderer() throws IOException {
        if (null != mCurrentPage) {
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
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
