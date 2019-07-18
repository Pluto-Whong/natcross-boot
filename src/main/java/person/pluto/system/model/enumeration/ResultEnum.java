package person.pluto.system.model.enumeration;

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

    // 1___流程通用问题
    // 参数错误
    PARAM_FAIL("1001", "参数错误"),
    // 用户无操作权限 No_authority
    NO_AUTHORITY("1004", "无操作权限"),

    // 21___充值问题
    // 不支持的充值渠道
    CHARGE_CHANNEL_NO_SUPPORT("2101", "不支持该渠道"),
    // 充值金额过低
    CHARGE_VALUE_LOWWER("2102", "充值金额过低"),
    // 充值金额过高
    CHARGE_VALUE_HIGHER("2103", "充值金额过高"),
    // 无该充值订单
    CHARGE_MISS_REOCRD("2104", "无该充值订单"),

    // 支付宝订单异常
    CHARGE_ALIPAY_FORM_EXCEPTION("2121", "生成ALIPAY表单异常"),
    // 微信订单异常
    CHARGE_WECHAT_FORM_EXCEPTION("2122", "生成WECHAT表单异常"),

    // 充值记录更新失败
    CHARGE_RECORD_UPDATE_FAIL("2201", "充值记录早已被更改"),
    // 充值记录更新失败
    CHARGE_ACCOUNT_ADD_FAIL("2202", "账户余额增加失败"),

    // 8___检查系别
    // 密码不符合设定
    PASSWORD_ILLEGAL("8001", "密码不合法"),
    // 注册用户名过长
    NAME_ILLEGAL("8002", "用户名非法"),
    // 手机号非法
    MOBILE_ILLEGAL("8003", "手机号非法"),

    // 95__注册系别
    // 注册时异常
    REGIS_EXCEPTION("9500", "信息异常"),
    // 用户已存在
    REGIS_USER_HAS("9501", "用户已存在"),

    // 96__登录系别
    // 登录异常
    LOGIN_EXCEPTION("9600", "登录时发生异常"),
    // 不是理想的登录名，及不知道怎么搜索
    LOGIN_NAME_INVALID("9601", "登录名有误"),
    // 不存在该用户
    LOGIN_USER_MISS("9602", "不存在该用户"),
    // 密码错误
    LOGIN_PASSWORD_INVALID("9603", "密码错误"),
    // 其他地点登录
    LOGIN_OTHER_PLACE("9604", "在其他地点进行了登录"),

    // 97__请求时用户问题
    // 用户不存在
    USER_NO_HAS("9700", "用户不存在"),
    // 在其他地点登录，或刷新过token
    USER_RENEVAL("9701", "TOKEN已变更"),

    // 98__token异常
    // 有的信息不存在，建议更新token
    TOKEN_MISTAKE("9800", "token有误"),
    // token过期
    TOKEN_OVERDUE("9801", "token过期"),
    // 校验失败、不符合对象等
    TOKEN_INVALID("9802", "token无效"),
    // 未知错误
    TOKEN_EXCEPTION("9899", "token解析异常"),

    // 数据操作异常
    DATA_OPR_FAIL("9998","数据操作异常"),
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
