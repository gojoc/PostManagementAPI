package ro.deloittedigital.samekh.postmanagement.configuration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ro.deloittedigital.samekh.postmanagement.model.domain.User;
import ro.deloittedigital.samekh.postmanagement.utility.JWTUtility;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";
    private final JWTUtility jwtUtility;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (ObjectUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(BEARER)) {
            chain.doFilter(request, response);
            return;
        }
        final String jwt = authorizationHeader.substring(BEARER.length());
        if (!jwtUtility.isValid(jwt) || jwtUtility.isExpired(jwt)) {
            chain.doFilter(request, response);
            return;
        }
        UserDetails userDetails = new User(jwtUtility.getEmail(jwt));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }
}
