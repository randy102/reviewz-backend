package com.backend.movie;

import com.backend.RouteConfig;
import com.backend.movie.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RouteConfig.MOVIE_BASE)
public class MovieController {
    @Autowired
    private MovieService movieService;

    // 1.2
    @GetMapping()
    public List<MovieEntity> allMovies(){ return movieService.getAll(); }

    // 1.2
    @GetMapping("filter")
    public List<MovieResponseDTO> filterMovie(MovieFilterDTO input){
        return movieService.filterMovie_2(input);
    }

    // 1.4
    @GetMapping("detail/{id}")
    public MovieDetailDTO movieDetail(@PathVariable("id") String id){
        return movieService.movieDetail(id);
    }

    // 1.5
    @PostMapping()
    @Secured("ROLE_ADMIN")
    public MovieEntity createMovie(@RequestBody CreateMovieDTO input){
        return movieService.create(input);
    }

    // 1.6
    @PutMapping("{id}")
    @Secured("ROLE_ADMIN")
    public MovieEntity updateMovie(@PathVariable("id") String id , @RequestBody CreateMovieDTO input){
        return movieService.update(id, input);
    }

    // 1.7
    @DeleteMapping("{id}")
    @Secured("ROLE_ADMIN")
    public boolean deleteMovie(@PathVariable("id") String id){
        return movieService.delete(id);
    }
}
