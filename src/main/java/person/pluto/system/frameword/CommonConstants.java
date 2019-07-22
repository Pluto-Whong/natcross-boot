package person.pluto.system.frameword;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 通用参数
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:01:04
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "common.constants")
public class CommonConstants {
    // 实例模块名，主要在多实例时进行区分
    public static String MOUDLE_NAME = null;
    // 本项目的地址（域名）
    public static String LOCAL_ADDRESS = null;

    /**
     * shiro 加密方式
     */
    public static String SHIRO_ALGORITHM_NAME = "md5";
    /**
     * shiro加密散列次数
     */
    public static Integer SHIRO_HASH_ITERATIONS = 1;

    public void setMOUDLE_NAME(String mOUDLE_NAME) {
        MOUDLE_NAME = mOUDLE_NAME;
        log.trace("MOUDLE_NAME[{}]", MOUDLE_NAME);
    }

    public void setLOCAL_ADDRESS(String lOCAL_ADDRESS) {
        LOCAL_ADDRESS = lOCAL_ADDRESS;
        log.trace("LOCAL_ADDRESS[{}]", LOCAL_ADDRESS);
    }

    public void setSHIRO_ALGORITHM_NAME(String sHIRO_ALGORITHM_NAME) {
        SHIRO_ALGORITHM_NAME = sHIRO_ALGORITHM_NAME;
        log.trace("SHIRO_ALGORITHM_NAME[{}]", SHIRO_ALGORITHM_NAME);
    }

    public void setSHIRO_HASH_ITERATIONS(Integer sHIRO_HASH_ITERATIONS) {
        SHIRO_HASH_ITERATIONS = sHIRO_HASH_ITERATIONS;
        log.trace("SHIRO_HASH_ITERATIONS[{}]", SHIRO_HASH_ITERATIONS);
    }

}
