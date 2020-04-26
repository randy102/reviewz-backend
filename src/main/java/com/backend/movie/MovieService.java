package com.backend.movie;

import com.backend.Error;
import com.backend.category.CategoryEntity;
import com.backend.movie.dto.*;
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
public class MovieService {
    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<MovieEntity> allMovies(){
        return movieRepository.findAll();
    }

    public MovieEntity createMovie(CreateMovieDTO input){
        MovieEntity toCreateMovie = new MovieEntity();

        BeanUtils.copyProperties(input, toCreateMovie);
        toCreateMovie.setCreatedAt(new Date().getTime());
        toCreateMovie.setNameVnR(StringUtils.convertToRaw(input.getNameVn()));
        toCreateMovie.setSummaryR(StringUtils.convertToRaw(input.getSummary()));

        return movieRepository.save(toCreateMovie);
    }


    public MovieEntity updateMovie(String id, CreateMovieDTO input){
        MovieEntity existed = movieRepository.findById(id).orElse(null);
        if(existed == null) throw Error.NotFoundError("Movie");

        BeanUtils.copyProperties(input, existed);
        existed.setNameVnR(StringUtils.convertToRaw(input.getNameVn()));
        existed.setSummaryR(StringUtils.convertToRaw(input.getSummary()));

        return movieRepository.save(existed);
    }

    public MovieEntity deleteMovie(String id){
        MovieEntity existed = movieRepository.findById(id).orElse(null);
        if(existed == null) throw Error.NotFoundError("Movie");

        movieRepository.delete(existed);

        return existed;
    }

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

        if(input.getKeyword() != null){
            pipe.add(Aggregation.match(new Criteria().orOperator(
                    Criteria.where("nameVnR").regex(input.getKeyword(),"gmi"),
                    Criteria.where("summaryR").regex(input.getKeyword(), "gmi"),
                    Criteria.where("nameEn").regex(input.getKeyword(),"gmi")
            )));
        }

        if(input.getCategory() != null){
            pipe.add(Aggregation.match(Criteria.where("categories").is(input.getCategory())));
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

            pipe.add(Aggregation.match(Criteria.where("releaseDate").gte(startYear).lte(endYear)));
        }

        if(input.getLastRelease() != null && input.getLastRelease().equals("false"))
            pipe.add(Aggregation.sort(Sort.by(Sort.Direction.ASC, "releaseDate")));
        else // Sort by default
            pipe.add(Aggregation.sort(Sort.by(Sort.Direction.DESC, "releaseDate")));

        if(input.getSkip() != 0){
            pipe.add(Aggregation.skip((long)input.getSkip()));
        }

        if(input.getLimit() != 0){
            pipe.add(Aggregation.limit(input.getLimit()));
        }

        pipe.add(Aggregation.project(MovieResponseDTO.class).and(ConvertOperators.ToString.toString("$_id")).as("_id"));
        pipe.add(Aggregation.lookup("mr_review","_id", "idMovie", "reviews"));
        pipe.add(Aggregation.project(MovieResponseDTO.class).and(AccumulatorOperators.Avg.avgOf("reviews.star")).as("starAvg"));
        pipe.add(Aggregation.project(MovieResponseDTO.class).and(ConditionalOperators.IfNull.ifNull("starAvg").then(0)).as("starAvg"));

        Aggregation aggregation =  Aggregation.newAggregation(pipe);

        return mongoTemplate.aggregate(aggregation, "mr_movie", MovieResponseDTO.class).getMappedResults();
    }

    public MovieDetailDTO movieDetail(String id){
        MovieEntity existed = movieRepository.findById(id).orElse(null);
        if(existed == null)
            throw Error.NotFoundError("Movie");

        List<AggregationOperation> pipe = new ArrayList<>();
        pipe.add(Aggregation.match(Criteria.where("_id").is(id)));
        pipe.add(Aggregation.project(MovieResponseDTO.class).and(ConvertOperators.ToString.toString("$_id")).as("_id"));
        pipe.add(Aggregation.lookup("mr_review","_id", "idMovie", "reviews"));
        pipe.add(Aggregation.project(MovieResponseDTO.class).and(AccumulatorOperators.Avg.avgOf("reviews.star")).as("starAvg"));
        pipe.add(Aggregation.project(MovieResponseDTO.class).and(ConditionalOperators.IfNull.ifNull("starAvg").then(0)).as("starAvg"));
        MovieResponseDTO movie = mongoTemplate.aggregate(Aggregation.newAggregation(pipe), "mr_movie", MovieResponseDTO.class).getMappedResults().get(0);

        Query categoryQuery = new Query(Criteria.where("_id").in(existed.getCategories()));
        List<CategoryEntity> categories = mongoTemplate.find(categoryQuery, CategoryEntity.class);

        MovieDetailDTO detail = new MovieDetailDTO();

        BeanUtils.copyProperties(movie, detail);
        detail.setCategoriesObj(categories);

        return detail;
    }
}
