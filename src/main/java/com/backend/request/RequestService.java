package com.backend.request;

import com.backend.Error;
import com.backend.request.dto.CreateRequestDTO;
import com.backend.request.dto.RequestResponseDTO;
import com.backend.review.ReviewRepository;
import com.backend.review.dto.ReviewResponseDTO;
import com.backend.security.CurrentUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RequestService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    CurrentUser currentUser;

    public List<RequestResponseDTO> getRequests(){
        List<AggregationOperation> pipe = new ArrayList<>();

        pipe.add(Aggregation.project(RequestResponseDTO.class).and(ConvertOperators.ToObjectId.toObjectId("$idUser")).as("idUser"));
        pipe.add(Aggregation.lookup("mr_user", "idUser", "_id", "user"));
        pipe.add(Aggregation.unwind("user"));
        pipe.add(Aggregation.project(RequestResponseDTO.class).and(ConvertOperators.ToString.toString("$idUser")).as("idUser"));

        return mongoTemplate
                .aggregate(Aggregation.newAggregation(pipe), "mr_request", RequestResponseDTO.class)
                .getMappedResults();
    }

    public RequestEntity createRequest(CreateRequestDTO input){
        RequestEntity req = new RequestEntity();
        BeanUtils.copyProperties(input, req);

        req.setIdUser(currentUser.getInfo().getId());
        req.setResolved(false);
        req.setCreatedAt(new Date().getTime());

        return requestRepository.save(req);
    }

    public RequestEntity resolveRequest(String id){
        RequestEntity existed = requestRepository.findById(id).orElse(null);
        if(existed == null) throw Error.NotFoundError("Request");

        existed.setResolved(true);
        return requestRepository.save(existed);
    }
}
