package com.tericcabrel.authorization.events;

import com.tericcabrel.authorization.models.User;
import org.springframework.context.ApplicationEvent;

public class OnResetPasswordEvent extends ApplicationEvent {
    private User user;

    public OnResetPasswordEvent(User user) {
        super(user);

        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public OnResetPasswordEvent setUser(User user) {
        this.user = user;
        return this;
    }
}
