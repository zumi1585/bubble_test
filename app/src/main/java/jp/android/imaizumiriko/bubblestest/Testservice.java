package jp.android.imaizumiriko.bubblestest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class Testservice extends AppCompatActivity {

    private View view;
    private WindowManager windowManager;
    private int dpScale ;

    @Override
    protected void onCreate() {
        super.onCreate();


        // dipを取得
        dpScale = (int)getResources().getDisplayMetrics().density;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){

        //startForegroundService()
        Context context = getApplicationContext();
        String channelId = "default";
        String title = context.getString(R.string.app_name);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context,0,intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(
                channelId,title,NotificationManager.IMPORTANCE_DEFAULT);

        if(notificationManager != null){
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(context.channelId)
                    .setContentTitle(title)
                    //android標準アイコンから
            .setSmallIcon(android.R.drawable.btn_star)
            .setContentText("APPLICATION_OVERLAY")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setWhen(System.currentTimeMillis())
            .build();

            // startForeground
            startForeground(1, notification);


        }
        //----- startForegroundService()

        // inflaterの生成
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        int typeLayer = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        windowManager = (WindowManager)getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                typeLayer,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        //右上に配置
        params.gravity= Gravity.TOP 1 Gravity.END;
    params.x = 20 * dpScale; //20dp
    params.y = 80 * dpScale;//80dp

    //レイアウトファイルからInfalteするViewを作成
    final ViewGroup nullParent = null;
    view = layoutInflater.inflate(R.layout.service_layer,nullParent);

    //ViewにTouchListenerを設定する
        view.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                Log.d("debug","onTouch");
                if(event.getAction() ==MotionEvent.ACTION_DOWN){
                    Log.d("debug","ACTION_DOWN");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Log.d("debug","ACTION_UP");

                    //warning: override performClick()
                    view.performClick();

                    //Serviceを自ら停止させる
                    stopSelf();

                }
                return false;

            }
        });

        // Viewを画面上に追加
        windowManager.addView(view,params);

        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("debug","onDestroy");
        //Viewを削除
        windowManager.removeView(view);
    }

    @Override
    public IBinder onBind(Intent intent){
        //TODO Auto-generated method stub
        return null;
    }
}
