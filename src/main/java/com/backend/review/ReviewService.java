package com.backend.review;

import com.backend.Error;
import com.backend.review.dto.CreateReviewDTO;
import com.backend.review.dto.ReviewResponseDTO;
import com.backend.review.dto.UpdateReviewDTO;
import com.backend.root.CRUD;
import com.backend.security.CurrentUser;
import com.backend.security.RoleEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewService implements CRUD<ReviewResponseDTO, CreateReviewDTO, UpdateReviewDTO> {
    @Autowired CurrentUser currentUser;

    @Autowired ReviewRepository reviewRepository;

    @Autowired MongoTemplate mongoTemplate;

    private List<AggregationOperation> joinAggregate(){
        List<AggregationOperation> pipe = new ArrayList<>();
        pipe.add(Aggregation.project(ReviewResponseDTO.class).and(ConvertOperators.ToObjectId.toObjectId("$idUser")).as("idUser"));
        pipe.add(Aggregation.lookup("mr_user", "idUser", "_id", "user"));
        pipe.add(Aggregation.unwind("user"));
        pipe.add(Aggregation.project(ReviewResponseDTO.class).and(ConvertOperators.ToString.toString("$idUser")).as("idUser"));

        pipe.add(Aggregation.project(ReviewResponseDTO.class).and(ConvertOperators.ToObjectId.toObjectId("$idMovie")).as("idMovie"));
        pipe.add(Aggregation.lookup("mr_movie", "idMovie", "_id", "movie"));
        pipe.add(Aggregation.unwind("movie"));
        pipe.add(Aggregation.project(ReviewResponseDTO.class).and(ConvertOperators.ToString.toString("$idMovie")).as("idMovie"));
        return pipe;
    }

    @Override
    public List<ReviewResponseDTO> getAll() {
        List<AggregationOperation> pipe = joinAggregate();
        return mongoTemplate
                .aggregate(Aggregation.newAggregation(pipe), "mr_review", ReviewResponseDTO.class)
                .getMappedResults();
    }

    @Override
    @Deprecated
    public ReviewResponseDTO create(CreateReviewDTO input) {
        return null;
    }

    public List<ReviewResponseDTO> getReviewByMovie(String id){
        List<AggregationOperation> pipe = joinAggregate();
        pipe.add(Aggregation.match(Criteria.where("idMovie").is(id)));
        return mongoTemplate
                .aggregate(Aggregation.newAggregation(pipe), "mr_review", ReviewResponseDTO.class)
                .getMappedResults();
    }

    public List<ReviewResponseDTO> getReviewByUser(String id){
        List<AggregationOperation> pipe = joinAggregate();
        pipe.add(Aggregation.match(Criteria.where("idUser").is(id)));
        return mongoTemplate
                .aggregate(Aggregation.newAggregation(pipe), "mr_review", ReviewResponseDTO.class)
                .getMappedResults();
    }


    public ReviewEntity createReview(CreateReviewDTO input) {
        ReviewEntity review = new ReviewEntity();

        BeanUtils.copyProperties(input, review);
        review.setIdUser(currentUser.getInfo().getId());
        review.setVerified(false);
        review.setCreatedAt(new Date().getTime());

        return reviewRepository.save(review);
    }


    public ReviewEntity updateReview(String id, UpdateReviewDTO input) {
        ReviewEntity exited = reviewRepository.findById(id).orElse(null);
        if(exited == null) throw Error.NotFoundError("Review");

        BeanUtils.copyProperties(input,exited);
        exited.setVerified(false);

        return reviewRepository.save(exited);
    }

    @Override
    @Deprecated
    public ReviewResponseDTO update(String id, UpdateReviewDTO input) {
        return null;
    }

    public ReviewEntity verifyReview(String id){
        ReviewEntity exited = reviewRepository.findById(id).orElse(null);
        if(exited == null) throw Error.NotFoundError("Review");

        exited.setVerified(true);
        return reviewRepository.save(exited);
    }

    @Override
    public ReviewResponseDTO delete(String id) {
        ReviewEntity exited = reviewRepository.findById(id).orElse(null);
        if(exited == null) throw Error.NotFoundError("Review");

        // User only allow to delete its own review
        if(currentUser.getInfo().hasRole(RoleEnum.ROLE_USER)){
            if(!exited.getIdUser().equals(currentUser.getInfo().getId()))
                throw Error.NoPermissionError();
        }

        reviewRepository.delete(exited);
        return (ReviewResponseDTO) exited;
    }

    public void deleteReviewsByMovie(String idMovie){
        List<ReviewEntity> exitedReviews = reviewRepository.findByIdMovie(idMovie);
        reviewRepository.deleteAll(exitedReviews);
    }
}
