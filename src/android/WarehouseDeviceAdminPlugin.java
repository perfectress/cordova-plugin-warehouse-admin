package com.perfectress.warehousem;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class WarehouseDeviceAdminPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Activity activity = cordova.getActivity();
        Context context = activity.getApplicationContext();
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminComponent = new ComponentName(context, WarehouseDeviceAdminReceiver.class);

        if ("activateAdmin".equals(action)) {
            if (!dpm.isAdminActive(adminComponent)) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Enable device admin to use kiosk mode.");
                activity.startActivity(intent);
                callbackContext.success("Device admin activation requested");
            } else {
                callbackContext.success("Already device admin");
            }
            return true;
        }

        if ("startKiosk".equals(action)) {
            if (dpm.isDeviceOwnerApp(activity.getPackageName())) {
                dpm.setLockTaskPackages(adminComponent, new String[]{activity.getPackageName()});
                activity.startLockTask();
                callbackContext.success("Kiosk mode started");
            } else {
                callbackContext.error("App is not device owner");
            }
            return true;
        }

        if ("stopKiosk".equals(action)) {
            activity.stopLockTask();
            callbackContext.success("Kiosk mode stopped");
            return true;
        }

        return false;
    }
}
