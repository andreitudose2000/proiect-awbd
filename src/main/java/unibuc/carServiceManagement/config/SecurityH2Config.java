package unibuc.carServiceManagement.config;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("h2")
public class SecurityH2Config extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("guest")
                .password(passwordEncoder.encode("12345"))
                .roles("GUEST")
                .build());
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("12345"))
                .roles("GUEST", "ADMIN")
                .build());
        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/parts/**").hasAnyRole("ADMIN")
                .antMatchers("/tasks/**").hasRole("ADMIN")
                .antMatchers("/car-services/*").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/mechanics/**").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/appointments/**").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/clients").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/clients/info/*").hasAnyRole("GUEST", "ADMIN")
                .antMatchers("/clients/new").hasRole("ADMIN")
                .and()
                .formLogin().loginPage("/loginForm")
                .loginProcessingUrl("/authUser")
                .failureUrl("/loginForm?error").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access_denied");
    }




}

