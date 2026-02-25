package com.example.context;

import com.example.bo.UserContext;

public class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    private UserContextHolder() {
    }

    public static void set(UserContext userContext) {
        UserContextHolder.userContext.set(userContext);
    }

    public static UserContext get() {
        return userContext.get();
    }

    public static void clear() {
        userContext.remove();
    }


}
