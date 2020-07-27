package com.example.omakhelper.aallHelpers;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmHelpers {
    public static String allSubscriberListUpdated = "allSubscriberList";
    public static String allLeadsUpdated = "allLeadsUpdated";
    public static String allCalculationsNewUpdated = "allCalculationsNewUpdated";
    public static String allCalculationEmailListUpdated = "allCalculationEmailList";
    public static String allFAQListUpdated = "allFAQList";
    public static String allUserProjectList = "allUserProjectList";
    public static String allAdminProjectList = "allAdminProjectList";
    public static String areEmailsUpdated = "areEmailsUpdated";
    public static String getSingleProjectDetailAdminSide = "getSingleProjectDetailAdminSide";
    public static String getSingleProjectCommentsAdminSide = "getSingleProjectCommentsAdminSide";
    public static String getSingleProjectDetailUserSide = "getSingleProjectDetailUserSide";
    public static String getAllCommentsAdminSide = "getAllCommentsAdminSide";
    public static String getAllUserActivitiesData = "getAllUserActivitiesData";
    static Realm realm;
    static RealmHelpers realmHelpers;
    Context context;

    public RealmHelpers() {
    }

    public RealmHelpers(Context context) {
        realmHelpers = new RealmHelpers();
        realm = HelperFunctions.getRealm("messages", context);
    }

    public static RealmHelpers getInstance(Context context) {
        realmHelpers = new RealmHelpers();
        realm = HelperFunctions.getRealm("messages", context);

        return realmHelpers;

    }


    public boolean getBooleanFlag(String flagName) {
        RealmQuery<RealmFlags> query = realm.where(RealmFlags.class).equalTo("key", flagName);
        RealmFlags realmFlags = query.findFirst();

        if (realmFlags != null) {
            return realmFlags.getBooleanValue();
        }

        return false;
    }

    public void setBooleanFlag(final String flagName, final boolean value) {
        RealmQuery<RealmFlags> query = realm.where(RealmFlags.class).equalTo("key", flagName);
        final RealmFlags isUpdatedFlag = query.findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (
                        isUpdatedFlag != null) {
                    isUpdatedFlag.setBooleanValue(value);
                } else {
                    final RealmFlags realmFlags = new RealmFlags();
                    realmFlags.setKey(flagName);
                    realmFlags.setBooleanValue(value);
                    realm.insertOrUpdate(realmFlags);
                }
            }
        });
    }



    /////////////////////  get size of realm data   ///////////////////////
    public <T extends RealmObject> RealmResults<T> getSize(Class<T> clazz) {
        RealmQuery uniqueFieldQuery = realm.where(clazz);
        RealmResults<T> uniqueValueProjects = uniqueFieldQuery.findAll();
        return uniqueValueProjects;
    }

    public <T extends RealmObject> void deleteFromRealm(Class<T> clazz) {
        RealmQuery query = realm.where(clazz);
        final RealmResults<T> rows = query.findAll();

        if (rows == null) {
            return;
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                rows.deleteAllFromRealm();
            }
        });
    }

    public <T extends RealmObject> void deleteFromRealm(String field, String value, Class<T> clazz) {
        Realm.getDefaultInstance();
        RealmQuery query = realm.where(clazz);
        query.equalTo(field, value, Case.INSENSITIVE).findFirst();
        final RealmResults<T> rows = query.findAll();
        if (rows.size() == 0) return;

        if (rows != null) {
            realm.executeTransaction(
                    new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            rows.deleteAllFromRealm();
                        }
                    });
        }
    }

    public <T extends RealmObject> void deleteFromRealm(String field, Integer value, Class<T> clazz) {
        RealmQuery query = realm.where(clazz);
        query.equalTo(field, value).findFirst();
        final RealmResults<T> rows = query.findAll();

        if (rows != null) {
            realm.executeTransaction(
                    new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            rows.deleteAllFromRealm();
                        }
                    });
        }
    }

    public <T extends RealmObject> void deleteFromRealm1(String field, int value, Class<T> clazz) {
        RealmQuery query = realm.where(clazz);
        query.equalTo(field, value).findFirst();
        final RealmResults<T> rows = query.findAll();

        if (rows != null) {
            realm.executeTransaction(
                    new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            rows.deleteAllFromRealm();
                        }
                    });
        }
    }

    public <T extends RealmObject> void saveToRealm(final T object) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(object);
            }
        });
    }

    public <T extends RealmObject> void saveToRealm(final List<T> objects) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(objects);
            }
        });
    }

    public <T extends RealmObject> void saveToRealm(final T object, Class<T> clazz) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(object);
            }
        });
    }

    public <T extends RealmObject> T getFromRealmLastResult(Class<T> clazz) {
        RealmQuery uniqueFieldQuery = realm.where(clazz).sort("id", Sort.DESCENDING);
        T uniqueValueProjects = clazz.cast(uniqueFieldQuery.findFirst());
        return uniqueValueProjects;
    }

    public <T extends RealmObject> T getFromRealmLastResult(Class<T> clazz, String primaryKey) {
        RealmQuery uniqueFieldQuery = realm.where(clazz).sort(primaryKey, Sort.DESCENDING);
        T uniqueValueProjects = clazz.cast(uniqueFieldQuery.findFirst());
        return uniqueValueProjects;
    }


    public <T extends RealmObject> RealmResults<T> getUniqueFieldSortedValuesFromRealm(String field, Class<T> clazz) {
        RealmQuery uniqueFieldQuery = realm.where(clazz).distinct(field);
        uniqueFieldQuery.notEqualTo(field, "").sort("status", Sort.ASCENDING);
        RealmResults<T> uniqueValueProjects = uniqueFieldQuery.findAll();
        return uniqueValueProjects;
    }

    public <T extends RealmObject> RealmResults<T> getUniqueFieldValuesFromRealm(String field, Class<T> clazz) {
        RealmQuery uniqueFieldQuery = realm.where(clazz).distinct(field);
        uniqueFieldQuery.notEqualTo(field, "");
        RealmResults<T> uniqueValueProjects = uniqueFieldQuery.findAll();
        return uniqueValueProjects;
    }

    public <T extends RealmObject> RealmResults<T> getFromRealm(Class<T> clazz) {
        return realm.where(clazz).findAll();
    }

    public <T extends RealmObject> RealmResults<T> getFilteredDataFromRealm(String field, String value, Class<T> clazz) {
        RealmQuery uniqueFieldQuery = realm.where(clazz);
        uniqueFieldQuery.equalTo(field, value);
        RealmResults<T> uniqueValueProjects = uniqueFieldQuery.findAll();
        return uniqueValueProjects;
    }

    public <T extends RealmObject> RealmResults<T> getFilteredDataFromRealm(String field, Integer value, Class<T> clazz) {
        RealmQuery uniqueFieldQuery = realm.where(clazz);
        uniqueFieldQuery.equalTo(field, value);
        RealmResults<T> uniqueValueProjects = uniqueFieldQuery.findAll();
        return uniqueValueProjects;
    }

    public <T extends RealmObject> T getDataFromRealm(String field, Integer value, Class<T> clazz) {
        RealmQuery<T> queryForUser = realm.where(clazz).equalTo(field, value);
        return queryForUser.findFirst();
    }

    public <T extends RealmObject> T getSingleRowForIntegerField(String field, Integer value, Class<T> clazz) {
        RealmQuery query = realm.where(clazz);
        query.equalTo(field, value);
        RealmResults<T> foundRows = query.findAll();
        if (foundRows.size() > 0) {
            return foundRows.get(0);
        }

        return null;
    }

    public <T extends RealmObject> T getSingleRowForStringField(String field, String value, Class<T> clazz) {
        RealmQuery query = realm.where(clazz);
        query.equalTo(field, value);
        RealmResults<T> foundRows = query.findAll();
        if (foundRows.size() > 0) {
            return foundRows.get(0);
        }

        return null;
    }

    public <T extends RealmObject> T getSingleRowContains(String field, String value, Class<T> clazz) {

        RealmQuery query = realm.where(clazz);
        query.contains(field, value);
        RealmResults<T> foundRows = query.findAll();
        if (foundRows.size() > 0) {
            return foundRows.get(0);
        }

        return null;
    }


}
