package person.pluto.system.frameword.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import javax.servlet.Filter;

/**
 * <p>
 * web设置类（过滤器等）
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:03:44
 */
@Configuration
public class WebConfig {

    /**
     * 过滤器
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-10 11:06:07
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(getInterfaceFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*"));
        return registrationBean;
    }

    /**
     * 过滤器实体
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-10 11:09:16
     * @return
     */
    @Bean
    public InterfaceFilter getInterfaceFilter() {
        return new InterfaceFilter();
    }

}
