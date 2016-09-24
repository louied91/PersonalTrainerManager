package spcstudent.android.personaltrainermanager;

import java.util.Date;
import java.util.UUID;

public class Session {
    private UUID sessionId;
    private UUID customerID;
    private String sessionName;
    private Date sessionDateTime;
    private double sessionCost;
    private boolean isSessionCompleted;

    public Session(UUID customerID, Boolean markIsCompleted) {
        this.sessionId = UUID.randomUUID();
        this.customerID = customerID;
        this.isSessionCompleted = markIsCompleted;
    }

    public Session(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public Session(UUID customerID, String sessionName, Date sessionDateTime,
                   double sessionCost, boolean markCompleted) {
        this.sessionId = UUID.randomUUID();
        this.customerID = customerID;
        this.sessionName = sessionName;
        this.sessionDateTime = sessionDateTime;
        this.sessionCost = sessionCost;
        this.isSessionCompleted = markCompleted;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Date getSessionDateTime() {
        return sessionDateTime;
    }

    public void setSessionDateTime(Date sessionDateTime) {
        this.sessionDateTime = sessionDateTime;
    }

    public double getSessionCost() {
        return sessionCost;
    }

    public void setSessionCost(double sessionCost) {
        this.sessionCost = sessionCost;
    }

    public boolean isSessionCompleted() {
        return isSessionCompleted;
    }

    public void setIsSessionCompleted(boolean isSessionCompleted) {
        this.isSessionCompleted = isSessionCompleted;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }
}