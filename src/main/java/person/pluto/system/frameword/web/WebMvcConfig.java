package person.pluto.system.frameword.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

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

    public static final List<String> INTERCEPTOR_EXCLUDE_LIST = Arrays.asList("/demo/**");

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 需要告知系统，这是要被当成静态文件的！
        // 第一个方法设置访问路径的匹配方式（该方式有一大缺点，controller的路径不得为*.*的格式），第二个方法设置资源路径（file可以用以进行扩展，例如上传的图片等）
        registry.addResourceHandler("**/*.*").addResourceLocations("classpath:/resources/", "classpath:/static/",
                "classpath:/public/", "file:./static-extend/");
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

}
