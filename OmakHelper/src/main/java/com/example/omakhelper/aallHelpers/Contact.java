package com.example.omakhelper.aallHelpers;

import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Contact {

    long contactId;
    private long listId;
    private String name;
    private List<String> phoneNumbers;
    private String photoUri; // No need to save this to the database
    private boolean isFavorite;

    /**
     * Contact constructor
     * Accepts a name and a list of numbers (without an image)
     *
     * @param name
     * @param phoneNumbers
     */
    public Contact(String name, @NonNull List<String> phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    /**
     * Contact constructor
     * Accepts a name, a list of numbers and an image
     *
     * @param name
     * @param phoneNumbers
     * @param photoUri
     */
    public Contact(String name, @NonNull List<String> phoneNumbers, String photoUri) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.photoUri = photoUri;
    }

    /**
     * Contact constructor
     * Accepts a name, one phone number and an image
     *
     * @param name        the contact's name
     * @param phoneNumber the contact's phone number
     * @param photoUri    the contact's image
     */
    public Contact(String name, @NonNull String phoneNumber, @Nullable String photoUri) {
        this.name = name;
        this.photoUri = photoUri;
        this.phoneNumbers = new ArrayList<>();
        this.phoneNumbers.add(phoneNumber);
    }

    /**
     * Contact constructor
     * Accepts a name, one phone number and an image
     *
     * @param id          the contact's id
     * @param name        the contact's name
     * @param phoneNumber the contact's phone number
     * @param photoUri    the contact's image
     */
    public Contact(long id, String name, @NonNull String phoneNumber, @Nullable String photoUri) {
        this.contactId = id;
        this.name = name;
        this.photoUri = photoUri;
        this.phoneNumbers = new ArrayList<>();
        this.phoneNumbers.add(phoneNumber);
    }

    public Contact(Cursor cursor) {
        this.contactId = cursor.getLong(cursor.getColumnIndex(Phone.CONTACT_ID));
        this.name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME_PRIMARY));
        this.photoUri = cursor.getString(cursor.getColumnIndex(Phone.PHOTO_THUMBNAIL_URI));
        this.phoneNumbers = new ArrayList<>();
        this.phoneNumbers.add(cursor.getString(cursor.getColumnIndex(Phone.NUMBER)));
        this.isFavorite = "1".equals(cursor.getString(cursor.getColumnIndex(Phone.STARRED)));
    }

    /**
     * Returns the contact's id
     *
     * @return the contact's id
     */
    public long getContactId() {
        return contactId;
    }

    /**
     * Sets the contact's id by a given id
     *
     * @param contactId
     */
    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    /**
     * Returns the contact's name
     *
     * @return String of the name
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Sets the contact's name by a given String
     *
     * @param name
     */
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * Returns all the phone numbers of a contact
     *
     * @return List<String>
     */
    @NonNull
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * Sets the contact's phone numbers by a given list of strings
     *
     * @param phoneNumbers
     */
    public void setPhoneNumbers(@NonNull List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    /**
     * Returns the contact's main phone number
     *
     * @return String
     */
    public String getMainPhoneNumber() {
        if (phoneNumbers == null) return null;
        else if (phoneNumbers.isEmpty()) return null;

        String phoneNumber = phoneNumbers.get(0);

        // Try decoding it just in case
        try {
            phoneNumber = java.net.URLDecoder.decode(phoneNumber, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // The number cant be decoded so its probably not needed anyway
        }

        return phoneNumber;
    }

    /**
     * Returns the contact's list id
     *
     * @return long
     */
    public long getListId() {
        return listId;
    }

    /**
     * Sets the contact's list id by a given number
     *
     * @param listId
     */
    public void setListId(long listId) {
        this.listId = listId;
    }

    /**
     * Returns the contact's image (Uri)
     *
     * @return String
     */
    public String getPhotoUri() {
        return photoUri;
    }

    /**
     * Sets the contact's image by a given image (String)
     *
     * @param photoUri
     */
    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    /**
     * Returns wither the contact is a favorite contact
     *
     * @return
     */
    public boolean getIsFavorite() {
        return isFavorite;
    }

    /**
     * Makes the contact favorite/not favorite
     *
     * @param isFavorite
     * @return
     */
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    /**
     * Returns the contact's details in a string
     *
     * @return a string representing the contact
     */
    @NonNull
    @Override

    public String toString() {
//        return String.format(Utilities.sLocale, "id: %d, list_id: %d, name: %s, numbers: %s", contactId, listId, name, this.phoneNumbers.toString());
        return String.format("id: %d, list_id: %d, name: %s, numbers: %s", contactId, listId, name, this.phoneNumbers.toString());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (super.equals(obj)) return true;
        if (obj instanceof Contact) {
            Contact c = (Contact) obj;
            return (name.equals(c.getName()) &&
                    phoneNumbers.equals(c.getPhoneNumbers()));
        }
        return false;
    }
}