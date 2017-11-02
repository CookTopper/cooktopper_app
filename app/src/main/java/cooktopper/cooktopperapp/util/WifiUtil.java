package cooktopper.cooktopperapp.util;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiUtil {

    public String macAddress;

    public String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        macAddress = wifiManager.getConnectionInfo().getMacAddress();

        if (macAddress == null)
            macAddress = "DEVICE DONT HAVE MAC, or WI-FI is DISABLED";

        return macAddress;
    }

}
