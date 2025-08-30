package com.gohul.springSecurity.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomEventListener {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent successEvent) {
        log.error("Authentication is success for the user:: {}", successEvent.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failureEvent) {
        log.error("Authentication is failed for the user:: {} due to:: {}", failureEvent.getAuthentication().getName(), failureEvent.getException().getMessage());
    }
}
