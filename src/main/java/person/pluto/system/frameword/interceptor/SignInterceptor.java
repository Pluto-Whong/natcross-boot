package person.pluto.system.frameword.interceptor;

import lombok.extern.slf4j.Slf4j;
import person.pluto.system.frameword.CommonConstants;
import person.pluto.system.model.enumeration.ResultEnum;
import person.pluto.system.tools.SignTools;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 签名拦截器
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:01:58
 */
@Slf4j
public class SignInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("SignInterceptor request method = [{}] , uri = [{}] from ip = [{}]", request.getMethod(),
                request.getRequestURI(), request.getRemoteAddr());

        Map<String, String> params = new HashMap<>();
        List<Object> fileList = new LinkedList<>();

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            params.put(key, value == null || value.length < 1 ? null : value[0]);
        }

        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            Map<String, List<MultipartFile>> fileMap = multipartHttpServletRequest.getMultiFileMap();
            for (Entry<String, List<MultipartFile>> entry : fileMap.entrySet()) {
                for (MultipartFile entryFile : entry.getValue()) {
                    try {
                        fileList.add(entryFile.getBytes());
                    } catch (IOException e) {
                        returnJson(response, ResultEnum.SIGN_FILE_ERROR.toResultModel().toString());
                        return false;
                    }
                }
            }
        }

        boolean signatureValid = SignTools.isSignatureValid(params, CommonConstants.SERVER_SIGN_KEY,
                CommonConstants.SIGN_MAX_DIFF_MILLIS, fileList);
        if (!signatureValid) {
            returnJson(response, ResultEnum.SIGN_ERROR.toResultModel().toString());
            return false;
        }

        return true;
    }

    /**
     * 写入response作为返回数据
     *
     * @author wangmin1994@qq.com
     * @since 2019-04-29 13:47:10
     * @param response
     * @param json
     */
    private void returnJson(HttpServletResponse response, String json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
            writer.flush();
        } catch (IOException e) {
            log.error("response error", e);
        }
    }

}
