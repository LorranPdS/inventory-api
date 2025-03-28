package br.com.potential.inventory.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {

    public static final String CATEGORY_CODE_MUST_BE_INFORMED = "The category code must be informed.";
    public static final String CATEGORY_CODE_NOT_FOUND = "There's no category for the given code";
    public static final String CATEGORY_DESCRIPTION_MUST_BE_INFORMED = "The category description must be informed.";
    public static final String CATEGORY_ID_MUST_BE_INFORMED = "The category id must be informed.";
    public static final String CATEGORY_ID_NOT_FOUND = "There's no category for the given id";
    public static final String INVALID_FORMAT_UUID = "The provided UUID format is not valid";
}
