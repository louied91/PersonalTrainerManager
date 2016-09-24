package spcstudent.android.personaltrainermanager;

import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;


public class Customer {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String billingInformation;
    private String email;

    public Customer() {
        this.customerId = UUID.randomUUID();
    }

    public Customer(UUID customerId) {
        this.customerId = customerId;
    }

    public Customer(String firstName, String lastName,
                    Date birthDate, String billingInformation, String email) {

        this.customerId = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.billingInformation = billingInformation;
        this.email = email;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBillingInformation() {
        return billingInformation;
    }

    public void setBillingInformation(String billingInformation) {
        this.billingInformation = billingInformation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoFilename() {
        return "IMG_" + getCustomerId().toString() + ".jpg";
    }
}