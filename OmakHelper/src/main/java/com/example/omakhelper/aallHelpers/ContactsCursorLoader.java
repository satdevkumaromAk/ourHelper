package com.example.omakhelper.aallHelpers;


import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import androidx.loader.content.CursorLoader;

import static android.provider.ContactsContract.Contacts;

public class ContactsCursorLoader extends CursorLoader {

    /**
     * Cursor selection string
     */
    public static final String[] CONTACTS_PROJECTION_DISPLAY_NAME_PRIMARY =
            new String[]{
                    Phone.CONTACT_ID, // 0
                    Phone.DISPLAY_NAME_PRIMARY, // 1
                    Phone.PHOTO_ID, // 2
                    Phone.PHOTO_THUMBNAIL_URI, // 3
                    Phone.NUMBER, // 4
                    Phone.STARRED
            };
    public static String CURSOR_NAME_COLUMN = Phone.DISPLAY_NAME_PRIMARY;
    public static String CURSOR_NUMBER_COLUMN = Phone.NUMBER;

    /**
     * Constructor
     *
     * @param context
     * @param phoneNumber String
     * @param contactName String
     */
    public ContactsCursorLoader(Context context, String phoneNumber, String contactName) {
        super(
                context,
                buildUri(phoneNumber, contactName),
                getProjection(context),
                getWhere(context),
                null,
                getSortKey(context) + " ASC");
    }

    /**
     * Returns the projection string
     *
     * @param context
     * @return String
     */
    private static String[] getProjection(Context context) {
        return CONTACTS_PROJECTION_DISPLAY_NAME_PRIMARY;
    }

    /**
     * Get a filter string
     *
     * @param context
     * @return String
     */
    private static String getWhere(Context context) {
        return "(" + Phone.DISPLAY_NAME_PRIMARY + " IS NOT NULL" +
                " OR " + Phone.DISPLAY_NAME_ALTERNATIVE + " IS NOT NULL" + ")" +
                " AND " + Phone.HAS_PHONE_NUMBER + "=1" +
                " AND (" + ContactsContract.RawContacts.ACCOUNT_NAME + " IS NULL" +
                " OR ( " + ContactsContract.RawContacts.ACCOUNT_TYPE + " NOT LIKE '%whatsapp%'" +
                " AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " NOT LIKE '%tachyon%'" + "))";
    }

    private static String getSortKey(Context context) {
        return Contacts.SORT_KEY_PRIMARY;
    }

    /**
     * Builds contact uri by given name and phone number
     *
     * @param phoneNumber
     * @param contactName
     * @return Builder.build()
     */
    private static Uri buildUri(String phoneNumber, String contactName) {
        Uri.Builder builder;
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            builder = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(phoneNumber)).buildUpon();
            builder.appendQueryParameter(ContactsContract.STREQUENT_PHONE_ONLY, "true");
        } else if (contactName != null && !contactName.isEmpty()) {
            builder = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(contactName)).buildUpon();
            builder.appendQueryParameter(ContactsContract.PRIMARY_ACCOUNT_NAME, "true");
        } else {
            builder = Phone.CONTENT_URI.buildUpon();
        }

        builder.appendQueryParameter(Contacts.EXTRA_ADDRESS_BOOK_INDEX, "true");
        builder.appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "true");
        return builder.build();
    }

}