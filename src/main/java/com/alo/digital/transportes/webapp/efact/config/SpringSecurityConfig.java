package com.alo.digital.transportes.webapp.efact.config;

import com.alo.digital.transportes.webapp.efact.service.SpringSecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled =  true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	private SpringSecurityUserService springSecurityService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(springSecurityService).passwordEncoder(passwordEncoder());
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		
		http.headers().frameOptions().sameOrigin();
		
        	http.authorizeRequests()
                	.antMatchers("/login").permitAll()
                	.antMatchers(
                            "/",
                            "/js/_clientes/**",
                            "/css/**",
                            "/font-awesome/**",
                            "/bootstrap/**",
                            "/jquery/**",
                            "/bootstrap-datepicker/**",
                            "/bootstrap-dialog/**",
                            "/bootstrap-table/**",
                            "/webfonts/**",
                            "/images/**",
                            "/webjars/**")
                    .permitAll()
                    .antMatchers("/_clientes/**").anonymous()
                    .antMatchers("/admin/**").hasRole("1")
                    .antMatchers("/js/admin/**").hasRole("1")
                    .antMatchers("/inicio/**").authenticated()
                    .antMatchers("/js/consultas/**").authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/loginerror")
					.defaultSuccessUrl("/inicio")
					.usernameParameter("username")
					.passwordParameter("password")
                    .permitAll()
                .and()
                .logout()
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(false)
                    .deleteCookies("remove")
                    .permitAll();
                //.and()
                //.exceptionHandling().accessDeniedPage("/error/403.html");
                //.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
					
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
	    PasswordEncoder encoder = new BCryptPasswordEncoder();
	    return encoder;
	}
	
	
}
