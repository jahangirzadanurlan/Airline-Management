package com.example.bookingms.auth;

import com.example.commonsecurity.auth.SecurityHelper;
import com.example.commonsecurity.auth.services.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final SecurityHelper securityHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        if (securityHelper.servletPathIsAuth(request) || !securityHelper.authHeaderIsValid(authHeader)){
            filterChain.doFilter(request,response);
            return;
        }
        String jwt=authHeader.substring(7);
        List<String> roles = jwtService.extractRoles(jwt);
        String username=jwtService.extractUsername(jwt);

        if (roles != null){
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,null,getAuthorities(roles)
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
