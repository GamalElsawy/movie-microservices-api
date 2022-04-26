package com.gamalelsawy.moviecatalogservice.resources;

import com.gamalelsawy.moviecatalogservice.models.CatalogItem;
import com.gamalelsawy.moviecatalogservice.models.Movie;
import com.gamalelsawy.moviecatalogservice.models.Rating;
import com.gamalelsawy.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = restTemplate.getForObject("http://localhost:8093/ratings/users/" + userId, UserRating.class);

        return ratings.getUserRatings().stream().map(rating -> {
                    Movie movie = restTemplate.getForObject("http://localhost:8092/movies/" + rating.getMovieId(), Movie.class);
                    /*Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://localhost:8092/movies/" + rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();*/
                    return new CatalogItem(movie.getName(), "desc", rating.getRating());
                })
                .collect(Collectors.toList());
    }
}
