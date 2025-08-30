package com.gohul.springSecurity.pastSecurityConfig;


import com.gohul.springSecurity.config.CustomCORSConfig;
import com.gohul.springSecurity.filter.CustomLoggingAfterFilter;
import com.gohul.springSecurity.filter.ReadCSRFTokenFilter;
import com.gohul.springSecurity.filter.RequestValidationFilter;
import com.gohul.springSecurity.sucurityExcep.CustomAccessDeniedHandler;
import com.gohul.springSecurity.sucurityExcep.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    private final CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler =
            new CsrfTokenRequestAttributeHandler();

//    @Bean
    public SecurityFilterChain doSecurityConfig(HttpSecurity http) throws Exception {
        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession")
                            .maximumSessions(3).expiredUrl("/maxSessionReached")).
                requiresChannel( rcc -> rcc.anyRequest().requiresInsecure()). //for only http
                authorizeHttpRequests((req) ->
//                req.requestMatchers("/accounts/myAccount").hasAuthority("VIEWACCOUNT")
//                        .requestMatchers("/balance/myBalance").hasAnyAuthority("VIEWBALANCE","VIEWCARD")
//                        .requestMatchers( "/cards/myCard").hasAuthority("VIEWCARD")
//                        .requestMatchers("/loans/myLoans").hasAnyAuthority("VIEWLOAN","VIEWACCOUNT")
//                        .requestMatchers("/customers/customer").authenticated()
//                        .anyRequest().permitAll()
                req.requestMatchers("/accounts/myAccount").hasAnyRole("USER","ADMIN","MANAGER")
                        .requestMatchers("/balance/myBalance").hasRole("USER")
                        .requestMatchers( "/cards/myCard").hasRole("USER")
                        .requestMatchers("/loans/myLoans").hasRole("USER")
                        .requestMatchers("/customers/customer").authenticated()
                        .anyRequest().permitAll()

        );
        http.securityContext( scConfig -> scConfig.requireExplicitSave(false));
        http.sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        http.csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/contacts/contact","/customers/register"));
        http.addFilterAfter(new ReadCSRFTokenFilter() , BasicAuthenticationFilter.class);
        http.addFilterBefore(new RequestValidationFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new CustomLoggingAfterFilter(), BasicAuthenticationFilter.class);
        http.cors(corsConfig -> corsConfig.configurationSource(new CustomCORSConfig()));
        http.headers(AbstractHttpConfigurer::disable);
        http.formLogin(Customizer.withDefaults());
//        http.httpBasic(Customizer.withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();

    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails admin = User.withUsername("kprahul").password("{bcrypt}$2y$10$j4vpuBDmLN0Mfz1X.1MbpeV9jbAT4alpZOUcNlLpfsxsvPQwkzuS2").authorities("admin").build();
//        UserDetails user = User.withUsername("kpgohul").password("{bcrypt}$2y$10$KSz0rL0.Zv2oZdNXhRDzKukb8hU56R2PeayWvaQXhjamLgIce.TOu").authorities("user").build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }

//    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public CompromisedPasswordChecker compromisedPasswordChecker()
//    {
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }


}
