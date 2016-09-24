package database.Trainer;

import android.database.Cursor;
import android.database.CursorWrapper;

import spcstudent.android.personaltrainermanager.Session;

import java.util.Date;
import java.util.UUID;

public class SessionCursorWrapper extends CursorWrapper{
    public SessionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Session getSession() {
        String sessionIdString = getString(getColumnIndex(
                TrainerDatabase.SessionTable.Columns.SESSION_ID));
        String customerIdString = getString(getColumnIndex(
                TrainerDatabase.SessionTable.Columns.CUSTOMER_ID));
        String sessionNameString = getString(getColumnIndex(
                TrainerDatabase.SessionTable.Columns.SESSION_NAME));
        long sessionDateTimeLong = getLong(getColumnIndex(
                TrainerDatabase.SessionTable.Columns.SESSION_DATE));
        double sessionCostDouble = getDouble(getColumnIndex(
                TrainerDatabase.SessionTable.Columns.SESSION_COST));
        int isSessionCompletedInt = getInt(getColumnIndex(
                TrainerDatabase.SessionTable.Columns.SESSION_COMPLETED));

        Session session = new Session(UUID.fromString(sessionIdString));

        session.setCustomerID(UUID.fromString(customerIdString));
        session.setSessionName(sessionNameString);
        session.setSessionDateTime(new Date(sessionDateTimeLong));
        session.setSessionCost(sessionCostDouble);
        session.setIsSessionCompleted(isSessionCompletedInt == 1);

        return session;
    }
}