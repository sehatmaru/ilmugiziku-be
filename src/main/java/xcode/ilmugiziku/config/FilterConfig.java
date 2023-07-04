package xcode.ilmugiziku.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<WebFilter> jwtFilter() {
        FilterRegistrationBean<WebFilter> filter= new FilterRegistrationBean<>();
        filter.setFilter(new WebFilter());
        filter.addUrlPatterns("/api/*");

        return filter;
    }
}
