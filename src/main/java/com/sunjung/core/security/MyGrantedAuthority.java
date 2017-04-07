package com.sunjung.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

/**
 * Created by 为 on 2017-4-7.
 */
public final class MyGrantedAuthority implements GrantedAuthority {
    private final String auth;

    public MyGrantedAuthority(String auth) {
        Assert.hasText(auth, "A granted authority textual representation is required");
        this.auth = auth;
    }

    @Override
    public String getAuthority() {
        return this.auth;
    }

    public boolean equals(Object obj) {
        return this == obj?true:(obj instanceof MyGrantedAuthority ?this.auth.equals(((MyGrantedAuthority)obj).auth):false);
    }

    public int hashCode() {
        return this.auth.hashCode();
    }

    public String toString() {
        return this.auth;
    }
}
