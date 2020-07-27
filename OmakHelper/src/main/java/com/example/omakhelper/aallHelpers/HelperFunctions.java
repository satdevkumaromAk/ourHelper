package com.example.omakhelper.aallHelpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.omakhelper.R;
import com.example.omakhelper.aallHelpers.Retofit.ApiClient;
import com.example.omakhelper.aallHelpers.Retofit.ApiHelper;
import com.example.omakhelper.aallHelpers.Retofit.ApiInterface;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class HelperFunctions {
    static TextView tvTitles, tvUpdateData;
    static LinearLayout llBackIcon, llMenu;
    static ImageView ivEditAction, ivContactSupport, ivSearchIcon, ivHomeIcon, ivUploadFiles, ivRefreshData, ivDelete;
    Realm realm;
    RealmHelpers realmHelpers;
    boolean isReturn = false;
    String mailFor = "";
    private HelperFunctionsListener listener;

    /*Realm Configuration */
    public static Realm getRealm(String whichRealm, Context applicationContext) {
        Realm.init(applicationContext);
        if ("messages".equals(whichRealm)) {
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name("messages.realm")
                    .deleteRealmIfMigrationNeeded()
                    //// .schemaVersion(1)
                    //// .migration(new AppFlowDatabaseRealmMigration())
                    .build();
            Realm.setDefaultConfiguration(config);
        }
        return Realm.getDefaultInstance();
    }

    /* GEt Date Method*/
    public static Date convertString2Date(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String currentTimeDate(String dateStr) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dateStr);
            df.setTimeZone(TimeZone.getDefault());
            String formattedDate = df.format(date);

            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String upperCaseLetter(String str) {
        StringBuffer capBuffer = new StringBuffer();
        if (str.isEmpty() || str.equals("") || str == null) {
            return " ";
        }
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(str);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();

    }

    public static String lowerCaseLetter(String str) {
        if (!str.isEmpty()) {
            return str.toLowerCase();

        }
        return str;
    }

    public static String getIntentExtra(Context context, String key) {
        Activity activity = getActivity(context);
        String userId = activity.getIntent().getStringExtra("userId");
        return userId;


    }



    ////  get All New Calculation data Api   /////////

    public static String getFormattedDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        return formatter.format(date);
    }

    public static String getFormattedDateFromString(String dateString) {
        Date date = convertString2Date(dateString);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss");
        return formatter.format(date);
    }

    public static String getFormattedDateFromString(String dateString, String format) {
        Date date = convertString2Date(dateString);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }


    // method for open call //
    public static void openCallDialer(Context context, String callerId) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + callerId));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }


    //tool bar common methods like findView by ids and set on click listener

    public static void setToolBarTitle(String title) {
        tvTitles.setText(title);
    }

    public static void setVisiblityToolBarIcons(String icon, String type) {
        switch (icon) {
            case "backIcon":
                if (type.equals("visible")) {
                    llBackIcon.setVisibility(View.VISIBLE);
                } else {
                    llBackIcon.setVisibility(View.GONE);
                }

                break;
            case "searchIcon":
                if (type.equals("visible")) {
                    ivSearchIcon.setVisibility(View.VISIBLE);
                } else {
                    ivSearchIcon.setVisibility(View.GONE);
                }
                break;
            case "homeIcon":
                if (type.equals("visible")) {
                    ivHomeIcon.setVisibility(View.VISIBLE);
                } else {
                    ivHomeIcon.setVisibility(View.GONE);
                }
                break;
            case "refreshIcon":
                if (type.equals("visible")) {
                    ivRefreshData.setVisibility(View.VISIBLE);
                } else {
                    ivRefreshData.setVisibility(View.GONE);
                }
                break;
            case "deleteIcon":
                if (type.equals("visible")) {
                    ivDelete.setVisibility(View.VISIBLE);
                } else {
                    ivDelete.setVisibility(View.GONE);
                }
                break;
            case "editIcon":
                if (type.equals("visible")) {
                    ivEditAction.setVisibility(View.VISIBLE);
                } else {
                    ivEditAction.setVisibility(View.GONE);
                }
                break;
            case "contactIcon":
                if (type.equals("visible")) {
                    ivContactSupport.setVisibility(View.VISIBLE);
                } else {
                    ivContactSupport.setVisibility(View.GONE);
                }
                break;
            case "menu":
                if (type.equals("visible")) {
                    llMenu.setVisibility(View.VISIBLE);
                } else {
                    llMenu.setVisibility(View.GONE);
                }
                break;
            case "update":
                if (type.equals("visible")) {
                    tvUpdateData.setVisibility(View.VISIBLE);
                } else {
                    tvUpdateData.setVisibility(View.GONE);
                }
                break;
        }

    }

    // helper method for set array adapter for spinner
    public static SpinnerAdapter setArrayAdapterHelperMethod(Context context, String[] options) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, options);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    public static String getDate(String datePattern) {
        Calendar calendar = Calendar.getInstance();
        Date todayDate = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        return simpleDateFormat.format(todayDate);
    }


    public String dateDifference(String dateStart, String dateStop) {

        //// dateStart = "01/14/2012 09:29:58";
        //// dateStop = "01/15/2012 10:31:48";

        //// HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //// in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");
            return Long.toString(diffSeconds);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String setupTimeZoneValue() {
        TimeZone tz = TimeZone.getDefault();
        return getCurrentTimezoneOffset() + "/" + tz.getRawOffset();
    }

    public String getCurrentTimezoneOffset() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

        String offset =
                String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        Math.abs(offsetInMillis / 3600000),
                        Math.abs((offsetInMillis / 60000) % 60));
        offset = "GMT" + (offsetInMillis >= 0 ? "+" : "-") + offset;

        return offset + "/" + offsetInMillis;
    }

    public String convertUnix2Date(Long time) {
        return DateFormat.format("dd-MM-yyyy hh:mm:ss", time).toString();
    }

    //this interface is user for finding my admin is upto date with data
    public interface HelperFunctionsListener {
        void onApiCompleted();
    }


    public static boolean contactExists(Context context, String number) {
        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection,
                null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }

    public static void openSaveContact(Context context, String name, String number, String email) {
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);

        if (!name.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.NAME, HelperFunctions.upperCaseLetter(name));
        }

        if (!number.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        }

        if (!email.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        }

        context.startActivity(intent);
    }

    public static boolean UpdateContact(String name, String number, Context context) {
        boolean success = true;
        try {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.CommonDataKinds.Phone._ID + "=? AND " +
                                    ContactsContract.Contacts.Data.MIMETYPE + "='" +
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'",
                            new String[]{name})
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                    .build());

            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.CommonDataKinds.Phone._ID + "=? AND " +
                                    ContactsContract.Contacts.Data.MIMETYPE + "='" +
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'",
                            new String[]{name})
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                    .build());
            try {
                context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                Toast.makeText(context, "Contact is successfully Edit", Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean openWhatsapp(Context context, String number, String name) {
        Boolean success = false;
        try {
            String bodyMessageFormal = "Hello";
            bodyMessageFormal += (name == "") ? "" : " " + HelperFunctions.upperCaseLetter(name);
            bodyMessageFormal += ", \n\n"; // Replace with your message.

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setType("text/rtf");
            intent.setData(Uri.parse("https://wa.me/" + number + "/?text=" + bodyMessageFormal));
            context.startActivity(intent);
            success = true;
        } catch (Exception e) {
            Alerts.toast(context, "Message failed: " + e.getMessage());
            //Toast.makeText(context, "Message failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return success;
    }

    public static boolean openSmsApp(Context context, String number, String name) {
        Boolean success = false;
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", number);
        smsIntent.putExtra("sms_body", "Hi " + name + ",");
        smsIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        if (smsIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(smsIntent);
        }
        success = true;

        return success;
    }

    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

}
