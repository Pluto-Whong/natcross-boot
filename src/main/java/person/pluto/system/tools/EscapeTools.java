package person.pluto.system.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.StringEscapeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * html标签过滤工具
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-06-27 15:10:22
 */
@Slf4j
public class EscapeTools {
    /**
     * 过滤所有字段
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-27 15:08:35
     * @param object
     */
    public static void escapeHtml4All(Object object) {
        escapeHtml4IgnoreSet(object, null);
    }

    /**
     * 过滤html标签，指定字段
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-27 14:45:09
     * @param object
     * @param names
     */
    public static void escapeHtml4Aggregation(Object object, Object... names) {
        for (Object name : names) {
            escapeHtml4One(object, name);
        }
    }

    /**
     * 忽略指定字段
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-27 15:08:54
     * @param object
     * @param names
     */
    public static void escapeHtml4Ignore(Object object, Object... names) {
        Set<Object> nameSet = new HashSet<>();
        for (Object name : names) {
            nameSet.add(name);
        }
        escapeHtml4IgnoreSet(object, nameSet);
    }

    /**
     * 忽略执行字段set方式
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-27 15:09:07
     * @param object
     * @param nameSet
     */
    public static void escapeHtml4IgnoreSet(Object object, Set<Object> nameSet) {
        if (object instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) object;
            Set<?> keySet = map.keySet();
            for (Object key : keySet) {
                if (nameSet != null && nameSet.contains(key)) {
                    continue;
                }
                escapeHtml4One(object, key);
            }
        } else {
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (nameSet != null && nameSet.contains(field.getName())) {
                    continue;
                }
                escapeHtml4One(object, field.getName());
            }
        }
    }

    /**
     * 处理过滤指定字段
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-27 15:09:17
     * @param object
     * @param name
     */
    public static void escapeHtml4One(Object object, Object name) {
        if (object == null || name == null) {
            return;
        }
        try {
            if (object instanceof Map) {
                Class<?> clazz = object.getClass();
                Method getMethod = clazz.getMethod("get", Object.class);
                Object value = getMethod.invoke(object, name);
                if (value instanceof String) {
                    value = StringEscapeUtils.escapeHtml4((String) value);
                    Method putMethod = clazz.getMethod("put", Object.class, Object.class);
                    putMethod.invoke(object, name, value);
                }
            } else {
                if (!(name instanceof String)) {
                    return;
                }
                Class<?> clazz = object.getClass();
                Field field = clazz.getDeclaredField((String) name);
                field.setAccessible(true);
                Object value = field.get(object);
                if (value instanceof String) {
                    value = StringEscapeUtils.escapeHtml4((String) value);
                    field.set(object, value);
                }
            }
        } catch (Exception e) {
            log.error("field escape error", e);
        }
    }
}
