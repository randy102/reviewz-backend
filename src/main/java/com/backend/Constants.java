package com.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class Constants {

    public Map<String, Set<String>> NO_AUTH_ROUTE = new HashMap<>();

    public Constants(){
        NO_AUTH_ROUTE.put("GET",new HashSet<>());
        NO_AUTH_ROUTE.put("POST",new HashSet<>());
        NO_AUTH_ROUTE.put("PUT",new HashSet<>());
        NO_AUTH_ROUTE.put("DELETE",new HashSet<>());

        NO_AUTH_ROUTE.get("POST").add("/api/user/login");
        NO_AUTH_ROUTE.get("PUT").add("/api/user/register");
    }
}
