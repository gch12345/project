package Fred.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BlogConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/fonts/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/plugins/editor/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/login")
                .excludePathPatterns("/a/**")
                .excludePathPatterns("/index");
    }
}

