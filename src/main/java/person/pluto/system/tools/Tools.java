package person.pluto.system.tools;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
@Slf4j
public final class Tools {

    /**
     * 将前端获取到的string类型data转换为执行的class
     *
     * @param urlStr
     * @param toClass
     * @return
     */
    public static <T> T getParam(String urlStr, Class<T> toClass) {
        log.trace("param is [{}]", urlStr);
        // 问：为何不直接使用java.net.URLDecode？
        // 答：jquery中的urlencode规则与java.net.URLDecode规则不同，导致转码、译码不理想，发现的问题是"+"不被转码，而java.net.URLDecode却会转成空格，org.apache.tomcat.util.buf.UDecoder是比较统一的一种
        urlStr = UDecoder.URLDecode(urlStr);
        if (!urlStr.endsWith("}")) {
            int endIndex = urlStr.lastIndexOf("}");
            urlStr = urlStr.substring(0, endIndex + 1);
        }
        log.trace("param is decode [{}]", urlStr);
        return JSON.parseObject(urlStr, toClass);
    }

    /**
     * (阻塞)时间锁 延时时间 time 到期时长 time2 开始时间 instant
     */
    public static boolean timeLock(long time, long time2, Instant instant) {

        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return !instant.plusMillis(time2).isBefore(Instant.now());
    }

    /**
     * 时间阻塞
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-18 08:54:37
     * @param time    阻塞时间
     * @param timeOut 到期时间
     * @return
     */
    public static boolean timeLock(long time, LocalDateTime timeOut) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return timeOut.compareTo(LocalDateTime.now()) >= 0;
    }

    /**
     * 将emoji表情替换成*
     *
     * @param source 原始数据
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {
        if (StringUtils.isNotBlank(source)) {
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
        } else {
            return source;
        }
    }

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
     * 获取本地IP
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-17 16:01:49
     * @return
     */
    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
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
