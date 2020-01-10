package person.pluto.natcross.enumeration;

/**
 * <p>
 * 端口类型
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 11:39:40
 */
public enum PortTypeEnum {

    //
    NORMAL(0, "普通"),
    //
    HTTPS(10, "HTTPs端口"),
    //
    ;

    private Integer code;
    private String comment;

    private PortTypeEnum(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public static PortTypeEnum getEnumByCode(Integer code) {
        if (code == null) {
            return NORMAL;
        }
        for (PortTypeEnum e : PortTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return NORMAL;
    }

    public Integer getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }

}
