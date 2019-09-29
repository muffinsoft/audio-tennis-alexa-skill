package com.muffinsoft.alexa.skills.audiotennis.constants;

public class SessionConstants extends com.muffinsoft.alexa.sdk.constants.SessionConstants {

    // chose direct activity on the start or from menu
    // handles only activity types or returns to the menu
    public static final String SELECT_ACTIVITY_STEP = "selectActivityStep";

    //same as above, but also handles negative and positive replies
    // positive will redirects to the possible activity
    public static final String SWITCH_ACTIVITY_STEP = "switchActivityStep";

    public static final String BLOCKED_ACTIVITY_CALL = "blockedActivityCall";

    // when activity just unlocked proposal to play in this activity
    // positive reply changes activity to new one, negative will continue old activity
    public static final String SWITCH_UNLOCK_ACTIVITY_STEP = "switchUnlockActivityStep";

    public static final String EXIT_FROM_HELP = "exitFromHelp";

    //positive reply redirects to the random opened activity
    // negative will proceed current activity
    public static final String ASK_RANDOM_SWITCH_ACTIVITY_STEP = "askRandomSwitchActivityStep";
    public static final String EXIT_FROM_ONE_POSSIBLE_ACTIVITY = "exitFromOnePossibleActivity";
    public static final String LAST_PURCHASE_ATTEMPT_ON = "lastPurchaseAttemptOn";
    public static final String PURCHASE_STATE = "purchaseState";
    public static final String NEW_ACTIVITY_OR_MENU = "newActivityOrMenu";
}
