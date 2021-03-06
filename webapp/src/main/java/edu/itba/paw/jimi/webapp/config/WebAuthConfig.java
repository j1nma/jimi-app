package edu.itba.paw.jimi.webapp.config;

import edu.itba.paw.jimi.webapp.auth.CorsFilter;
import edu.itba.paw.jimi.webapp.auth.StatelessAuthenticationFilter;
import edu.itba.paw.jimi.webapp.auth.StatelessLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Base64;

@Configuration
@EnableWebSecurity
@ComponentScan("edu.itba.paw.jimi.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Autowired
	private StatelessLoginSuccessHandler statelessLoginSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler statelessLoginFailureHandler;

	@Autowired
	private StatelessAuthenticationFilter statelessAuthenticationFilter;

	@Autowired
	private CorsFilter corsFilter;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.userDetailsService(userDetailsService).sessionManagement()
				.and()
				.addFilterBefore(corsFilter, ChannelProcessingFilter.class)
				.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
				.and().authorizeRequests()
				.antMatchers("/api/dishes/**").hasRole("ADMIN")
				.antMatchers("/api/admin/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/api/tables/**").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.POST, "/api/tables").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/api/tables/{id:[\\d+]}/name").hasAnyRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/api/tables/{id:[\\d+]}/undoneDishes").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.POST, "/api/tables/{id:[\\d+]}/diners").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.POST, "/api/tables/{id:[\\d+]}/status").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.POST, "/api/tables/{id:[\\d+]}/undoneDishes/{dishId:[\\d+]}/amount").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.DELETE, "/api/tables/{id:[\\d+]}/undoneDishes/{dishId:[\\d+]}").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.DELETE, "/api/tables/{id:[\\d+]}").hasAnyRole("ADMIN")
				.antMatchers("/api/users/**").hasRole("ADMIN")
				.antMatchers("/api/kitchen/**").authenticated()
				.antMatchers("/api/**").authenticated()
				.anyRequest().authenticated()
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.formLogin().usernameParameter("username").passwordParameter("password").loginProcessingUrl("/api/login")
				.successHandler(statelessLoginSuccessHandler)
				.failureHandler(statelessLoginFailureHandler)
				.and()
				.addFilterBefore(statelessAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				"/webapp/static/**",
				"/webapp/assets/**",
				"/webapp/asset-manifest.json",
				"/webapp/favicon.ico",
				"/webapp/index.html",
				"/webapp/manifest.json",
				"/webapp/service-worker.js",
				"/webapp/precache**"
		);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public String tokenSigningKey() {
		return Base64.getEncoder().encodeToString("E3E4B1AFE1B1457AAC1CAF95E8AD5888DF00E6A63E48B9CE2F241B59CAD955D1052415930B3B1ECFBE82BC4A9B4328E50DE23D3A129070DD8D7F2DC78F0F130F".getBytes());
	}

}
