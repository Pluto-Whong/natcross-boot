package person.pluto.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Pluto
 * @since 2019-07-18 17:13:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("email")
    private String email;

    @TableField("expired_date")
    private LocalDate expiredDate;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    @TableField("state")
    private Integer state;

    @TableField("user_name")
    private String userName;


}
