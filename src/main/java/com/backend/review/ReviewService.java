package com.backend.review;

import com.backend.Error;
import com.backend.review.dto.CreateReviewDTO;
import com.backend.review.dto.UpdateReviewDTO;
import com.backend.security.CurrentUser;
import com.backend.security.RoleEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewService {
    @Autowired CurrentUser currentUser;

    @Autowired ReviewRepository reviewRepository;

    public List<ReviewEntity> getReviews(){
        return reviewRepository.findAll();
    }

    public List<ReviewEntity> getReviewByMovie(String id){
        return reviewRepository.findByIdMovie(id);
    }

    public List<ReviewEntity> getReviewByUser(String id){
        return reviewRepository.findByIdUser(id);
    }

    public ReviewEntity createReview(CreateReviewDTO input){
        ReviewEntity review = new ReviewEntity();

        BeanUtils.copyProperties(input, review);
        review.setIdUser(currentUser.getInfo().getId());
        review.setVerified(false);
        review.setCreatedAt(new Date().getTime());

        return reviewRepository.save(review);
    }

    public ReviewEntity updateReview(String id, UpdateReviewDTO input){
        ReviewEntity exited = reviewRepository.findById(id).orElse(null);
        if(exited == null) throw Error.NotFoundError("Review");

        BeanUtils.copyProperties(input,exited);
        exited.setVerified(false);

        return reviewRepository.save(exited);
    }


    public ReviewEntity verifyReview(String id){
        ReviewEntity exited = reviewRepository.findById(id).orElse(null);
        if(exited == null) throw Error.NotFoundError("Review");

        exited.setVerified(true);
        return reviewRepository.save(exited);
    }

    public ReviewEntity deleteReview(String id){
        ReviewEntity exited = reviewRepository.findById(id).orElse(null);
        if(exited == null) throw Error.NotFoundError("Review");

        // User only allow to delete its own review
        if(currentUser.getInfo().hasRole(RoleEnum.ROLE_USER)){
            if(!exited.getIdUser().equals(currentUser.getInfo().getId()))
                throw Error.NoPermissionError();
        }

        reviewRepository.delete(exited);
        return exited;
    }
}
