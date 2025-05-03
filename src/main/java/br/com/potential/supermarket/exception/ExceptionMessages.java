package br.com.potential.supermarket.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {

    public static final String CATEGORY_CODE_MUST_BE_INFORMED = "The category code must be informed.";
    public static final String CATEGORY_CODE_NOT_FOUND = "There's no category for the given code";
    public static final String CATEGORY_DESCRIPTION_MUST_BE_INFORMED = "The category description must be informed.";
    public static final String CATEGORY_ID_MUST_BE_INFORMED = "The category id must be informed.";
    public static final String CATEGORY_ID_NOT_FOUND = "There's no category for the given id";
    public static final String PRODUCT_ID_NOT_FOUND = "There's no product for the given id";
    public static final String PRODUCT_NAME_MUST_BE_INFORMED = "The product name must be informed.";
    public static final String QUANTITY_AVAILABLE_MUST_BE_INFORMED = "The quantity available must be informed.";
    public static final String QUANTITY_LESS_OR_EQUAL_ZERO = "The quantity should not be less or equal to zero";
    public static final String SUPPLIER_ID_MUST_BE_INFORMED = "The supplier id must be informed.";
    public static final String SUPPLIER_ID_NOT_FOUND = "There's no supplier for the given id";
    public static final String SUPPLIER_NAME_MUST_BE_INFORMED = "The supplier's name must be informed.";
}
