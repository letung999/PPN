package com.ppn.ppn.config.security;

import com.ppn.ppn.constant.CorrelationId;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String correlationId = getCorrelationId(request);
            log.info("start REST with request {} with correlationId {}", request.getRequestURI(), correlationId);
            if (correlationId != null) {
                if (!request.getRequestURI().contains("/login")) {
                    String jwt = getJwtFromRequest(request);
                    if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                        String userName = jwtTokenProvider.getUserNameFromJWT(jwt);
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
                        if (userDetails != null) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            } else {
                correlationId = UUID.randomUUID().toString();
                log.info("No correlationId found in header. Generated: {}", correlationId);
            }
        } catch (Exception ex) {
            log.error("failed on set user authentication", ex);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getCorrelationId(HttpServletRequest request) {
        String currentCorrId = request.getHeader(CorrelationId.CORRELATION_ID_HEADER);
        return currentCorrId;
    }
}
