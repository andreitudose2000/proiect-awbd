package unibuc.clinicmngmnt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

public class MvcConfiguration implements WebMvcConfigurer {
    @Bean(name="simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver errorHandler () {
        SimpleMappingExceptionResolver s =
                new SimpleMappingExceptionResolver();

//        //exception to view name mapping
//        Properties p = new Properties();
//        p.setProperty(NullPointerException.class.getName(), "default_exception");
//        s.setExceptionMappings(p);
//
//        //mapping status code with view response.
//        s.addStatusCode("default_exception", 400);

        //setting default error view
        s.setDefaultErrorView("defaultException");
        //setting default status code
        s.setDefaultStatusCode(400);

        return s;
    }

}
