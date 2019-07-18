package person.pluto.system.frameword.exception;

import lombok.extern.slf4j.Slf4j;
import person.pluto.system.model.ResultModel;
import person.pluto.system.model.enumeration.ResultEnum;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 全局异常处理函数
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:02:11
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultModel globalExceptionHandler(HttpServletRequest request, HttpServletResponse response,
            Exception exception) {
        log.error("unkown error!", exception);
        return ResultModel.of(ResultEnum.FAIL);
    }
}
