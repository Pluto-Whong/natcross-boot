package person.pluto.system.tools;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * 
 * <p>
 * 类拷贝，解放深度克隆
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-06-29 15:29:06
 */
public class CopyPropertiesUtils {

    /**
     * 获取为null的参数，进行忽略
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-29 15:33:35
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptorArray = beanWrapper.getPropertyDescriptors();
        Set<String> nullNames = new HashSet<String>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptorArray) {
            Object sourceValue = beanWrapper.getPropertyValue(propertyDescriptor.getName());
            if (sourceValue == null) {
                nullNames.add(propertyDescriptor.getName());
            }
        }
        String[] result = new String[nullNames.size()];
        return nullNames.toArray(result);
    }

    /**
     * 拷贝属性
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-29 15:33:55
     * @param source
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
}
