package io.github.yangyouwang.core.security.entity;

import java.util.Date;

/**
 * token持久化
 */
public class PersistentRememberMeToken {

    private String username;

    private String series;

    private String tokenValue;

    private Date date;

    public String getUsername() {
        return username;
    }

    public String getSeries() {
        return series;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public Date getDate() {
        return date;
    }
}
