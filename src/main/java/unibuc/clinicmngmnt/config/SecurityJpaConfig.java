package unibuc.clinicmngmnt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@Profile("mysql")
public class SecurityJpaConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/parts/**").hasAnyRole("ADMIN")
                .antMatchers("/tasks/**").hasRole("ADMIN")
                .antMatchers("/clinics/*").hasAnyRole("GUEST","ADMIN")
                .antMatchers("/doctors/**").hasAnyRole("GUEST","ADMIN")
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