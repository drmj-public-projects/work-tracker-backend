package com.drmj.work_tracker.utils;

public enum ErrorMessage {

    PLACE_NOT_FOUND_MESSAGE("Place not found"),
    WORK_SESSION_NOT_FOUND_MESSAGE("WorkSession not found"),
    USER_NOT_FOUND_MESSAGE("User not found"),
    ORGANIZATION_NOT_FOUND_MESSAGE("Organization not found"),
    ORGANIZATION_SETTINGS_NOT_FOUND_MESSAGE("Organization settings not found"),
    INTERNAL_SERVER_ERROR_MESSAGE("Internal server error"),
    USER_NOT_IN_ORG("User does not belong to organization"),
    LOCATION_REQUIRED("Location is required"),
    USER_HAS_ACTIVE_SESSION("User already has an active session"),
    RESOURCE_NOT_FOUND("Resource not found"),
    PLACE_NOT_IN_ORG("Place does not belong to organization"),
    SESSION_NOT_ACTIVE("The workSession is not in active status"),
    USER_NOT_OWNER_OF_SESSION("The user is not the owner of work session");

    private final String message;

    ErrorMessage( String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
