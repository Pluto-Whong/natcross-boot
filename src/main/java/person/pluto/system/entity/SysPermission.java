package person.pluto.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("sys_permission")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "permission_id", type = IdType.AUTO)
    private Integer permissionId;

    @TableField("available")
    private Boolean available;

    @TableField("parent_id")
    private Long parentId;

    @TableField("parent_ids")
    private String parentIds;

    @TableField("permission")
    private String permission;

    @TableField("permission_name")
    private String permissionName;

    @TableField("resource_type")
    private String resourceType;

    @TableField("url")
    private String url;


}
