package com.backend.movie;

import com.backend.RouteConfig;
import com.backend.movie.dto.CreateMovieDTO;
import com.backend.movie.dto.MovieDetailDTO;
import com.backend.movie.dto.MovieFilterDTO;
import com.backend.movie.dto.UpdateMovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RouteConfig.MOVIE_BASE)
public class MovieController {
    @Autowired
    private MovieService movieService;

    // 1.2
    @GetMapping("filter")
    public List<MovieEntity> filterMovie(MovieFilterDTO input){
        return movieService.filterMovie(input);
    }

    // 1.4
    @GetMapping("detail/{id}")
    public MovieDetailDTO movieDetail(@PathVariable("id") String id){
        return movieService.movieDetail(id);
    }

    // 1.5
    @PostMapping()
    public MovieEntity createMovie(@RequestBody CreateMovieDTO input){
        return movieService.createMovie(input);
    }

    // 1.6
    @PutMapping()
    public MovieEntity updateMovie(@RequestBody UpdateMovieDTO input){
        return movieService.updateMovie(input);
    }

    // 1.7
    @DeleteMapping("{id}")
    public MovieEntity deleteMovie(@PathVariable("id") String id){
        return movieService.deleteMovie(id);
    }
}
