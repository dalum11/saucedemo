package data;

public final class Data {

    public static class Login {
        public static final String VALID_LOGIN = "standard_user";
        public static final String LOCKED_OUT_LOGIN = "locked_out_user";
        public static final String PROBLEM_LOGIN = "problem_user";
        public static final String PERFORMANCE_GLITCH_LOGIN = "performance_glitch_user";
        public static final String ERROR_LOGIN = "error_user";
        public static final String VISUAL_LOGIN = "visual_user";

        public static final String VALID_PASSWORD = "secret_sauce";
        public static final String INVALID_PASSWORD = "secret_sauce12";
    }

    public static class Endpoints {
        public static final String MAIN_PAGE = "inventory.html";
    }

    public static class ErrorMessages {
        public static final String BLOCKED_USER = "Epic sadface: Sorry, this user has been locked out.";
        public static final String INVALID_CREDENTIALS = "Epic sadface: Username and password do not match any user in this service";
        public static final String EMPTY_USERNAME = "Epic sadface: Username is required";
        public static final String EMPTY_PASSWORD = "Epic sadface: Password is required";

    }
}
