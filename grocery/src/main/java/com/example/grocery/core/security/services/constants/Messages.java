package com.example.grocery.core.security.services.constants;

public class Messages {

    public class ErrorMessages {

        private ErrorMessages() {
        }

        public static final String ID_NOT_FOUND = "Entered id not found in DB!";
        public static final String USER_ID_NOT_FOUND = "Entered user id not found in DB";
        public static final String EMAIL_REPEATED = "Email address can not be repeat!";
        public static final String PASSWORD_NOT_VALID = "Password can not contains username!";
        public static final String USER_EMAIL_NOT_FOUND = "Entered user email not found in DB!";
        public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token is not in DB!";
        public static final String USERNAME_EXIST = "Username already exists";
        public static final String ROLE_NOT_FOUND = "Role not found in DB!";
        public static final String REGISTER_TYPE_ERROR = "Email authenticated other social login!";
        public static final String PAGE_NUMBER_NEGATIVE = "Page number value must be positive number or zero!";
        public static final String PAGE_SIZE_NEGATIVE = "Page size number must be greater than 0!";
        public static final String SORT_PARAMETER_NOT_VALID = "Entered sort parameter not valid!";
        public static final String EMAIL_NOT_VERIFIED = "Email could not verified!";
    }

    public class GetListMessages {

        private GetListMessages() {
        }

        public static final String USERS_LISTED = "Users listed!";
        public static final String USERS_SORTED = "Users listed sorted by: ";
        public static final String USERS_PAGINATED = "Users listed by pagination";
        public static final String USERS_PAGINATED_AND_SORTED = "Users listed by pagination and sorted by: ";
    }

    public class GetByIdMessages {

        private GetByIdMessages() {
        }

        public static final String USER_LISTED = "User listed by entered id!";
    }

    public class CreateMessages {

        private CreateMessages() {
        }

        public static final String USER_CREATED = "User saved to DB!";
        public static final String GOOGLE_USER_CREATED = "Google account saved to DB!";
        public static final String FACEBOOK_USER_CREATED = "Facebook account saved to DB!";
    }

    public class DeleteMessages {

        private DeleteMessages() {
        }

        public static final String USER_DELETED = "User removed from DB";
        public static final String SIGN_OUT = "Log out successful!";
    }

    public class UpdateMessages {

        private UpdateMessages() {
        }

        public static final String USER_UPDATED = "User: {} updated!";
    }

    public class LogMessages {

        public class LogInfoMessages {

            private LogInfoMessages() {
            }

            public static final String USER_CREATED = "User: {} saved in DB!";
            public static final String USER_LOGINED = "User: {} logined!";
            public static final String USER_DELETED = "User: {}, {} deleted!";
            public static final String USER_UPDATED = "User: {} updated!";
            public static final String GOOGLE_USER_CREATED = "Google user: {} and gmail: {} saved in DB";
            public static final String FACEBOOK_USER_CREATED = "Facebook user: {} and gmail: {} saved in DB";
        }

        public class LogWarnMessages {

            private LogWarnMessages() {
            }

            public static final String USERNAME_EXIST = "Username: {} already exists!";
            public static final String USER_EMAIL_REPEATED = "Email: {} already exists!";
            public static final String USER_PASSWORD_NOT_VALID = "Invalid password! {} include {}";
            public static final String EMAIL_NOT_VERIFIED = "Email: {} could not verified!";
            public static final String PAGE_NUMBER_NEGATIVE = "Page number must be positive or zero!";
            public static final String PAGE_SIZE_NEGATIVE = "Page size number must be greater than 0!";
            public static final String SORT_PARAMETER_NOT_VALID = "Entered sort parameter not valid!";
        }

        public class LogErrorMessages {

            private LogErrorMessages() {
            }

            public static final String UNAUTHORIZED_ERROR = "Unauthorized error: {}";
            public static final String CANNOT_SET_USER_AUTHENTICATION = "Cannot set user authentication: {}";
            public static final String INVALID_JWT_TOKEN = "Invalid JWT token: {}";
            public static final String JWT_TOKEN_EXPIRED = "JWT token is expired: {}";
            public static final String JWT_TOKEN_UNSUPPORTED = "JWT token is unsupported: {}";
            public static final String JWT_CLAIMS_EMPTY = "JWT claims string is empty: {}";
        }
    }
}
