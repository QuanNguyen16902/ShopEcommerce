package com.ecommerce.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	 @Autowired
	    private UserDetailsService userService;

//	@Bean
//	public UserDetailsService userDetailsService() {
//		return new EcommerceUserDetailsService();
//	};
    
    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService);
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
//  @Autowired
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth  .userDetailsService(userDetailsService)
//        .passwordEncoder(passwordEncoder());
//	    }
	
//   @Bean
//	    public AuthenticationFailureHandler authenticationFailureHandler() {
//	        return (request, response, exception) -> {
//	            response.sendRedirect("/error/500");
//	        };
//	    }
//   @Bean
//   public AccessDeniedHandler accessDeniedHandler() {
//       return (request, response, accessDeniedException) -> {
//           response.sendRedirect("/error/403");
//       };
//   }
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
//        .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll()
                        .requestMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
                        .requestMatchers("/users/**", "settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
                        .requestMatchers("/categories/**","/brands/**").hasAnyAuthority("Admin", "Editor")
                        
                        .requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
                        
                        .requestMatchers("/products/edit/**", "/products/save", "/products/check_unique")
                        			.hasAnyAuthority("Admin", "Editor", "Salesperson")
                        			
                        .requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
                        			.hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                        			
                        .requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
                        .requestMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
                        .requestMatchers("/customers/**", "/orders/**", "/get_shipping_cost", "/reports/**").hasAnyAuthority("Admin", "Salesperson")
                        
                        .requestMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
                        
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .permitAll())
                .logout(logout -> logout
                		  .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                          .logoutUrl("/logout")
//                          .logoutSuccessUrl("/loginForm?logout")
                          .permitAll())
                 .rememberMe(remember -> remember
                		 .key("asdfghjkkaerqwrwqQewrq_1234567890")
                		 .tokenValiditySeconds(7 * 24 * 60 * 60))
//                 .exceptionHandling( e -> e.accessDeniedPage("/error"));
                 ;
        http.headers(header -> header.frameOptions(f -> f.sameOrigin()));
		return http.build();
 
	}
//	  @Bean
//	  public WebSecurityCustomizer webSecurityCustomizer() {
//	    return (web) -> web.ignoring().requestMatchers("/resources/**"); 
//	  }
}
