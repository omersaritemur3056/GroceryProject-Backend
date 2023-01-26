package com.example.grocery.business.constants;

public class Messages {

    public class ErrorMessages {
        public static final String ID_NOT_FOUND = "Entered id not found in DB!";
        public static final String CATEGORY_ID_NOT_FOUND = "Entered category id not found in DB";
        public static final String PRODUCER_ID_NOT_FOUND = "Entered producer id not found in DB";
        public static final String SUPPLIER_ID_NOT_FOUND = "Entered supplier id not found in DB";
        public static final String USER_ID_NOT_FOUND = "Entered user id not found in DB";
        public static final String PRODUCT_ID_NOT_FOUND = "Entered product id not found in DB";
        public static final String CUSTOMER_ID_NOT_FOUND = "Entered customer id not found in DB";
        public static final String PAYMENT_ID_NOT_FOUND = "Entered payment id not found in DB";

        public static final String CATEGORY_NAME_REPEATED = "Category name can not be repeat!";
        public static final String PRODUCT_NAME_REPEATED = "Product name can not be repeat!";
        public static final String SUPPLIER_NAME_REPEATED = "Supplier name can not be repeat!";
        public static final String PRODUCER_NAME_REPEATED = "Producer name can not be repeat!";
        public static final String TAX_NUMBER_REPEATED = "Tax number can not be repeat!";
        public static final String NATIONAL_IDENTITY_REPEATED = "National Identity can not be repeated!";

        public static final String EMAIL_REPEATED = "Email address can not be repeat!";
        public static final String PHONE_NUMBER_REPEATED = "Phone number can not be repeat!";
        public static final String PASSWORD_NOT_VALID = "Password can not contains username!";
        public static final String AGE_NOT_PERMISSIBLE = "Employee is must older than 18";
        public static final String DEBIT_CARD_NOT_VALID = "Debit card informations not valid!";
        public static final String USER_EMAIL_NOT_FOUND = "Entered user email not found in DB!";
        public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token is not in DB!";
        public static final String USERNAME_EXIST = "Username already exists";
        public static final String ROLE_NOT_FOUND = "Role not found in DB!";
        public static final String UNSUPPORTED_FORMAT = "Unsupported format!";
        public static final String IMAGE_URL_NOT_FOUND = "Entered image url not found in DB!";
        public static final String IMAGE_ID_NOT_FOUND = "Entered image id not found in DB!";
        public static final String FILE_IS_NULL = "Sending file is null!";

    }

    public class GetListMessages {
        public static final String USERS_LISTED = "Users listed!";
        public static final String SUPPLIERS_LISTED = "Suppliers listed!";
        public static final String PRODUCTS_LISTED = "Products listed!";
        public static final String PRODUCERS_LISTED = "Producers listed!";
        public static final String INDIVIDUAL_CUSTOMERS_LISTED = "Individual customers listed!";
        public static final String EMPLOYEES_LISTED = "Employees listed!";
        public static final String CUSTOMERS_LISTED = "Customers listed!";
        public static final String CORPORATE_CUSTOMERS_LISTED = "Corporate customers listed!";
        public static final String CATEGORIES_LISTED = "Categories listed!";
        public static final String PAYMENTS_LISTED = "Debit cards listed!";
        public static final String ORDERS_LISTED = "Orders listed!";
        public static final String IMAGES_LISTED = "Images listed!";
    }

    public class GetByIdMessages {
        public static final String USER_LISTED = "User listed by entered id!";
        public static final String SUPPLIER_LISTED = "Supplier listed by entered id!";
        public static final String PRODUCT_LISTED = "Product listed by entered id!";
        public static final String PRODUCER_LISTED = "Producer listed by entered id!";
        public static final String INDIVIDUAL_CUSTOMER_LISTED = "Individual customer listed by entered id!";
        public static final String EMPLOYEE_LISTED = "Employee listed by entered id!";
        public static final String CUSTOMER_LISTED = "Customer listed by entered id!";
        public static final String CORPORATE_CUSTOMER_LISTED = "Corporate customer listed by entered id!";
        public static final String CATEGORY_LISTED = "Category listed by entered id!";
        public static final String PAYMENT_LISTED = "Debit card listed by entered id!";
        public static final String ORDER_LISTED = "Order listed by entered id!";
        public static final String IMAGE_LISTED = "Image listed by entered id!";
    }

    public class CreateMessages {
        public static final String PRODUCT_CREATED = "Product saved to DB!";
        public static final String PRODUCER_CREATED = "Producer saved to DB!";
        public static final String SUPPLIER_CREATED = "Supplier saved to DB!";
        public static final String CATEGORY_CREATED = "Category saved to DB!";
        public static final String EMPLOYEE_CREATED = "Employee saved to DB!";
        public static final String INDIVIDUAL_CUSTOMER_CREATED = "Individual customer saved to DB!";
        public static final String CORPORATE_CUSTOMER_CREATED = "Corporate customer saved to DB!";
        public static final String USER_CREATED = "User saved to DB!";
        public static final String PAYMENT_CREATED = "Debit card information saved to DB";
        public static final String ORDER_CREATED = "Order saved to DB!";
        public static final String IMAGE_UPLOADED_AND_ADDED = "Image uploaded and saved to DB!";
    }

    public class DeleteMessages {
        public static final String PRODUCT_DELETED = "Product removed from DB!";
        public static final String PRODUCER_DELETED = "Producer removed from DB!";
        public static final String SUPPLIER_DELETED = "Supplier removed from DB!";
        public static final String CATEGORY_DELETED = "Category removed from DB!";
        public static final String EMPLOYEE_DELETED = "Employee removed from DB!";
        public static final String INDIVIDUAL_CUSTOMER_DELETED = "Individual customer removed from DB!";
        public static final String CORPORATE_CUSTOMER_DELETED = "Corporate customer removed from DB!";
        public static final String USER_DELETED = "User removed from DB";
        public static final String PAYMENT_DELETED = "Debit card information removed from DB";
        public static final String ORDER_DELETED = "Order removed from DB!";
        public static final String SIGN_OUT = "Log out successful!";
        public static final String IMAGE_DELETED = "Image removed from DB!";
    }

    public class UpdateMessages {
        public static final String PRODUCT_MODIFIED = "Product updated!";
        public static final String PRODUCER_MODIFIED = "Producer updated!";
        public static final String SUPPLIER_MODIFIED = "Supplier updated!";
        public static final String CATEGORY_MODIFIED = "Category updated!";
        public static final String EMPLOYEE_MODIFIED = "Employee updated!";
        public static final String INDIVIDUAL_CUSTOMER_MODIFIED = "Individual customer updated!";
        public static final String CORPORATE_CUSTOMER_MODIFIED = "Corporate customer updated!";
        public static final String USER_MODIFIED = "User updated!";
        public static final String PAYMENT_UPDATED = "Debit card updated!";
        public static final String ORDER_UPDATED = "Order updated!";
        public static final String IMAGE_UPDATED_AND_ADDED = "Image updated and saved to DB!";
    }

    public class GetByUrlMessages {

        public static final String IMAGE_LISTED = "Image listed by entered url!";

    }

    public class LogMessages {

        public class LogInfoMessages {

            public static final String SAVED_IMAGE_URL = "saved image url: {}";
            public static final String DELETED_IMAGE_URL = "deleted image url: {}";
            public static final String UPDATED_IMAGE_URL = "updated image url: {}";
            public static final String CATEGORY_ADDED = "Category: {} added!";
            public static final String CATEGORY_DELETED = "Category: {} deleted!";
            public static final String CATEGORY_UPDATED = "Category: {} updated!";
            public static final String PRODUCT_ADDED = "Product: {} added!";
            public static final String PRODUCT_DELETED = "Product: {} deleted!";
            public static final String PRODUCT_UPDATED = "Product: {} updated!";
            public static final String SUPPLIER_ADDED = "Supplier: {} added!";
            public static final String SUPPLIER_DELETED = "Supplier: {} deleted!";
            public static final String SUPPLIER_UPDATED = "Supplier: {} updated!";
            public static final String PRODUCER_ADDED = "Producer: {} added!";
            public static final String PRODUCER_DELETED = "Producer: {} deleted!";
            public static final String PRODUCER_UPDATED = "Producer: {} updated!";
            public static final String EMPLOYEE_ADDED = "Employee: {} {} added!";
            public static final String EMPLOYEE_DELETED = "Employee: {} {} deleted!";
            public static final String EMPLOYEE_UPDATED = "Employee: {} {} updated!";
            public static final String INDIVIDUAL_CUSTOMER_ADDED = "Individual customer: {} {} added!";
            public static final String INDIVIDUAL_CUSTOMER_DELETED = "Individual customer: {} {} deleted!";
            public static final String INDIVIDUAL_CUSTOMER_UPDATED = "Individual customer: {} {} updated!";
            public static final String CORPORATE_CUSTOMER_ADDED = "Corporate customer: {} added!";
            public static final String CORPORATE_CUSTOMER_DELETED = "Corporate customer: {} deleted!";
            public static final String CORPORATE_CUSTOMER_UPDATED = "Corporate customer: {} updated!";
            public static final String ORDER_CREATED = "Order= Customer id: {}, Payment id: {}, Product ids: {} saved to DB";
            public static final String ORDER_DELETED = "Order id: {} removed from DB";
            public static final String ORDER_UPDATED = "Order id: {} -> Order= Customer id: {}, Payment id: {}, Product ids: {} updated";
            public static final String PAYMENT_CREATED = "Card number: {}, Fullname: {}, Card expiration year: {}, Card expiration month: {}, Card cvv: {} has been successfully saved to the database";
            public static final String PAYMENT_DELETED = "Card id: {} has been successfully removed from DB";
            public static final String PAYMENT_UPDATED = "Card information id: {} -> Card number: {}, Fullname: {}, Card expiration year: {}, Card expiration month: {}, Card cvv: {} has been successfully updated";
            public static final String USER_CREATED = "User: {} saved in DB!";
            public static final String USER_LOGINED = "User: {} logined!";

        }

        public class LogWarnMessages {

            public static final String CATEGORY_NAME_REPEATED = "Category name: {} already exists!";
            public static final String PRODUCT_NAME_REPEATED = "Product name: {} already exists!";
            public static final String PRODUCER_NAME_REPEATED = "Producer name: {} already exists!";
            public static final String SUPPLIER_NAME_REPEATED = "Supplier name: {} already exists!";
            public static final String SUPPLIER_EMAIL_REPEATED = "Supplier email: {} already exists!";
            public static final String SUPPLIER_PHONE_NUMBER_REPEATED = "Supplier phone number: {} already exists!";
            public static final String NATIONAL_IDENTITY_REPEATED = "National Identity: {} already exists!";
            public static final String AGE_NOT_PERMISSIBLE = "Entered birthdate: {} not permissible!";
            public static final String TAX_NUMBER_REPEATED = "Tax number: {} already exists!";
            public static final String DEBIT_CARD_NOT_VALID = "Card informations {}, {}, {}, {}, {} not valid!";
            public static final String USERNAME_EXIST = "Username: {} already exists!";
            public static final String USER_EMAIL_REPEATED = "Email: {} already exists!";
            public static final String USER_PASSWORD_NOT_VALID = "Invalid password! {} include {}";
            public static final String UNSUPPORTED_FORMAT = "Unsupported format!";

        }

        public class LogErrorMessages {

            public static final String UNAUTHORIZED_ERROR = "Unauthorized error: {}";
            public static final String CANNOT_SET_USER_AUTHENTICATION = "Cannot set user authentication: {}";
            public static final String INVALID_JWT_TOKEN = "Invalid JWT token: {}";
            public static final String JWT_TOKEN_EXPIRED = "JWT token is expired: {}";
            public static final String JWT_TOKEN_UNSUPPORTED = "JWT token is unsupported: {}";
            public static final String JWT_CLAIMS_EMPTY = "JWT claims string is empty: {}";
            public static final String UPLOAD_FAILED = "Cloudinary upload failed! IOException message: {}";
            public static final String DELETE_FAILED = "Cloudinary delete failed! IOException message: {}";
            public static final String FILE_IS_NULL = "Sending file is null!";

        }
    }
}
