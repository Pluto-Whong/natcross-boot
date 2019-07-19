package person.pluto.system.tools;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 通用性工具集
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:17:57
 */
public final class Tools {

    /**
     * 去除list中的重复元素
     *
     * @param array
     * @return
     */
    public static <T> List<T> removeRepeat(List<T> array) {
        if (array == null) {
            return null;
        }
        Set<T> set = new HashSet<>();
        List<T> newList = new ArrayList<>();
        for (T model : array) {
            if (set.add(model)) {
                newList.add(model);
            }
        }
        return newList;
    }

    /**
     * 输入流转输出流
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-17 16:01:41
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void inputToOutput(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] temp = new byte[4096];
        int len = -1;
        while ((len = inputStream.read(temp)) != -1) {
            outputStream.write(temp, 0, len);
        }
        outputStream.flush();
    }

    /**
     * unicode转中文
     * 
     * @param str
     * @return
     * @author yutao
     * @date 2017年1月24日上午10:33:25
     */
    public static String unicodeToString(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

}
