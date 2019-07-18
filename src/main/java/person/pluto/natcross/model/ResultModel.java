package person.pluto.natcross.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * <p>
 * 常规类型的前后端返回model
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 10:45:41
 */
@Data
@Slf4j
public class ResultModel {

    public static ResultModel of(String retCode, String retMsg, Object object) {
        return new ResultModel(retCode, retMsg, object);
    }

    public static ResultModel of(ResultEnum resultEnum, Object data) {
        return new ResultModel(resultEnum.getCode(), resultEnum.getName(), data);
    }

    public static ResultModel of(ResultEnum resultEnum) {
        return new ResultModel(resultEnum.getCode(), resultEnum.getName(), null);
    }

    public static ResultModel ofFail(Object data) {
        return new ResultModel(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getName(), data);
    }

    public static ResultModel ofFail() {
        return new ResultModel(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getName(), null);
    }

    public static ResultModel ofSuccess(Object data) {
        return new ResultModel(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getName(), data);
    }

    public static ResultModel ofSuccess() {
        return new ResultModel(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getName(), null);
    }

    private String retCod;
    private String retMsg;
    private Object data;

    public ResultModel(String retCod, String retMsg, Object data) {
        this.retCod = retCod;
        this.retMsg = retMsg;
        this.data = data;
    }

    /**
     * 反射方式修改值
     *
     * @author wangmin1994@qq.com
     * @since 2019-05-10 14:04:48
     * @param fieldStr
     * @param object
     * @return
     */
    public ResultModel set(String fieldStr, Object object) {
        Field field = null;
        try {
            field = this.getClass().getDeclaredField(fieldStr);
        } catch (NoSuchFieldException | SecurityException e) {
            log.warn("ResultModel get field faild!", e);
            return this;
        }

        field.setAccessible(true);
        try {
            field.set(this, object);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.warn("ResultModel set field faild!", e);
            return this;
        }

        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
