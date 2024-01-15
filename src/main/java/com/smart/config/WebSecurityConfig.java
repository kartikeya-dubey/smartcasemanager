package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	
	/*
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	// Routing Configuration methods (AuthenticationManagerBuilder is depreciated, using bean based approach now) (source:
	
	// https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter/#:~:text=In%20Spring%20Security%205.7.0,the%20suggested%20alternatives%20going%20forward.)
	// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authorizeRequests%25%29
	// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authorizeHttpRequests(org.springframework.security.config.Customizer)

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		//verifying multiple urls together based on routed url + assigned role INORDER
		
		//http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/user/**").hasRole("USER").requestMatchers("/**").permitAll()).formLogin().and().csrf().disable();
		
		http.csrf().disable()
		.authorizeHttpRequests()
		.requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/**").permitAll()
		.and()
		.formLogin();
		
		return http.build();
	}
	*/
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		
		return new UserDetailsServiceImpl();	
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/**").permitAll())
                .formLogin(login -> login.loginPage("/signin").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index"));
        
        return httpSecurity.build();
	}

}
