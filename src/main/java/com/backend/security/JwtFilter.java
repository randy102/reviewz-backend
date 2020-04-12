package com.backend.security;

import com.backend.RouteConfig;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RouteConfig routeConfig;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    public RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        final String token = request.getHeader("Authorization");



        if (token == null)
            throw new ServletException("Not found: Token!");

        if (!token.startsWith("Bearer "))
            throw new ServletException("Incorrect: Token");

        String jwt = token.substring(7);

        if (!jwtUtil.verify(jwt))
            throw new ServletException("Token invalid");

        Claims claims = jwtUtil.decode(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        if (userDetails == null) throw new ServletException("Not found: User");

        //Set context
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String method = request.getMethod();
        Stream<String> noAuthRoutes = routeConfig.NO_AUTH_ROUTE.get(method).stream();
        boolean inNoAuthRoute = noAuthRoutes.anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));

        String clientHost = request.getServerName();
        boolean notAllowedDomain = !clientHost.equals("review-movie-project.herokuapp.com");
        // if(notAllowedDomain)
        //    throw new ServletException("Domain denied!");

        // boolean notApi = request.getRequestURI().matches("^(?!/?api).+$");
        return inNoAuthRoute;
    }
}
