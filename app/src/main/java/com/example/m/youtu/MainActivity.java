package com.example.m.youtu;

import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.ScaleType;
import org.salient.artplayer.VideoView;
import org.salient.artplayer.ui.ControlPanel;

public class MainActivity extends AppCompatActivity implements YouTuDraggingView.Callback {

    private VideoView mVideoView;
    private YouTuDraggingView mYouTuDraggingView;
    YoutubeControlPanel controlPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(ArrayAdapter.createFromResource(this, R.array.program_list, android.R.layout.simple_list_item_1));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playVideo();
            }
        });
        mVideoView = findViewById(R.id.videoView);
        mYouTuDraggingView = findViewById(R.id.youtube_view);
        mYouTuDraggingView.setCallback(this);

        controlPanel = new YoutubeControlPanel(this);
        mVideoView.setControlPanel(controlPanel);
        controlPanel.getFullScreenIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYouTuDraggingView.fullScreenChange();
            }
        });
        controlPanel.getDownIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYouTuDraggingView.fullScreenGoMin();
            }
        });
    }

    private void playVideo() {
        mYouTuDraggingView.show();
        mVideoView.setUp("http://vfx.mtime.cn/Video/2018/06/01/mp4/180601113115887894.mp4");
        mVideoView.start();
        MediaPlayerManager.instance().setScreenScale(ScaleType.SCALE_CENTER_CROP);
    }

    @Override
    protected void onDestroy() {
        MediaPlayerManager.instance().releaseMediaPlayer();
        super.onDestroy();
    }

    @Override
    public void onIconClick(YouTuDraggingView.IconType iconType) {
        if (iconType == YouTuDraggingView.IconType.CLOSE) {
            if (mVideoView.isCurrentPlaying()) {
                mVideoView.pause();
            }
        } else if (iconType == YouTuDraggingView.IconType.PAUSE) {
            if (mVideoView.isCurrentPlaying())
                mVideoView.pause();
        } else if (iconType == YouTuDraggingView.IconType.PLAY) {
            if (mVideoView.isCurrentPlaying() && MediaPlayerManager.instance().isPlaying())
                return;
            mVideoView.start();
        }
    }

    @Override
    public void status(int status) {
        controlPanel.setVisibility(status == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onVideoViewHide() {

    }

    @Override
    public void videoSize(int width, int height) {
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mYouTuDraggingView.getNowStateScale() == 1f) {
            mYouTuDraggingView.goMin();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
