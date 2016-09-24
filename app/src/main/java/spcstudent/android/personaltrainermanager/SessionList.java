package spcstudent.android.personaltrainermanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import database.Trainer.SessionCursorWrapper;
import database.Trainer.TrainerDBHelper;
import database.Trainer.TrainerDatabase;
import database.Trainer.TrainerDatabase.SessionTable;

public class SessionList {
    private  static  SessionList sSessionList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static SessionList get(Context context) {
        if (sSessionList == null) {
            sSessionList = new SessionList(context);
        }
        return  sSessionList;
    }

    private SessionList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TrainerDBHelper(mContext).getWritableDatabase();
    }

    public void addSession(Session session) {
        ContentValues values = getContentValues(session);
        mDatabase.insert(SessionTable.NAME, null, values);
    }

    public List<Session> getSessions(UUID customerId) {
        List<Session> sessions = new ArrayList<>();

        SessionCursorWrapper cursor = querySessions(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Session session = cursor.getSession();
                if (session.getCustomerID().toString().equals(customerId.toString())) {
                    sessions.add(cursor.getSession());
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return sessions;
    }

    public Session getSession(UUID id) {
        if (id == null) {
            return null;
        }

        SessionCursorWrapper cursor = querySessions(
                SessionTable.Columns.SESSION_ID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getSession();
        } finally {
            cursor.close();
        }
    }

    public void deleteSession(Session session) {
        String uuidString = session.getSessionId().toString();
        mDatabase.delete(SessionTable.NAME,
                SessionTable.Columns.SESSION_ID + " = ?",
                new String[] { uuidString });
    }

    public void deleteCustomersSessions(Customer customer) {
        String uuidString = customer.getCustomerId().toString();
        mDatabase.delete(SessionTable.NAME,
                SessionTable.Columns.CUSTOMER_ID + " = ?",
                new String[] { uuidString });
    }

    public void updateSession(Session session) {
        String uuidString = session.getSessionId().toString();
        ContentValues values = getContentValues(session);

        mDatabase.update(SessionTable.NAME, values,
                SessionTable.Columns.SESSION_ID + " = ?",
                new String[]{uuidString});
    }

    private static ContentValues getContentValues(Session session) {
        ContentValues values = new ContentValues();
        values.put(SessionTable.Columns.SESSION_ID, session.getSessionId().toString());
        values.put(SessionTable.Columns.CUSTOMER_ID, session.getCustomerID().toString());
        values.put(SessionTable.Columns.SESSION_NAME, session.getSessionName());
        values.put(SessionTable.Columns.SESSION_DATE, session.getSessionDateTime().getTime());
        values.put(SessionTable.Columns.SESSION_COST, session.getSessionCost());
        values.put(SessionTable.Columns.SESSION_COMPLETED, session.isSessionCompleted());

        return values;
    }

    private SessionCursorWrapper querySessions (String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SessionTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new SessionCursorWrapper(cursor);
    }
}