package com.epam.training.ticketservice.ui.valueproviders;

import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.service.RoomService;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomValueProvider extends ValueProviderSupport {

    private final RoomService roomService;

    public RoomValueProvider(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public List<CompletionProposal> complete(MethodParameter methodParameter,
                                             CompletionContext completionContext,
                                             String[] strings) {
        String currentInput = completionContext.currentWordUpToCursor();
        return this.roomService.findByName(currentInput)
                .stream()
                .map(Room::getName)
                .map(CompletionProposal::new)
                .collect(Collectors.toList());
    }
}
