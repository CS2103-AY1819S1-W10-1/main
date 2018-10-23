package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");

    public static final Prefix PREFIX_MODULECODE = new Prefix("mc/");
    public static final Prefix PREFIX_MODULETITLE = new Prefix("mt/");
    public static final Prefix PREFIX_ACADEMICYEAR = new Prefix("ay/");
    public static final Prefix PREFIX_SEMESTER = new Prefix("sem/");

    public static final Prefix PREFIX_OCCASION_NAME = new Prefix("on/");
    public static final Prefix PREFIX_OCCASION_DATE = new Prefix("od/");
    public static final Prefix PREFIX_LOCATION = new Prefix("l/");
    public static final Prefix PREFIX_ORGANIZER = new Prefix("org/");
}
