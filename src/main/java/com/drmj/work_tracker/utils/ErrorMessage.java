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
    USER_NOT_OWNER_OF_SESSION("The user is not the owner of work session"),
    INVALID_TIME_RANGE("Invalid time range"),
    MANUAL_NOT_ALLOWED("Register manual workSession is not allowed"),
    SESSION_OVERLAP("The user already have a session in the range time"),
    USER_WITH_NOT_PERMISSION_TO_UPDATE_WORK_SESSION("You do not have permission to update this work session"),
    ONLY_COMPLETED_SESSIONS_CAN_BE_UPDATED("Only completed work sessions can be edited"),
    NEW_RANGE_OVERLAPS("The new time range overlaps with another work session"),
    USER_WITH_EMAIL_NOT_FOUND("User with email does not been found"),
    INVALID_CREDENTIALS("Invalid credentials"),
    USER_NOT_ACCESS_TO_ORGANIZATION("The user does not have access to this organization");

    private final String message;

    ErrorMessage( String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
