package person.pluto.natcross.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * <p>
 * 输入流对输出流 直接输出
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-07-05 10:20:33
 */
public class InputToOutputThread extends Thread {

    private boolean isAlive = true;
    private Reader reader;
    private Writer writer;

    private IBelongControl belongControl;

    private InputStream inputStream;
    private OutputStream outputStream;

    public InputToOutputThread(Reader reader, Writer writer, IBelongControl belongControl) {
        this.reader = reader;
        this.writer = writer;
        this.belongControl = belongControl;
    }

    public InputToOutputThread(InputStream inputStream, OutputStream outputStream, IBelongControl belongControl) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.belongControl = belongControl;
    }

    @Override
    public void run() {
        int len = -1;
        byte[] arrayTemp = new byte[1024];
        try {
//            while (isAlive && (len = reader.read(arrayTemp)) > 0) {
//                writer.write(arrayTemp, 0, len);
//                writer.flush();
//            }
            while (isAlive && (len = inputStream.read(arrayTemp)) > 0) {
                outputStream.write(arrayTemp, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (belongControl != null) {
            belongControl.noticeStop();
        }
    }

    public void cancell() {
        isAlive = false;
        try {
            if (reader != null) {
                reader.close();
                reader = null;
            }
            if (writer != null) {
                writer.close();
                writer = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
