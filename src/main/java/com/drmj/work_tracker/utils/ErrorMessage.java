package com.drmj.work_tracker.utils;

import lombok.Getter;

@Getter
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
    INVALID_GROUP_BY("Invalid groupBy: "),
    PLACE_ID_REQUIRED("PlaceId is required"),
    MANUAL_NOT_ALLOWED("Register manual workSession is not allowed"),
    SESSION_OVERLAP("The user already have a session in the range time"),
    USER_WITH_NOT_PERMISSION_TO_UPDATE_WORK_SESSION("You do not have permission to update this work session"),
    ONLY_COMPLETED_SESSIONS_CAN_BE_UPDATED("Only completed work sessions can be edited"),
    NEW_RANGE_OVERLAPS("The new time range overlaps with another work session"),
    USER_WITH_EMAIL_NOT_FOUND("User with email does not been found"),
    INVALID_CREDENTIALS("Invalid credentials"),
    USER_NOT_ACCESS_TO_ORGANIZATION("The user does not have access to this organization"),
    HOURLY_RATE_NOT_FOUND_MESSAGE("The place does not have an hourly rate configured"),
    START_AND_END_DATE_REQUIRED_IN_CUSTOM("StartDate and EndDate are required for CUSTOM"),
    START_AND_END_DATE_REQUIRED("StartDate and EndDate are required"),
    PLACE_ALREADY_EXISTS("Place with this name already exists in the organization"),
    LOCATION_REQUIRED_FOR_ORG("Location is required for this organization"),
    USER_NOT_AUTHORIZED_TO_GENERATE_CODE("User does not have permission to generate invitation codes"),
    INVALID_INVITATION_CODE("Invalid or expired invitation code"),
    INVITATION_CODE_EXPIRED("Invitation code has expired"),
    INVITATION_CODE_MAX_USES_REACHED("Invitation code has reached maximum uses"),
    USER_ALREADY_MEMBER("User is already a member of this organization"),
    INVITATION_CODE_GENERATION_FAILED("Failed to generate unique invitation code"),
    USER_EMAIL_ALREADY_EXISTS("User with this email already exists"),
    USER_NAME_REQUIRED("User name is required"),
    USER_EMAIL_REQUIRED("User email is required"),
    USER_PASSWORD_REQUIRED("Password is required"),
    PASSWORD_TOO_SHORT("Password must be at least 6 characters"),
    USER_NOT_ALLOWED("The user does not have permission to register new users"),
    REGISTRATION_FAILED("Registration failed, please try again"),
    USER_UPDATE_FAILED("Failed to update user profile"),
    USER_ORGANIZATION_LIMIT_REACHED("You have reached the maximum number of organizations (2)"),
    ORGANIZATION_NAME_REQUIRED("Organization name is required"),
    USER_NOT_AUTHORIZED_TO_UPDATE_SETTINGS("User does not have permission to update organization settings"),
    PLACE_ID_OR_ORGANIZATION_ID_REQUIRED("Either placeId or organizationId is required"),
    ONLY_ONE_OF_PLACE_OR_ORGANIZATION_ALLOWED("Only one of placeId or organizationId should be provided");

    private final String message;

    ErrorMessage( String message) {
        this.message = message;
    }
}
