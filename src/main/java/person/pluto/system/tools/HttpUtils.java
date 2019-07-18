package person.pluto.system.tools;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * http工具集
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:17:23
 */
@Slf4j
public class HttpUtils {

    /**
     * 异步http get 等待数据时间60秒
     *
     * @param url
     * @param param
     * @param header
     * @param futureCallback
     * @return
     */
    public static boolean doGetAsyn(String url, Map<String, String> param, Map<String, String> header,
            FutureCallback<HttpResponse> futureCallback) {
        return doGetAsyn(url, param, header, futureCallback, 60000);
    }

    /**
     * 异步http get
     *
     * @param url
     * @param param
     * @param header
     * @param futureCallback
     * @param socketTimeout
     * @return
     */
    public static boolean doGetAsyn(String url, Map<String, String> param, Map<String, String> header,
            FutureCallback<HttpResponse> futureCallback, int socketTimeout) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(500).setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(100).build();

        // 配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors()).setSoKeepAlive(false).build();
        // 设置连接池大小
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            log.error("设置连接池异常", e);
            return false;
        }
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connManager.setMaxTotal(1);
        connManager.setDefaultMaxPerRoute(1);

        CloseableHttpAsyncClient client = HttpAsyncClients.custom().setRedirectStrategy(new LaxRedirectStrategy())
                .setConnectionManager(connManager).setDefaultRequestConfig(requestConfig).build();
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (Entry<String, String> entry : param.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            if (header != null) {
                for (Entry<String, String> entry : header.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }

            // 异步请求
            client.start();
            client.execute(httpGet, futureCallback);
        } catch (URISyntaxException e) {
            log.error("创建uri过程异常", e);
            return false;
        }

        return true;

    }

    /**
     * 异步 http post 默认等待60秒
     *
     * @param url
     * @param param
     * @param header
     * @param futureCallback
     * @return
     */
    public static boolean doPostAsyn(String url, Map<String, String> param, Map<String, String> header,
            FutureCallback<HttpResponse> futureCallback) {
        return doPostAsyn(url, param, header, futureCallback, 60000);
    }

    /**
     * 异步 http post
     *
     * @param url
     * @param param
     * @param header
     * @param futureCallback
     * @param socketTimeout
     * @return
     */
    public static boolean doPostAsyn(String url, Map<String, String> param, Map<String, String> header,
            FutureCallback<HttpResponse> futureCallback, int socketTimeout) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(500).setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(100).build();

        // 配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors()).setSoKeepAlive(false).build();
        // 设置连接池大小
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            log.error("设置连接池异常", e);
            return false;
        }
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connManager.setMaxTotal(1);
        connManager.setDefaultMaxPerRoute(1);

        CloseableHttpAsyncClient client = HttpAsyncClients.custom().setRedirectStrategy(new LaxRedirectStrategy())
                .setConnectionManager(connManager).setDefaultRequestConfig(requestConfig).build();

        try {
            HttpPost httpPost = new HttpPost(url);

            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Entry<String, String> entry : param.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }

            if (header != null) {
                for (Entry<String, String> entry : header.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }

            // 异步请求
            client.start();
            client.execute(httpPost, futureCallback);
        } catch (UnsupportedEncodingException e) {
            log.error("创建uri过程异常", e);
            return false;
        }
        return true;
    }

    /**
     * 同步 http get
     *
     * @param url
     * @param param
     * @param header
     * @return
     */
    public static String doGet(String url, Map<String, String> param, Map<String, String> header) {

        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
        CloseableHttpResponse response = null;

        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (Entry<String, String> entry : param.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            if (header != null) {
                for (Entry<String, String> entry : header.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            } else {
                log.error("请求[{}]异常返回[{}]", httpClient, response);
            }
        } catch (Exception e) {
            log.error("http get 请求时异常", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                log.error("response关闭异常", e);
            }
        }
        return null;
    }

    /**
     * 同步 http post map方式param
     *
     * @param url
     * @param param
     * @param header
     * @return
     */
    public static String doPost(String url, Map<String, String> param, Map<String, String> header) {
        return doPost(url, param, header, Consts.UTF_8);
    }

    /**
     * 同步 http post map方式param
     *
     * @param url
     * @param param
     * @param header
     * @return
     */
    public static String doPost(String url, Map<String, String> param, Map<String, String> header, Charset charset) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
        CloseableHttpResponse response = null;

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Entry<String, String> entry : param.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, charset);
                httpPost.setEntity(entity);
            }
            if (header != null) {
                for (Entry<String, String> entry : header.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }

            // 执行http请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), charset);
            } else {
                log.error("请求[{}]异常返回[{}]", httpClient, response);
            }
        } catch (Exception e) {
            log.error("http post 表单 请求时异常", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("response关闭异常", e);
            }
        }

        return null;
    }

    /**
     * 同步 http post ，自动将object 转换为json
     *
     * @param url
     * @param object
     * @param header
     * @return
     */
    public static String doPostJson(String url, Object object, Map<String, String> header) {
        String json = JSON.toJSONString(object);

        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
        CloseableHttpResponse response = null;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            if (header != null) {
                for (Entry<String, String> entry : header.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }

            // 执行http请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            } else {
                log.error("请求[{}]异常返回[{}]", httpClient, response);
            }
        } catch (Exception e) {
            log.error("http post json 请求时异常", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("response关闭异常", e);
            }
        }
        return null;
    }

    /**
     * put请求 使用file进行
     *
     * @author wangmin1994@qq.com
     * @since 2019-05-24 15:32:28
     * @param url
     * @param file
     * @param header
     * @return
     */
    public static String doPutFile(String url, File file, Map<String, String> header) {
        try {
            return doPutInputStream(url, new FileInputStream(file), header);
        } catch (FileNotFoundException e) {
            log.error("http put file 获取文件流时异常", e);
        }
        return null;
    }

    /**
     * put请求 使用inputStream进行
     *
     * @author wangmin1994@qq.com
     * @since 2019-05-24 15:29:21
     * @param url
     * @param inputStream
     * @param header
     * @return
     */
    public static String doPutInputStream(String url, InputStream inputStream, Map<String, String> header) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
        CloseableHttpResponse response = null;

        try {
            HttpPut httpPut = new HttpPut(url);
            if (header != null) {
                for (Entry<String, String> entry : header.entrySet()) {
                    httpPut.addHeader(entry.getKey(), entry.getValue());
                }
            }

            HttpEntity entity = EntityBuilder.create().setStream(inputStream).build();
            httpPut.setEntity(entity);

            // 执行http请求
            response = httpClient.execute(httpPut);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            } else {
                log.error("请求[{}]异常返回[{}]", httpClient, response);
            }
        } catch (Exception e) {
            log.error("http put 请求时异常", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("response关闭异常", e);
            }
        }
        return null;
    }
}
