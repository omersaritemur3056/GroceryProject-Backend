package com.example.grocery.business.constants;

public class Messages {

    public class ErrorMessages {
        public static final String idNotFound = "Entered id not found on DB!";
        public static final String categoryIdNotFound = "Entered category id not found on DB";
        public static final String producerIdNotFound = "Entered producer id not found on DB";
        public static final String supplierIdNotFound = "Entered supplier id not found on DB";

        public static final String categoryNameRepeated = "Category name can not be repeat!";
        public static final String productNameRepeated = "Product name can not be repeat!";
        public static final String supplierNameRepeated = "Supplier name can not be repeat!";
        public static final String producerNameRepeated = "Producer name can not be repeat!";
        public static final String taxNumberRepeated = "Tax number can not be repeat!";
        public static final String nationalIdentityRepeated = "National Identity can not be repeated!";

        public static final String emailRepeated = "Email address can not be repeat!";
        public static final String phoneNumberRepeated = "Phone number can not be repeat!";
        public static final String individualCustomerPasswordNotValid = "Password can not include firstname or lastname";
        public static final String employeePasswordNotValid = "Password can not include firstname, lastname or year of birth";
        public static final String ageNotPermissible = "Employee is must older than 18";
    }

    public class GetListMessages {
        public static final String usersListed = "Users listed!";
        public static final String suppliersListed = "Suppliers listed!";
        public static final String productsListed = "Products listed!";
        public static final String producersListed = "Producers listed!";
        public static final String individualCustomersListed = "Individual customers listed!";
        public static final String employeesListed = "Employees listed!";
        public static final String customersListed = "Customers listed!";
        public static final String corporateCustomersListed = "Corporate customers listed!";
        public static final String categoriesListed = "Categories listed!";
    }

    public class GetByIdMessages {
        public static final String userListed = "User listed by entered id!";
        public static final String supplierListed = "Supplier listed by entered id!";
        public static final String productListed = "Product listed by entered id!";
        public static final String producerListed = "Producer listed by entered id!";
        public static final String individualCustomerListed = "Individual customer listed by entered id!";
        public static final String employeeListed = "Employee listed by entered id!";
        public static final String customerListed = "Customer listed by entered id!";
        public static final String corporateCustomerListed = "Corporate customer listed by entered id!";
        public static final String categoryListed = "Category listed by entered id!";
    }

    public class CreateMessages {
        public static final String productCreated = "Product added!";
        public static final String producerCreated = "Producer added!";
        public static final String supplierCreated = "Supplier added!";
        public static final String categoryCreated = "Category added!";
        public static final String employeeCreated = "Employee added!";
        public static final String individualCustomerCreated = "Individual customer added!";
        public static final String corporateCustomerCreated = "Corporate customer added!";
    }

    public class DeleteMessages {
        public static final String productDeleted = "Product removed from DB!";
        public static final String producerDeleted = "Producer removed from DB!";
        public static final String supplierDeleted = "Supplier removed from DB!";
        public static final String categoryDeleted = "Category removed from DB!";
        public static final String employeeDeleted = "Employee removed from DB!";
        public static final String individualCustomerDeleted = "Individual customer removed from DB!";
        public static final String corporateCustomerDeleted = "Corporate customer removed from DB!";
    }

    public class UpdateMessages {
        public static final String productModified = "Product updated!";
        public static final String producerModified = "Producer updated!";
        public static final String supplierModified = "Supplier updated!";
        public static final String categoryModified = "Category updated!";
        public static final String employeeModified = "Employee updated!";
        public static final String individualCustomerModified = "Individual customer updated!";
        public static final String corporateCustomerModified = "Corporate customer updated!";
    }

    public class LogMessages {

    }
}
