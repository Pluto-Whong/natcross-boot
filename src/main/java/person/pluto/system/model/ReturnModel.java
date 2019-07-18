package person.pluto.system.model;

import lombok.Data;

/**
 * <p>
 * boolean类型返回model
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 10:46:06
 */
@Data
public class ReturnModel {
    private boolean success;
    private Object data;

    private ReturnModel(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public static ReturnModel of(boolean success, Object data) {
        return new ReturnModel(success, data);
    }

    public static ReturnModel ofSuccess(Object data) {
        return new ReturnModel(true, data);
    }

    public static ReturnModel ofFail(Object data) {
        return new ReturnModel(false, data);
    }
}
