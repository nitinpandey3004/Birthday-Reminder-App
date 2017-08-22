package com.nitin.birthdayremainder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    String PREFERENCE_SOUND = "notifications_new_message_ringtone";
    Context context_this ,context_this1;



    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Log","Birthday Remainder");

        Calendar c =Calendar.getInstance();
       // System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df= new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = df.format(c.getTime());

        String current[] = currentDate.split("-");
        int currentDay = Integer.parseInt(current[0]);
        int currentMonth = Integer.parseInt(current[1]);

        DatabaseHandler handler = new DatabaseHandler(context);
        context_this1=context;
        ArrayList<Friend> friendArrayList = handler.getAllFriends();

        for(Friend friend :friendArrayList){
           // System.out.println("Current date"+currentDate+"Birth Date :"+friend.getDob());

            String friendDob[]=friend.getDob().split("-");
            int friendDay = Integer.parseInt(friendDob[0]);
            int friendMonth = Integer.parseInt(friendDob[1]);

            if(currentDay == friendDay && currentMonth == friendMonth){
                sendBirthdayNotification(context , friend.getName());
            }
        }
    }

    public  void sendBirthdayNotification(Context context , String name){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context,NotificationActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context_this = context;

        PendingIntent pendingIntent = PendingIntent.getActivity(context , 100 ,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

/*
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String strRingtonePreference = sharedPreferences.getString(PREFERENCE_SOUND, "name");
        Uri defaultSoundUri = Uri.parse(strRingtonePreference);
        */




        if(MainActivity.flag_notify_final == 1) {

            alarm();
            if (MainActivity.flag_vibrate_final == 1) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_action_name1)
                        .setContentTitle("BirthDay Reminder")
                        .setContentText("It is " + name + "'s BirthDay Today!")
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                notificationManager.notify(100, builder.build());
            } else {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_action_name1)
                        .setContentTitle("BirthDay Reminder")
                        .setContentText("It is " + name + "'s BirthDay Today!")
                        .setAutoCancel(true);
                notificationManager.notify(100, builder.build());
            }
        }


              //  .setSound(defaultSoundUri);
/*
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.icon_transperent);
        } else {
            builder.setSmallIcon(R.drawable.icon);
        }*/

    }


    private void alarm(){
        SharedPreferences getAlarms = PreferenceManager.
                getDefaultSharedPreferences(context_this);
        String alarms = getAlarms.getString("notifications_new_message_ringtone", "default ringtone");
        Uri uri = Uri.parse(alarms);
        playSound(context_this1, uri);

        //call mMediaPlayer.stop(); when you want the sound to stop
    }


    private MediaPlayer mMediaPlayer;
    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }
}
