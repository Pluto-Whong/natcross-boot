package person.pluto.system.frameword.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 
 * <p>
 * 过滤器
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-06-10 10:45:52
 */
@Slf4j
public class InterfaceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new CustomeizedRequest((HttpServletRequest) request), response);
    }

    /**
     * 
     * <p>
     * 修改request返回数据
     * </p>
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-10 10:57:44
     */
    private class CustomeizedRequest extends HttpServletRequestWrapper {

        public CustomeizedRequest(HttpServletRequest request) {
            super(request);
            log.trace("customeized request is created for InterfaceFilter");
        }

    }

}
