package com.tericcabrel.authorization.events;

import org.springframework.context.ApplicationEvent;

import com.tericcabrel.authorization.models.entities.User;

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
