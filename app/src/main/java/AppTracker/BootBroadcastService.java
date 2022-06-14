package AppTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Alarms.resetIsUsageExceededData(context);
            Alarms.scheduleNotification(context);
        }
    }
}
