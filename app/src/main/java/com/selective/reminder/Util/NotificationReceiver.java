package com.selective.reminder.Util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.selective.reminder.R;
import com.selective.reminder.UI.introActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int alarmNo = intent.getIntExtra("alarm_no", 1);     // 알람번호
        String title = intent.getStringExtra("title");                  // 제목
        String message = intent.getStringExtra("message");              // 메시지

        // 알림을 클릭하면 IntroActivity 호출
        Intent appIntent = new Intent(context, introActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(context, alarmNo, appIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);

        String channelId = "notificationId";

        // api 26 이전에 사용할 사운드
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // 알림 Builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)      // 상태바에 표시할 아이콘
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // 소리 + 팝업(헤드 업)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // api 26 이상부터는 channel 이 필요함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel == null) {
                // 기본 채널 생성 (IMPORTANCE_HIGH : 소리 + 팝업(헤드 업))
                channel = new NotificationChannel(channelId, "channelName", NotificationManager.IMPORTANCE_HIGH);

                notificationManager.createNotificationChannel(channel);
            }
        }

        // 알림 표시
        notificationManager.notify(alarmNo, notificationBuilder.build());
    }
}