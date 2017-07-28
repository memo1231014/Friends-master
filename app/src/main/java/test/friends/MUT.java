package test.friends;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import test.friends.view.fragments.SendMessageFragment;


/************************************************************************\
                This code written by Mohamed Atef

                             \|||||/
                             /-----\
                             |0   0|
                             | { } |
                             \-----/
                                |
                               /|\
                              / | \
                             /  |  \
                                |
                                |
                               / \
                              /   \
                             /     \
                            /       \

         please don't give shits if you couldn't understand my codes,
         Really I'm trying to do my best as fast as possible so if you
         coudn't understand it tell me and if you got any idea better
         than mine tell me please thank you and don't small us :)


                        I Think I Design I Code
                                                                    Tefa Alhwa
     \************************************************************************/

public class MUT {
        // here you can find methods and variables that i used many times in different places within the project


    public static void addFragment(Context context, Fragment fragment, String backStackText) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().add(R.id.content, fragment);
        if (!backStackText.equals("")) {
            fragmentTransaction.addToBackStack(backStackText);
        }
        fragmentTransaction.commit();

    }

    public static void replaceFragment(Context context, Fragment fragment, String backStackText) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.content, fragment);
        if (!backStackText.equals("")) {
            fragmentTransaction.addToBackStack(backStackText);
        }
        fragmentTransaction.commit();

    }


    public static void makeCall(Context context, String number) {

        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            context.startActivity(intent);
        } catch (Exception e) {
            MUT.lToast(context, context.getResources().getString(R.string.wrong_phone));
            e.getStackTrace();
        }
    }

    public static void sendMail(Context context, String mail) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mail, null));
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }


    public static void lToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void sToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    public static void dToast(Context context, String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }


    public static int getScreenWidth(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return (displaymetrics.widthPixels);
    }

    public static Dialog initializeLoading(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loadingbar);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public static void showErrorMessages(Context context, VolleyError error) {
        if (error instanceof ServerError) {
            MUT.lToast(context, context.getResources().getString(R.string.server_error));
        } else if (error instanceof NoConnectionError || error instanceof TimeoutError) {
            MUT.lToast(context, context.getResources().getString(R.string.connection_error));
        } else {
            MUT.lToast(context, context.getResources().getString(R.string.server_error));
        }
    }

        public static void sendMessage(Context context,String to){
            Bundle bundle = new Bundle();
            bundle.putString("friendName",to);
            SendMessageFragment sendMessageFragment = new SendMessageFragment();
            sendMessageFragment.setArguments(bundle);
            MUT.addFragment(context,sendMessageFragment,"sendMessageFragment");
        }

    public static void turnGPSOn(final Context context) {
        GoogleApiClient mGoogleApiClient;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS: {
                        break;
                    }
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult((Activity) context, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            e.getStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }
}
