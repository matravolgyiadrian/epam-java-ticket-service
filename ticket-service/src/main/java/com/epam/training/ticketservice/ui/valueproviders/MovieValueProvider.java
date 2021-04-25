package com.epam.training.ticketservice.ui.valueproviders;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.service.MovieService;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieValueProvider extends ValueProviderSupport {

    private final MovieService movieService;

    public MovieValueProvider(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public List<CompletionProposal> complete(MethodParameter methodParameter,
                                             CompletionContext completionContext,
                                             String[] strings) {
        String currentInput = completionContext.currentWordUpToCursor();
        return this.movieService.findByTitle(currentInput)
                .stream()
                .map(Movie::getTitle)
                .map(CompletionProposal::new)
                .collect(Collectors.toList());
    }
}
