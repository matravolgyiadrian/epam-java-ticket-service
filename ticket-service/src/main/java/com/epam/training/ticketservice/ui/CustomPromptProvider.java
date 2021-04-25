package com.epam.training.ticketservice.ui;

import com.epam.training.ticketservice.service.UserService;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CustomPromptProvider implements PromptProvider {
    private final UserService userService;

    public CustomPromptProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public AttributedString getPrompt() {
        if (userService.isSignedIn()) {
            return new AttributedString("TicketService (connected)>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
        } else {
            return new AttributedString("TicketService>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
        }
    }

}
