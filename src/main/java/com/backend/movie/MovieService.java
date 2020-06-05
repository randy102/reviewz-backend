package com.backend.movie;

import com.backend.Error;
import com.backend.actor.ActorEntity;
import com.backend.category.CategoryEntity;
import com.backend.director.DirectorEntity;
import com.backend.movie.dto.*;
import com.backend.review.ReviewService;
import com.backend.root.CRUD;
import com.backend.security.CurrentUser;
import com.backend.util.StringUtils;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class MovieService implements CRUD<MovieEntity, CreateMovieDTO, CreateMovieDTO> {
    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ReviewService reviewService;



    @Override
    public List<MovieEntity> getAll() {
        return movieRepository.findAll();
    }

    @Override
    public MovieEntity create(CreateMovieDTO input) {
        MovieEntity toCreateMovie = new MovieEntity();

        BeanUtils.copyProperties(input, toCreateMovie);
        toCreateMovie.setCreatedAt(new Date().getTime());
        toCreateMovie.setNameVnR(StringUtils.convertToRaw(input.getNameVn()));
        toCreateMovie.setSummaryR(StringUtils.convertToRaw(input.getSummary()));

        return movieRepository.save(toCreateMovie);
    }


    @Override
    public MovieEntity update(String id, CreateMovieDTO input) {
        MovieEntity existed = movieRepository.findById(id).orElse(null);
        if(existed == null) throw Error.NotFoundError("Movie");

        BeanUtils.copyProperties(input, existed);
        existed.setNameVnR(StringUtils.convertToRaw(input.getNameVn()));
        existed.setSummaryR(StringUtils.convertToRaw(input.getSummary()));

        return movieRepository.save(existed);
    }

    @Override
    public MovieEntity delete(String id) {
        MovieEntity existed = movieRepository.findById(id).orElse(null);
        if(existed == null) throw Error.NotFoundError("Movie");

        movieRepository.delete(existed);
        reviewService.deleteReviewsByMovie(existed.getId());

        return existed;
    }

    @Deprecated
    public List<MovieEntity> filterMovie(MovieFilterDTO input){
        Query query = new Query();

        if(input.getKeyword() != null){
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("nameVnR").regex(input.getKeyword(),"gmi"),
                    Criteria.where("summaryR").regex(input.getKeyword(), "gmi"),
                    Criteria.where("nameEn").regex(input.getKeyword(),"gmi")
            ));
        }

        if(input.getCategory() != null){
            query.addCriteria(Criteria.where("categories").is(input.getCategory()));
        }

        if(input.getYear() != 0){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, input.getYear());

            calendar.set(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            long startYear = calendar.getTimeInMillis();

            calendar.set(Calendar.MONTH, 12);
            calendar.set(Calendar.DAY_OF_MONTH, 30);
            long endYear = calendar.getTimeInMillis();

            query.addCriteria(Criteria.where("releaseDate").gte(startYear).lte(endYear));
        }

        if(input.getLastRelease() != null && input.getLastRelease().equals("false"))
            query.with(Sort.by(Sort.Direction.ASC, "releaseDate"));
        else // Sort by default
            query.with(Sort.by(Sort.Direction.DESC, "releaseDate"));

        if(input.getSkip() != 0){
            query.skip(input.getSkip());
        }

        if(input.getLimit() != 0){
            query.limit(input.getLimit());
        }

        return mongoTemplate.find(query, MovieEntity.class);
    }

    public List<MovieResponseDTO> filterMovie_2(MovieFilterDTO input){
        List<AggregationOperation> pipe = new ArrayList<>();

        // Keyword
        if(input.getKeyword() != null){
            pipe.add(Aggregation.match(new Criteria().orOperator(
                    Criteria.where("nameVnR").regex(input.getKeyword(),"gmi"),
                    Criteria.where("summaryR").regex(input.getKeyword(), "gmi"),
                    Criteria.where("nameEn").regex(input.getKeyword(),"gmi")
            )));
        }

        //Category
        if(input.getCategory() != null){
            pipe.add(Aggregation.match(Criteria.where("categories").is(input.getCategory())));
        }

        //Actor
        if(input.getActor() != null){
            pipe.add(Aggregation.match(Criteria.where("actors").is(input.getActor())));
        }

        //Director
        if(input.getDirector() != null){
            pipe.add(Aggregation.match(Criteria.where("directors").is(input.getDirector())));
        }

        // Year
        if(input.getYear() != 0){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, input.getYear());

            calendar.set(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            long startYear = calendar.getTimeInMillis();

            calendar.set(Calendar.MONTH, 12);
            calendar.set(Calendar.DAY_OF_MONTH, 30);
            long endYear = calendar.getTimeInMillis();

            pipe.add(Aggregation.match(Criteria.where("releaseDate").gte(startYear).lte(endYear)));
        }

        // Last released sort
        if(input.getLastRelease() != null && input.getLastRelease().equals("false"))
            pipe.add(Aggregation.sort(Sort.by(Sort.Direction.ASC, "releaseDate")));
        else // Sort by default (newest first)
            pipe.add(Aggregation.sort(Sort.by(Sort.Direction.DESC, "releaseDate")));


        pipe.add(Aggregation.project(MovieResponseDTO.class).and(ConvertOperators.ToString.toString("$_id")).as("_id"));

        // Rated
        pipe.add(Aggregation.lookup("mr_review","_id", "idMovie", "reviews"));
        // Filter Reviews
        pipe.add(Aggregation.project(MovieResponseDTO.class)
                .and(ArrayOperators.Filter.filter("$reviews")
                        .as("review")
                        .by(ComparisonOperators.valueOf("review.verified").equalToValue(true)))
                .as("reviews"));
        pipe.add(Aggregation.project(MovieResponseDTO.class).and(ArrayOperators.Size.lengthOfArray("reviews")).as("rated"));

        // Sort by number of review
        // ( highest first)
        if(input.getMostRated() != null && input.getMostRated().equals("true")){
            pipe.add(Aggregation.sort(Sort.by(Sort.Direction.DESC, "rated")));
        }
        // Reviews
        pipe.add(Aggregation.lookup("mr_review","_id", "idMovie", "reviews"));
        // Filter Reviews
        pipe.add(Aggregation.project(MovieResponseDTO.class)
                .and(ArrayOperators.Filter.filter("$reviews")
                        .as("review")
                        .by(ComparisonOperators.valueOf("review.verified").equalToValue(true)))
                .as("reviews"));
        // Star Avg
        pipe.add(Aggregation.project(MovieResponseDTO.class).and(AccumulatorOperators.Avg.avgOf("reviews.star")).as("starAvg"));
        pipe.add(Aggregation.project(MovieResponseDTO.class).and(ConditionalOperators.IfNull.ifNull("starAvg").then(0)).as("starAvg"));

        // Sort by Star ( highest first)
        if(input.getHighestStar() != null && input.getHighestStar().equals("true")){
            pipe.add(Aggregation.sort(Sort.by(Sort.Direction.DESC, "starAvg")));
        }


        // Skip
        if(input.getSkip() != 0){
            pipe.add(Aggregation.skip((long)input.getSkip()));
        }

        //Limit
        if(input.getLimit() != 0){
            pipe.add(Aggregation.limit(input.getLimit()));
        }

        Aggregation aggregation =  Aggregation.newAggregation(pipe);

        return mongoTemplate.aggregate(aggregation, "mr_movie", MovieResponseDTO.class).getMappedResults();
    }

    public MovieDetailDTO movieDetail(String id){
        MovieEntity existed = movieRepository.findById(id).orElse(null);
        if(existed == null)
            throw Error.NotFoundError("Movie");

        List<AggregationOperation> pipe = new ArrayList<>();
        pipe.add(Aggregation.match(Criteria.where("_id").is(id)));

        // Convert _id to string
        pipe.add(Aggregation.project(MovieDetailDTO.class).and(ConvertOperators.ToString.toString("$_id")).as("_id"));

        // Reviews
        pipe.add(Aggregation.lookup("mr_review","_id", "idMovie", "reviews"));

        // Filter Reviews
        pipe.add(Aggregation.project(MovieDetailDTO.class)
                .and(ArrayOperators.Filter.filter("$reviews").as("review").by(ComparisonOperators.valueOf("review.verified").equalToValue(true)))
                .as("reviews"));

        // Star Avg
        pipe.add(Aggregation.project(MovieDetailDTO.class).and(AccumulatorOperators.Avg.avgOf("reviews.star")).as("starAvg"));
        pipe.add(Aggregation.project(MovieDetailDTO.class).and(ConditionalOperators.IfNull.ifNull("starAvg").then(0)).as("starAvg"));


        // Reviews
        pipe.add(Aggregation.lookup("mr_review","_id", "idMovie", "reviews"));

        // Filter Reviews
        pipe.add(Aggregation.project(MovieDetailDTO.class)
                .and(ArrayOperators.Filter.filter("$reviews").as("review").by(ComparisonOperators.valueOf("review.verified").equalToValue(true)))
                .as("reviews"));
        // Rated
        pipe.add(Aggregation.project(MovieDetailDTO.class).and(ArrayOperators.Size.lengthOfArray("reviews")).as("rated"));

        MovieDetailDTO detail = mongoTemplate.aggregate(Aggregation.newAggregation(pipe), "mr_movie", MovieDetailDTO.class).getMappedResults().get(0);

        //Category
        Query categoryQuery = new Query(Criteria.where("_id").in(existed.getCategories()));
        List<CategoryEntity> categories = mongoTemplate.find(categoryQuery, CategoryEntity.class);
        detail.setCategoriesObj(categories);

        //Actor
        Query actorQuery = new Query(Criteria.where("_id").in(existed.getActors()));
        List<ActorEntity> actors = mongoTemplate.find(actorQuery, ActorEntity.class);
        detail.setActorsObj(actors);

        //Actor
        Query directorQuery = new Query(Criteria.where("_id").in(existed.getDirectors()));
        List<DirectorEntity> directors = mongoTemplate.find(directorQuery, DirectorEntity.class);
        detail.setDirectorsObj(directors);

        return detail;
    }
}
