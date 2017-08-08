package com.zhuoxin.treasure;


import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoxin.treasure.commons.ActivityUtils;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Administrator on 2017/7/31.
 */

public class MainMp4Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    private TextureView textureView;
    private ActivityUtils activityUtils;
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         /*
        * MediaPlayer播放视频
        * TextureView用于展示播放的视频
        *
        * 何时播放视频
        * TextureView
        * */
        textureView = new TextureView(getContext());
        return textureView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityUtils = new ActivityUtils(this);
         /*
        * TextureView何时准备好
        * 添加监听
        * */
        textureView.setSurfaceTextureListener(this);
    }
    //-----------------------------------------TextureView监听实现的方法----------------------------------
    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
         /*
        * TextureView准备好了
        * 1、获取视频资源
        * 2、初始化视频播放控件，即MediaPlayer
        * 3、将视频资源设置给视频播放控件
        * 4、将视频添加到TextureView进行播放
        * */
        try {
            AssetFileDescriptor assetFileDescriptor =getContext().getAssets().openFd("welcome.mp4");
            FileDescriptor fileDescriptor = assetFileDescriptor.getFileDescriptor();
            //2、初始化视频播放控件，即MediaPlayer
            mediaPlayer = new MediaPlayer();
            //3、将视频资源设置给视频播放控件
            mediaPlayer.setDataSource(fileDescriptor,assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
            //异步准备
            mediaPlayer.prepareAsync();
            //设置监听，监听MediaPlayer什么时候准备好了
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //4、将视频添加到TextureView进行播放
                    mediaPlayer.setSurface(new Surface(surface));
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            });

        } catch (IOException e) {
            activityUtils.showToast("视频播放失败："+e.getMessage());
        }
    }
    //当TextureView尺寸发生改变的时候执行
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    //当TextureView销毁的时候
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }
    //更新的时候

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
}
