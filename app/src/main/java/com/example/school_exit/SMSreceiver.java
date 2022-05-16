package com.example.school_exit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

public class SMSreceiver extends BroadcastReceiver {

    public static final String MESSAGE_MARKER = "SchoolExit";

    public static final String INTENT_SUM = "sum";
    public static final String INTENT_DESC = "sum";

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private final int NOTIFICATION_CANCEL_CODE = 0;
    private final String NOTIFICATION_CHANNEL_ID = "SCHOOL_EXIT_CHANNEL";

    NotificationManagerCompat notificationManager;
    NotificationChannel channel;
    NotificationManager manager;

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent == null || ! intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
            return;

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        notificationManager = NotificationManagerCompat.from(context);
        this.context = context;

        Toast.makeText(context,"here",Toast.LENGTH_LONG).show();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus"); // SMS info

                for (int i = 0; i < pdusObj.length; i++) { // iterate through the messages
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress(); // extract sender phone number

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    System.out.println(message);

                    Log.d("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    // Show alert
                    Toast.makeText(context, "senderNum: " + senderNum + ", message: " + message, Toast.LENGTH_LONG).show();
                    if (message.startsWith(MESSAGE_MARKER)){
                        Log.d("SmsReceiver", "message from myAPP!!!");
                        Toast.makeText(context, "message from myAPP!!!", Toast.LENGTH_LONG).show();

                        makeNotification("SchoolExit","New request received!");

                    }
                } // end for loop
            } // bundle is null
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    private void makeNotification(String notiTitle, String notiText) {

            createNotificationChannel();
            System.out.println("Channel  Created");

            Intent go = new Intent(this.context, SignIn.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.context);
            stackBuilder.addNextIntentWithParentStack(go);

            // 3 - Get the PendingIntent containing the entire back stack
            PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Builder notificationB = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(notiTitle)
                    .setContentText(notiText)
                    .setSmallIcon(R.drawable.school_exit_logo_100) // TODO: Check the icon with a cellPhone
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);

            manager.notify(NOTIFICATION_CANCEL_CODE, notificationB.build());
            System.out.println("NOTIFIED");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription(context.getString(R.string.app_name));
            channel.setLightColor(Color.GREEN);

            manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}