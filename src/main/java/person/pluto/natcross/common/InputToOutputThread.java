package person.pluto.natcross.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 输入流对输出流 直接输出
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-07-05 10:20:33
 */
@Slf4j
public class InputToOutputThread extends Thread {

    private boolean isAlive = true;

    private IBelongControl belongControl;

    private InputStream inputStream;
    private OutputStream outputStream;

    public InputToOutputThread(InputStream inputStream, OutputStream outputStream, IBelongControl belongControl) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.belongControl = belongControl;
    }

    @Override
    public void run() {
        int len = -1;
        byte[] arrayTemp = new byte[NatcrossConstants.STREAM_CACHE_SIZE];
        try {
            while (isAlive && (len = inputStream.read(arrayTemp)) > 0) {
                outputStream.write(arrayTemp, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.debug("one InputToOutputThread closed");
        }

        if (belongControl != null) {
            belongControl.noticeStop();
        }
    }

    public void cancell() {
        isAlive = false;
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.debug("one InputToOutputThread inputStream closed exception,but won't to fixed");
            }
            inputStream = null;
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.debug("one InputToOutputThread outputStream closed exception,but won't to fixed");
            }
            outputStream = null;
        }
    }

}
