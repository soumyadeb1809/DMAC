package in.teamconsultants.dmac.utils;

import java.util.ArrayList;

public class AppConstants {

    public static class REGISTRATION {
        public static final int QUICK = 0;
        public static final int REGULAR = 1;

        public static final class INITIATOR {
            public static final int CUSTOMER = 0;
            public static final int FE = 1;
        }
    }

    public static class INTENT_TAG {
        public static final String REG_TYPE = "reg_type";
        public static final String REG_INITIATOR = "reg_initiator";
        public static final String CUSTOMER_JOB = "customer_job";

        public static final String CAMSCANNER_INTENT_URI = "com.intsig.camscanner";
    }

    public static final String LOG_TAG = "DMAC_LOG";

    public static final int MAX_ALLOWED_JOBS = 20;

    public static final class RESPONSE {
        public static final String SUCCESS = "Success";
        public static final String FAILED = "Failed";
    }

    public static final class SP {
        public static final String SP_USER_DATA = "user_data";
        public static final String TAG_USER_DETAILS = "user_details";
        public static final String TAG_TOKEN = "token";

    }

    public static final class USER_ROLE {
        public static final String CUSTOMER = "3";
        public static final String FE = "2";
    }

    public static final class FILE_SEARCH {
        public static String INVALID_START_DATE = "Start Date";
        public static String INVALID_END_DATE = "End Date";
        public static int INVALID_STATUS = -1;
    }

}
