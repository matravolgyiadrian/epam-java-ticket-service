package com.epam.training.ticketservice.ui;

import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CustomPromptProvider implements PromptProvider {
    private final UserServiceImpl userService;

    public CustomPromptProvider(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public AttributedString getPrompt() {
        if (userService.isSignedIn()) {
            return new AttributedString("Ticket service (connected)>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
        } else {
            return new AttributedString("Ticket service>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
        }
    }

}
