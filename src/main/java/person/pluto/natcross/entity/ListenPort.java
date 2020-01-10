package person.pluto.natcross.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import person.pluto.natcross.enumeration.ListenStatusEnum;
import person.pluto.natcross.enumeration.PortTypeEnum;
import person.pluto.natcross2.serverside.listen.ServerListenThread;

/**
 * <p>
 * 监听模型
 * </p>
 *
 * @author Pluto
 * @since 2019-07-22 13:55:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_listen_port")
public class ListenPort implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("listen_port")
    private Integer listenPort;

    @TableField("port_describe")
    private String portDescribe;

    @TableField("dest_ip")
    private String destIp;

    @TableField("dest_port")
    private Integer destPort;

    @TableField("on_start")
    private Boolean onStart;

    @TableField("port_type")
    private Integer portType;

    @JsonIgnore
    @TableField("cert_path")
    private String certPath;

    @JsonIgnore
    @TableField("cert_password")
    private String certPassword;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modify", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 监听状态
     */
    @JsonIgnore
    @TableField(exist = false)
    private ServerListenThread serverListenThread;

    /**
     * 获取监听状态
     *
     * @author Pluto
     * @since 2019-07-22 14:51:37
     * @return
     */
    public ListenStatusEnum getListenStatus() {
        if (serverListenThread == null) {
            return ListenStatusEnum.STOP;
        }
        if (!serverListenThread.isAlive()) {
            return ListenStatusEnum.WAIT;
        }
        return ListenStatusEnum.RUNNING;
    }

    /**
     * 端口类型
     * 
     * @author Pluto
     * @since 2020-01-10 11:44:41
     * @return
     */
    public PortTypeEnum getPortTypeEnum() {
        return PortTypeEnum.getEnumByCode(this.portType);
    }

    /**
     * 端口类型
     * 
     * @author Pluto
     * @since 2020-01-10 13:50:41
     * @return
     */
    public Integer getPortType() {
        return this.getPortTypeEnum().getCode();
    }

    /**
     * 证书配置状态
     * 
     * @author Pluto
     * @since 2020-01-10 13:50:15
     * @return
     */
    public String getCertStatus() {
        if (StringUtils.isAnyBlank(this.certPath, this.certPassword)) {
            return "默认配置";
        } else {
            return this.getCertPath();
        }
    }

}
