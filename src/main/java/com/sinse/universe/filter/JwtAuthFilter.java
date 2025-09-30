package com.sinse.universe.filter;

import com.sinse.universe.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("jwtAuthFilter 진입");

        String authz = request.getHeader("Authorization");
        log.debug("authz 출력={}", authz);

        // accessToken이 있는 경우만 검증, 없으면 필터를 그냥 거쳐서 Authentication이 없어서 시큐리티가 접근을 막는다.
        if (authz != null && authz.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = authz.substring(7);
            log.debug("token={}", token);
            try {
                Jws<Claims> jws = jwtUtil.validateToken(token);  //검증

                Integer subject = Integer.valueOf(jws.getBody().getSubject());
                String roleName = jws.getBody().get("role", String.class);
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName));

                // 검증 성공 시 security context에 Authentication 저장
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(subject, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("jwt 인증 성공");
            } catch (ExpiredJwtException e) {
                log.warn("JWT expired", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"code\":\"TOKEN_EXPIRED\",\"message\":\"Access token expired\"}");  // 만료된 경우 프론트가 재발급 요청
                return;
            } catch (JwtException e){
                log.warn("Invalid JWT", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"code\":\"TOKEN_INVALID\",\"message\":\"Invalid token\"}");
                return;
            }
        }

        log.debug("jwt 필터 빠져나감");
        filterChain.doFilter(request, response);
    }

    // 특정 경로에 대해 JwtAuthFilter가 동작하지 않도록 설정
    // accessToken이 만료되어 재발급 요청을 보낼때 여전히 accessToken이 존재하므로, 컨트롤러에 진입하기 전에 ExpiredJwtException이 발생해 필터에서 튕겨버림
    // 이것을 막고자 사용
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getMethod().equals("POST")
                && request.getRequestURI().equals("/api/auth/accessToken");
    }
}
