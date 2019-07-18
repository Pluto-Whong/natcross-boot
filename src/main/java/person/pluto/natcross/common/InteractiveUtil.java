package person.pluto.natcross.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;

import person.pluto.natcross.model.InteractiveModel;

/**
 * 
 * <p>
 * 交互方法工具
 * </p>
 *
 * @author Pluto
 * @since 2019-07-17 09:14:36
 */
public class InteractiveUtil {

    public static final Charset STRING_CHARSET = Charset.forName("UTF-8");

    /**
     * 发送
     *
     * @author Pluto
     * @since 2019-07-17 09:12:25
     * @param outputStream
     * @param byteArray
     * @throws IOException
     */
    public static void send(OutputStream outputStream, InteractiveModel interactiveModel) throws IOException {
        String jsonString = interactiveModel.toJSONString();
        byte[] bytes = jsonString.getBytes(STRING_CHARSET);
        outputStream.write(bytes.length);
        outputStream.write(bytes);
        outputStream.flush();
    }

    /**
     * 接收
     *
     * @author Pluto
     * @since 2019-07-17 09:14:11
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static InteractiveModel recv(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        byte[] b = new byte[read];
        inputStream.read(b, 0, read);

        String string = new String(b, STRING_CHARSET);
        InteractiveModel parseObject = JSON.parseObject(string, InteractiveModel.class);

        return parseObject;
    }

}
