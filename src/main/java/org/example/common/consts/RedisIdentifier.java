package org.example.common.consts;

public class RedisIdentifier {
    public static final String MEMBER = "member_";
    public static final String REFRESH = "ref_";


    public static String addIdentifier(String identifier, String userId){
        StringBuilder sb = new StringBuilder()
                .append(identifier)
                .append(userId);
        return sb.toString();
    }
}
