package database.Trainer;

public class TrainerDatabase {
    public static final class CustomerTable {
        public static final String NAME = "customers";

        public static final class Columns {
            public static final String CUSTOMER_ID = "customer_id";
            public static final String FIRST_NAME = "first_name";
            public static final String LAST_NAME = "last_name";
            public static final String BIRTHDATE = "birthdate";
            public static final String BILLING_INFO = "billing_info";
            public static final String EMAIL = "email";
        }
    }

    public static  final  class SessionTable {
        public static final String NAME = "sessions";

        public static final class Columns {
            public static final String SESSION_ID = "session_id";
            public static final String CUSTOMER_ID = "customer_id";
            public static final String SESSION_NAME = "session_name";
            public static final String SESSION_DATE = "session_date";
            public static final String SESSION_COST = "session_cost";
            public static final String SESSION_COMPLETED = "session_completed";
        }
    }
}