package lt.viko.eif.pvaiciulis.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lt.viko.eif.pvaiciulis.service.CustomUserDetailsService;
import lt.viko.eif.pvaiciulis.service.JwtService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

/**
 * JwtAuthenticationFilter class is a custom filter that processes incoming requests to authenticate users based on JWT tokens
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Processes the HTTP request to authenticate users based on JWT tokens.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs during the filtering process
     * @throws IOException if an input or output error occurs during the filtering process
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("hello");
        System.out.println(authHeader);
        jwt = authHeader.substring(7);
        userId = jwtService.extractUsername(jwt);
        System.out.println(userId);
        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
            System.out.println("im here 1");
            if(jwtService.isTokenValid(jwt, userDetails)){
                System.out.println("im here 2");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                System.out.println("im here 3");
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                System.out.println("im here 4");
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("im here 5");
            }
        }
        filterChain.doFilter(request, response);
        System.out.println("im here 6");
    }
}