package web.util;

public class InfluencerUtils {
    public static final String USERNAME_MATCHING_1 = "([a-z]+)(\\.|_)([a-z0-9]+)";
    public static final String USERNAME_MATCHING_2 = "([a-z]+)(\\.|_)?([0-9]+)";
    public static final String USERNAME_MATCHING_3 = "[a-z]+";

    public static int encodeUsername(String username) {
        if(username.matches(USERNAME_MATCHING_1)) return 1;
        if(username.matches(USERNAME_MATCHING_2)) return 2;
        if(username.matches(USERNAME_MATCHING_3)) return 3;
        return 0;
    }
}
