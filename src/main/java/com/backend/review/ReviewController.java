package com.backend.review;

import com.backend.RouteConfig;
import com.backend.review.dto.CreateReviewDTO;
import com.backend.review.dto.ReviewResponseDTO;
import com.backend.review.dto.UpdateReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = RouteConfig.REVIEW_BASE)
public class ReviewController {
    @Autowired ReviewService reviewService;

    // 3.1
    @GetMapping()
    public List<ReviewResponseDTO> getReviews(){
        return reviewService.getAll();
    }

    // 3.2
    @GetMapping("movie/{id}")
    public List<ReviewResponseDTO> getReviewByMovie(@PathVariable("id") String idMovie){
        return reviewService.getReviewByMovie(idMovie);
    }

    // 3.7
    @GetMapping("user/{id}")
    public List<ReviewResponseDTO> getReviewByUser(@PathVariable("id") String idUser){
        return reviewService.getReviewByUser(idUser);
    }

    // 3.3
    @PostMapping("verify/{id}")
    @Secured("ROLE_ADMIN")
    public ReviewEntity verifyReview(@PathVariable("id") String id){
        return reviewService.verifyReview(id);
    }

    // 3.4
    @PostMapping()
    public ReviewEntity createReview(@Valid @RequestBody CreateReviewDTO input) {
        return reviewService.createReview(input);
    }

    // 3.5
    @PutMapping("{id}")
    public ReviewEntity updateReview(@Valid @RequestBody UpdateReviewDTO input, @PathVariable("id") String id){
        return reviewService.updateReview(id, input);
    }

    // 3.6
    @DeleteMapping("{id}")
    public boolean deleteReview(@PathVariable("id") String id){
        return reviewService.delete(id);
    }
}
