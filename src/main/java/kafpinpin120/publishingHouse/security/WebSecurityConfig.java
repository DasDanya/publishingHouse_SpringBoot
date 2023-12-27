package kafpinpin120.publishingHouse.security;

import kafpinpin120.publishingHouse.security.jwt.AuthEntryPointJwt;
import kafpinpin120.publishingHouse.security.jwt.AuthTokenFilter;
import kafpinpin120.publishingHouse.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;
    private final UserService userService;

    public WebSecurityConfig(AuthEntryPointJwt unauthorizedHandler, UserService userService) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.userService = userService;
    }


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
            http.csrf(csrf->csrf.disable())
                    .exceptionHandling(exception-> exception.authenticationEntryPoint(unauthorizedHandler))
                    .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth->
                            auth.requestMatchers("/api/auth/**").permitAll()
                                    .requestMatchers("/api/users/**").hasAnyAuthority("ADMINISTRATOR")
                                    .requestMatchers("/api/typeProducts/add/**", "/api/typeProducts/update/**", "/api/typeProducts/delete/**").hasAnyAuthority("ADMINISTRATOR")
                                    .requestMatchers("/api/products/add/**", "/api/products/update/**", "/api/products/delete/**").hasAnyAuthority("CUSTOMER")
                                    .requestMatchers("/api/printingHouses/**").hasAnyAuthority("ADMINISTRATOR")
                                    .requestMatchers("/api/materials/add/**", "/api/materials/update/**", "/api/materials/delete/**").hasAnyAuthority("ADMINISTRATOR")
                                    .requestMatchers("/api/employees/**").hasAnyAuthority("ADMINISTRATOR")
                                    .requestMatchers("/api/bookings/add/**", "/api/bookings/delete/**").hasAnyAuthority("CUSTOMER")
                                    .anyRequest()
                                    .authenticated());

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
