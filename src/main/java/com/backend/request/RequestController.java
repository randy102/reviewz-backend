package com.backend.request;

import com.backend.RouteConfig;
import com.backend.request.dto.CreateRequestDTO;
import com.backend.request.dto.RequestResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RouteConfig.REQUEST_BASE)
public class RequestController {
    @Autowired
    RequestService requestService;

    @Secured("ROLE_ADMIN")
    @GetMapping()
    public List<RequestResponseDTO> getRequests(){
        return requestService.getRequests();
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("resolve/{id}")
    public RequestEntity resolveRequest(@PathVariable("id") String id){
        return requestService.resolveRequest(id);
    }

    @PostMapping()
    public RequestEntity createRequest(@RequestBody CreateRequestDTO input){
        return requestService.createRequest(input);
    }
}
