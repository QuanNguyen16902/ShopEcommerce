package com.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ecommerce.security.oauth.CustomerOAuth2UserService;
import com.ecommerce.security.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class EcommerceWebSecurityConfig {
	
	@Autowired 
	private CustomerOAuth2UserService oauth2UserService;
	private OAuth2LoginSuccessHandler oauth2LoginHandler;
	
	
	@Autowired
	private void setOAuth2LoginSuccessHandler(OAuth2LoginSuccessHandler oauth2LoginHandler) {
		this.oauth2LoginHandler = oauth2LoginHandler;
	}

	private DatabaseLoginSuccessHandler databaseLoginHandler;
	@Autowired
	private void setDatabaseLoginSuccessHandler(DatabaseLoginSuccessHandler databaseLoginHandler) {
		this.databaseLoginHandler = databaseLoginHandler;
	}

	@Autowired
	public UserDetailsService customerService;

    @Bean
    public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customerService);
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	@Bean
	public SecurityFilterChain customFilterChain(HttpSecurity http) throws Exception {

        http
//        .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                		 .requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll()
               .requestMatchers("/account_details", "/update_account_details", "/orders/**",
            		   "/cart", "/address_book/**","/checkout","/place_order","/process_paypal_order").authenticated()
               						.anyRequest().permitAll())
                .formLogin(formLogin -> formLogin
		                .loginPage("/login")
		                .usernameParameter("email")
		                .successHandler(databaseLoginHandler)
		                .permitAll())
                .oauth2Login(oauthLogin -> oauthLogin 
                			.loginPage("/login")
                			.userInfoEndpoint(user -> user.userService(oauth2UserService))
                			.successHandler(oauth2LoginHandler)
                			)
                .logout(logout -> logout
	        		  .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	                  .logoutUrl("/logout")
 	                  .permitAll())
                .rememberMe(remember -> remember
	        		 .key("1234567890_aBcDeFghjkkaerqwrwqQewrq")
	        		 .tokenValiditySeconds(14 * 24 * 60 * 60))
                .sessionManagement(session -> session
                		.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
         	;
		return http.build();
 
	}
	  @Bean
	  public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring().requestMatchers("/resources/**"); 
	  }
}
