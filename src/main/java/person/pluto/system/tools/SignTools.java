package person.pluto.system.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 签名工具类
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-05-20 18:04:43
 */
@Slf4j
public class SignTools {

    public static final DateTimeFormatter DATETIMEMILLIS = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMddHHmmss").appendValue(ChronoField.MILLI_OF_SECOND, 3).toFormatter();
    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_NONCE_STR = "nonce_str";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_CONTENT_MD5 = "contentMd5";

    /**
     * 针对单文件封装的快速调用
     *
     * @author wangmin1994@qq.com
     * @since 2019-07-02 13:55:55
     * @param data
     * @param signKey
     * @param maxDiffTime
     * @param fileObject
     * @return
     */
    public static boolean isSignatureValid(final Map<String, String> data, String signKey, Long maxDiffTime,
            Object fileObject) {
        return isSignatureValid(data, signKey, maxDiffTime, Arrays.asList(fileObject));
    }

    /**
     * 验签，最全面的验证，包含时间戳验证、常规参数签名验证、多文件contentMd5验证
     *
     * @author wangmin1994@qq.com
     * @since 2019-07-02 13:53:16
     * @param data
     * @param signKey
     * @param maxDiffTime
     * @param objectList
     * @return
     */
    public static boolean isSignatureValid(final Map<String, String> data, String signKey, Long maxDiffTime,
            List<Object> objectList) {
        if (!isTimeOutValid(data.get(FIELD_TIMESTAMP), maxDiffTime)) {
            return false;
        }
        if (!isSignatureValid(data, signKey)) {
            return false;
        }

        if (StringUtils.isBlank(data.get(FIELD_CONTENT_MD5)) && (objectList == null || objectList.size() < 1)) {
            return true;
        }

        String contentMd5 = data.get(FIELD_CONTENT_MD5);
        String multipleObjectContentMD5 = getMultipleObjectContentMD5(objectList);
        return StringUtils.equals(contentMd5, multipleObjectContentMD5);
    }

    /**
     * 验签，并验证时间戳的有效性
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-18 09:31:26
     * @param data
     * @param signKey
     * @param maxDiffTime
     * @return
     */
    public static boolean isSignatureTimeOutValid(final Map<String, String> data, String signKey, Long maxDiffTime) {
        if (!isTimeOutValid(data.get(FIELD_TIMESTAMP), maxDiffTime)) {
            return false;
        }
        return isSignatureValid(data, signKey);
    }

    /**
     * 检查是否超时
     *
     * @author wangmin1994@qq.com
     * @since 2019-07-02 13:47:35
     * @param timestamp
     * @param maxDiffTime
     * @return
     */
    public static boolean isTimeOutValid(String timestamp, Long maxDiffTime) {
        LocalDateTime sendTime = null;
        try {
            sendTime = getDateTimeMillisByString(timestamp);
        } catch (Exception e) {
            log.warn("验签时，timestamp字段转换时间异常，获取的值为[{}]", timestamp);
            return false;
        }

        long timeDifferenceForNow = getTimeDifferenceForNow(sendTime);
        if (timeDifferenceForNow > maxDiffTime) {
            log.debug("验签时，时间间隔过长 {} > {} (ms)", timeDifferenceForNow, maxDiffTime);
            return false;
        }
        return true;
    }

    /**
     * 验签
     *
     * @author wangmin1994@qq.com
     * @since 2019-05-20 18:17:38
     * @param data
     * @param signKey
     * @return
     */
    public static boolean isSignatureValid(final Map<String, String> data, String signKey) {
        if (!data.containsKey(FIELD_SIGN)) {
            return false;
        }
        String sign = data.get(FIELD_SIGN);
        return StringUtils.equalsIgnoreCase(generateSignature(data, signKey), sign);
    }

    /**
     * 对data填充必备设定字段，带文件，针对单文件进行的快速封装方法
     *
     * @author wangmin1994@qq.com
     * @since 2019-07-02 14:01:51
     * @param data
     * @param signKey
     * @param fileObject
     * @return
     */
    public static Map<String, String> fullSignature(Map<String, String> data, String signKey, Object fileObject) {
        return fullSignature(data, signKey, Arrays.asList(fileObject));
    }

    /**
     * 对data填充必备设定字段，带文件
     *
     * @author wangmin1994@qq.com
     * @since 2019-07-02 13:40:44
     * @param data
     * @param signKey
     * @param objectList
     * @return
     */
    public static Map<String, String> fullSignature(Map<String, String> data, String signKey, List<Object> objectList) {
        if (objectList != null && objectList.size() > 0) {
            data.put(FIELD_CONTENT_MD5, getMultipleObjectContentMD5(objectList));
        }
        return fullSignature(data, signKey);
    }

    /**
     * 对data填充必备设定字段
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-18 09:16:43
     * @param data
     * @param signKey
     * @return
     */
    public static Map<String, String> fullSignature(Map<String, String> data, String signKey) {
        data.put(FIELD_TIMESTAMP, getDateTimeMillisToString(LocalDateTime.now()));
        data.put(FIELD_NONCE_STR, RandomStringUtils.randomAlphanumeric(8, 16));
        data.put(FIELD_SIGN, generateSignature(data, signKey));
        return data;
    }

    /**
     * 获取签名
     *
     * @author wangmin1994@qq.com
     * @since 2019-05-20 18:11:53
     * @param data
     * @param signKey
     * @return
     */
    public static String generateSignature(final Map<String, String> data, String signKey) {
        Set<String> paramKeySet = data.keySet();
        String[] paramKeyArray = paramKeySet.toArray(new String[paramKeySet.size()]);
        Arrays.sort(paramKeyArray);

        StringBuilder stringBuilder = new StringBuilder();
        String paramValue = null;

        for (String paramKey : paramKeyArray) {
            if (FIELD_SIGN.equals(paramKey)) {
                continue;
            }
            paramValue = data.get(paramKey);
            // 参数值为空，则不参与签名
            if (StringUtils.isNotBlank(paramValue)) {
                stringBuilder.append(paramKey).append("=").append(paramValue.trim()).append("&");
            }
        }
        stringBuilder.append("key=").append(signKey);
        return DigestUtils.md5Hex(stringBuilder.toString());
    }

    /**
     * yyyyMMddHHmmssSSS
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:10:03
     * @param dateTime
     * @return
     */
    public static String getDateTimeMillisToString(LocalDateTime dateTime) {
        return dateTime.format(DATETIMEMILLIS);
    }

    /**
     * yyyyMMddHHmmssSSS
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:07:40
     * @param value
     * @return
     */
    public static LocalDateTime getDateTimeMillisByString(String value) {
        return LocalDateTime.parse(value, DATETIMEMILLIS);
    }

    /**
     * 发送时间与当前时间的差异（ms）
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-18 09:08:35
     * @param sendTime
     * @return
     */
    public static long getTimeDifferenceForNow(LocalDateTime sendTime) {
        return getTimeDifference(LocalDateTime.now(), sendTime);
    }

    /**
     * 获取两个时间的差异绝对值（ms）
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-18 09:05:43
     * @param firstTime
     * @param secondTime
     * @return
     */
    public static long getTimeDifference(LocalDateTime firstTime, LocalDateTime secondTime) {
        return Math.abs(Duration.between(firstTime, secondTime).toMillis());
    }

    /**
     * 获取多个文件集合的Content-MD5
     * 
     * 计算每个文件的Content-MD5，按ASCII码进行升序拼接再对String拼接串获取Content-MD5
     * 
     * @author wangmin1994@qq.com
     * @since 2019-07-02 11:54:35
     * @param objectList
     * @return
     */
    public static String getMultipleObjectContentMD5(List<Object> objectList) {
        if (objectList == null || objectList.size() < 1) {
            return null;
        }

        List<String> contentMd5List = new LinkedList<>();

        for (Object object : objectList) {
            String contentMD5 = null;

            if (object instanceof String) {
                contentMD5 = SignTools.getContentMD5((String) object);
            } else if (object instanceof File) {
                contentMD5 = SignTools.getContentMD5((File) object);
            } else if (object instanceof InputStream) {
                contentMD5 = SignTools.getContentMD5((InputStream) object);
            } else if (object instanceof byte[]) {
                contentMD5 = SignTools.getContentMD5((byte[]) object);
            }

            contentMd5List.add(contentMD5);
        }
        return getStringListContentMd5(contentMd5List);
    }

    /**
     * 获取字符串拼接串的content-md5
     *
     * @author wangmin1994@qq.com
     * @since 2019-07-02 11:56:31
     * @param stringList
     * @return
     */
    public static String getStringListContentMd5(List<String> stringList) {
        if (stringList == null || stringList.size() < 1) {
            return null;
        }

        // 不要对进入的list造成影响
        List<String> destList = Arrays.asList(new String[stringList.size()]);
        Collections.copy(destList, stringList);

        Collections.sort(destList);

        StringBuilder stringBuilder = new StringBuilder();
        for (String string : destList) {
            stringBuilder.append(string);
        }

        return stringBuilder.toString();

        // tmder，为了配合以前指定的单文件签名策略，这里不能再进行md5
//        MessageDigest md5Digest = DigestUtils.getMd5Digest();
//
//        for (String string : destList) {
//            DigestUtils.updateDigest(md5Digest, string);
//        }
//        return Base64.encodeBase64String(md5Digest.digest());
    }

    /**
     * 计算文件的Content-MD5
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-17 15:51:02
     * @param byteArray
     * @return
     */
    public static String getContentMD5(final byte[] byteArray) {
        return Base64.encodeBase64String(DigestUtils.md5(byteArray));
    }

    /**
     * 计算文件的Content-MD5
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-05 14:53:50
     * @param inputStream
     * @return
     */
    public static String getContentMD5(InputStream inputStream) {
        try {
            return Base64.encodeBase64String(DigestUtils.md5(inputStream));
        } catch (IOException e) {
            log.warn("获取文件Content-MD5时出现异常", e);
        }
        return null;
    }

    /**
     * 计算文件的Content-MD5
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-05 14:54:04
     * @param file
     * @return
     */
    public static String getContentMD5(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return getContentMD5(fileInputStream);
        } catch (IOException e) {
            log.warn("获取文件Content-MD5时出现异常", e);
        }
        return null;
    }

    /**
     * 计算文件的Content-MD5
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-05 14:54:09
     * @param filePath
     * @return
     */
    public static String getContentMD5(String filePath) {
        return getContentMD5(new File(filePath));
    }

}
