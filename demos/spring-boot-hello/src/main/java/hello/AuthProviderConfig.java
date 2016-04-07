package hello;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider;
//import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient;

@Configuration
@EnableWebSecurity
public class AuthProviderConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/", "/home").permitAll()
			.antMatchers("/auth").hasRole("USER")
			.anyRequest().authenticated()
			.and()
			.httpBasic()
//			.and()
//			.formLogin()
//			.loginPage("/login").permitAll()
			.and()
			.logout()
			.logoutUrl("/logout")
//			.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // GET not recommended!
			.logoutSuccessUrl("/")
			.permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(kerberosAuthenticationProvider());
		auth.inMemoryAuthentication().withUser("test").password("test").roles("USER");
	}

//	@Bean
//	public KerberosAuthenticationProvider kerberosAuthenticationProvider() {
//		KerberosAuthenticationProvider provider =
//			new KerberosAuthenticationProvider();
//		SunJaasKerberosClient client = new SunJaasKerberosClient();
//		client.setDebug(true);
//		provider.setKerberosClient(client);
//		provider.setUserDetailsService(dummyUserDetailsService());
//		return provider;
//	}

	@Bean
	public UserDetailsService dummyUserDetailsService() {
		return username -> new User(username, "passwd", Collections.emptyList());
	}
}