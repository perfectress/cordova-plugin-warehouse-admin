package com.perfectress.warehouse;

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

    private static final String ACTION_ACTIVATE_ADMIN = "activateAdmin";
    private static final String ACTION_START_KIOSK = "startKiosk";
    private static final String ACTION_STOP_KIOSK = "stopKiosk";

    private DevicePolicyManager dpm;
    private ComponentName adminComponent;

    @Override
    protected void pluginInitialize() {
        Context context = cordova.getActivity().getApplicationContext();
        dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(context, WarehouseDeviceAdminReceiver.class);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Activity activity = cordova.getActivity();

        if (ACTION_ACTIVATE_ADMIN.equals(action)) {
            if (!dpm.isAdminActive(adminComponent)) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
                cordova.getActivity().startActivity(intent);
                callbackContext.success("Requested device admin activation");
            } else {
                callbackContext.success("Already device admin");
            }
            return true;
        }

        if (ACTION_START_KIOSK.equals(action)) {
            if (dpm.isDeviceOwnerApp(activity.getPackageName())) {
                dpm.setLockTaskPackages(adminComponent, new String[]{activity.getPackageName()});
                activity.startLockTask();
                callbackContext.success("Kiosk mode started");
            } else {
                callbackContext.error("App is not device owner");
            }
            return true;
        }

        if (ACTION_STOP_KIOSK.equals(action)) {
            activity.stopLockTask();
            callbackContext.success("Kiosk mode stopped");
            return true;
        }

        return false;
    }
}