package person.pluto.natcross.model;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 前后端返回码
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 10:59:53
 */
public enum ResultEnum {
    // 成功
    SUCCESS("1000", "成功"),
    // 未知错误
    FAIL("9999", "未知错误");

    private String code;
    private String name;

    ResultEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ResultEnum getEnumByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (ResultEnum e : ResultEnum.values()) {
            if (StringUtils.equals(code, e.code)) {
                return e;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
