package person.pluto.natcross.server;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import person.pluto.natcross.entity.ListenPort;
import person.pluto.natcross.model.CertModel;
import person.pluto.system.common.SystemFormat;

/**
 * <p>
 * 文件服务
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 13:01:28
 */
@Service
public class FileServer {

    @Autowired
    private CertModel certModel;

    /**
     * 保存证书文件
     * 
     * @author Pluto
     * @since 2020-01-10 13:24:51
     * @param certFile
     * @param listenPort
     * @throws Exception
     */
    public String saveCertFile(MultipartFile certFile, ListenPort listenPort) throws Exception {
        String listenCertFilename = SystemFormat.getListenCertFilename(certFile.getOriginalFilename(), listenPort);
        String formatCertPath = certModel.formatCertPath(listenCertFilename);
        certFile.transferTo(new File(formatCertPath));
        return listenCertFilename;
    }

}
