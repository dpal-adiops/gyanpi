package com.adiops.apigateway.oauth2.security.auth;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * This class extends WebSecurityConfigurerAdapter and provides usual spring
 * security configuration.Here, we are using bcrypt encoder to encode our
 * passwords.Following configuration basically bootstraps the authorization
 * server and resource server.
 * 
 * @author Deepak Pal
 *
 */
@Configuration
// Enables spring security web security support.
@EnableWebSecurity
//Support to have method level access control such as @PreAuthorize @PostAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource(name = "userService")
	private UserDetailsService userDetailsService;
	
	/**
     * To configure Spring Security, here are some considerations.
     * 1. Spring Security By default, CSRF is enabled, at which point the POST form we submit must have hidden fields to pass CSRF.
     * And in logout, we have to exit the user by POST to / logout. See our login.html and logout.html for details.
     * 2. When rememberMe() is enabled, we must provide rememberMeServices, such as the getRememberMeServices() method below.
     * And we can only configure cookie names, expiration times and other related configurations in TokenBasedRememberMeServices. If we configure cookies at the same time elsewhere, we will report errors.
     * Error examples: xxxx. and (). rememberMe (). rememberMeServices (getRememberMeServices (). rememberMeCookieName ("cookie-name")
     */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
        .authorizeRequests()        	
        	.antMatchers("/css/**", "/js/**", "/fonts/**","/sass/**","/vendors/**","/images/**","/resources/**").permitAll() 
        	.antMatchers("/*").permitAll() 
        	.antMatchers("/admin/**").hasAnyRole("ADMIN")
        	.antMatchers("/api/**").hasAnyRole("ADMIN")
        	.antMatchers("/web/**").hasAnyRole("USER")            
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
        .logout() 
        	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        	.deleteCookies("JSESSIONID")
        	.invalidateHttpSession(true) 
            .permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/403");
		
	
		
		
		
	}

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}
		
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}