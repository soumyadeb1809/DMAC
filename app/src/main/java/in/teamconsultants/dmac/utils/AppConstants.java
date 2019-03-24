package in.teamconsultants.dmac.utils;

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
        public static final String FILE_STATUS = "file_status";

        public static final String IMG_SRC_TYPE = "image_source";
        public static final String IMG_URI = "image_uri";
        public static final String IMG_BITMAP = "image_bitmap";

        public static final String FILE_CATEGORY_NAME = "file_category_name";
        public static final String FILE_CATEGORY_ID = "file_category_id";

        public static final String INVOICE_NAME = "invoice_name";
        public static final String INVOICE_AMOUNT = "invoice_amount";
        public static final String INVOICE_ID = "invoice_id";

        public static final String USER_FULL_NAME = "fullName";
        public static final String USER_PHONE = "phone";
    }

    public static final String LOG_TAG = "DMAC_LOG";

    public static final int MAX_ALLOWED_JOBS = 20;

    public static final int MAX_ALLOWED_DIRECTORS = 5;

    public static final class RESPONSE {
        public static final String SUCCESS = "Success";
        public static final String FAILED = "Failed";
    }

    public static final class SP {
        public static final String SP_USER_DATA = "user_data";
        public static final String SP_REMEM_USER = "remem_user";
        public static final String TAG_USER_DETAILS = "user_details";
        public static final String TAG_TOKEN = "token";

        public static final String SP_DASHBOARD_DATA = "dash_data";
        public static final String TAG_TOTAL_FILES = "total_files";
        public static final String TAG_VERIFIED_FILES = "verified_files";
        public static final String TAG_TOTAL_ACCOUNTS = "total_accounts";
        public static final String TAG_OPEN_ACCOUNTS = "open_accounts";

        public static final String IS_USER_LOGGED_IN = "user_logged_in";
        public static final String TAG_USER_EMAIL = "email";
        public static final String TAG_USER_PASSWORD = "password";

        public static final String TAG_USER_ACCOUNT_LIST = "account_list";

    }

    public static final class USER_ROLE {
        public static final String CUSTOMER = "3";
        public static final String FE = "2";
    }

    public static final class FILE_SEARCH {
        public static final String INVALID_START_DATE = "Start Date";
        public static final String INVALID_END_DATE = "End Date";
        public static final int INVALID_STATUS = -1;
        public static final String FAILED_STATUS_ID = "10";
    }


    public static final class IMAGE_SOURCE {
        public static final String BITMAP = "bitmap";
        public static final String URI = "uri";
    }

    public static final class ENTITY_TYPE {
        public static final String PROPRIETARY = "Proprietary";
        public static final String PARTNERSHIP = "Partnership";
        public static final String LLP = "LLP";
        public static final String COMPANY = "Company";
        public static final String SOCIETY = "Society";
        public static final String TRUST = "Trust";

    }

    public static final class BUSINESS_DOC_TYPE {
        public static final int OTHERS = 1;
        public static final int MOA = 2;
        public static final int AOA = 3;
        public static final int PARTNERSHIP_DEED = 4;
        public static final int REG_CERT = 5;
        public static final int INCORP_CERT = 6;
        public static final int PAN = 7;
        public static final int GST_REG = 8;
        public static final int AUDIT_BAL_SHEET = 9;
    }

    public static final class DIRECTOR_DOC_TYPE {
        public static final int AADHAR = 1;
        public static final int PAN = 2;
    }

    public static final class STATUS {
        public static final String YES = "1";
        public static final String NO = "0";
    }

    public static final class INFO {
        public static final String DMAC_SUPPORT_EMAIL = "support@dmac.com";
        public static final String DMAC_SUPPORT_SUB = "Support Request - DMAC App";
    }

    public static final class INVOICE {
        public static final String STATUS_PAID = "22";
        public static final String STATUS_NEW = "21";
    }


    public static final String FILE_BASE_URL = "https://staging.teamconsultants.in/upload/";
    public static final String PRIVACY_POLICY_URL = "https://teamconsultants.in/privacy-policy.php";
    public static final String ABOUT_US_URL = "http://teamconsultants.in/profile.php";
    public static final String APP_MARKET_BASE_URL = "market://details?id=";
    public static final String PLAY_STORE_BASE_URL = "http://play.google.com/store/apps/details?id=";

}
