package person.pluto.system.frameword.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import person.pluto.system.frameword.interceptor.SignInterceptor;

/**
 * <p>
 * webMVC设置类（拦截器类）
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:04:18
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("**/*.*").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry.addMapping("/**");
        corsRegistration.allowedOrigins("*");
        corsRegistration.allowedMethods("POST, GET, OPTIONS, DELETE, PUT");
        corsRegistration.allowedHeaders("Content-Type, x-requested-with, X-Custom-Header, Authorization, token");
        corsRegistration.allowCredentials(true);
        corsRegistration.maxAge(3600L);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getSignInterceptor()).addPathPatterns("/**/projectSign/**");
    }

    @Bean
    public SignInterceptor getSignInterceptor() {
        return new SignInterceptor();
    }

}
