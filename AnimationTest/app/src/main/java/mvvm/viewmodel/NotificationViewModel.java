package mvvm.viewmodel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.lucas.animationtest.R;

import mvvm.view.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by lucas on 16/11/2016.
 */

public class NotificationViewModel {
    private Context context;

    public NotificationViewModel(Context context) {
        this.context = context;
    }

    public void pushMessage(View view) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("main")
                    .setSmallIcon(R.drawable.ic_suggestion)
                    .setContentText("back to main")
                    .build();
        }
        manager.notify(0, notification);
    }
}
