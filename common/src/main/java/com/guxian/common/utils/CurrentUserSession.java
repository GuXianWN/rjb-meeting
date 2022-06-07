package com.guxian.common.utils;

import com.guxian.common.entity.UserSession;
import com.mysql.cj.log.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
public class CurrentUserSession {
    private CurrentUserSession() {
    }

    private static final ThreadLocal<UserSession> userSession = new ThreadLocal<>();

    private static boolean closed=true;

    public static void setUserSession(UserSession userSession) {
        CurrentUserSession.userSession.set(userSession);
    }

    public static UserSession getUserSession() {
        if (closed) {
            log.warn("token 为debug 模式，不检查token");
            return new UserSession().setUserId(0L).setUserName("no-token-user");
        }
        assert CurrentUserSession.userSession != null;
        return CurrentUserSession.userSession.get();
    }

    public static void setUserSession(UserSession userSession, boolean closed) {
        CurrentUserSession.closed = closed;
        setUserSession(userSession);
    }
}
