package com.example.androidarduino;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
        Log.e("toke","My token is: "+s);
        saveToke(s);
    }

    private void saveToke(String s) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuarios").child(mFirebaseUser.getUid()).child("token");
        reference.setValue(s);
    }

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
         Log.e("TAG","Message Recived from: "+from);

         if (remoteMessage.getNotification() != null){
             Log.e("TAG","Title is:" + remoteMessage.getNotification().getTitle());
             Log.e("TAG","Body is:" + remoteMessage.getNotification().getBody());
         }
         if (remoteMessage.getData().size() > 0){
             String title = remoteMessage.getData().get("title");
             String detail = remoteMessage.getData().get("detail");
             greaterThanOreo(title,detail);

         }
    }


    private void greaterThanOreo(String title, String detail) {
        String id = "message";
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(id,"new",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        builder.setAutoCancel(true).setWhen(System.currentTimeMillis())
                .setContentTitle(title).setSmallIcon(R.drawable.ic_launcher)
                .setContentText(detail).setContentInfo("new")
                .setContentIntent(clickNotification());

        Random random = new Random();
        int idNotify = random.nextInt(8000);

        notificationManager.notify(idNotify,builder.build());
    }
    public PendingIntent clickNotification (){
        Intent intent = new Intent(getApplicationContext(),HomeMenu.class);
        intent.putExtra("color","red");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0, intent,0);
    }
}
