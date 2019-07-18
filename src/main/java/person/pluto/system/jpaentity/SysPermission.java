package person.pluto.system.jpaentity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class SysPermission {
    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Integer permissionId;// 主键.
    @Column(nullable = false)
    private String permissionName;// 名称.
    @Column(columnDefinition = "enum('menu','button')")
    private String resourceType;// 资源类型，[menu|button]
    private String url;// 资源路径.
    private String permission; // 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private Long parentId; // 父编号
    private String parentIds; // 父编号列表
    private Boolean available = Boolean.TRUE;
    // 角色 -- 权限关系：多对多关系;
    @ManyToMany
    @JoinTable(name = "SysRolePermission", joinColumns = { @JoinColumn(name = "permissionId") }, inverseJoinColumns = {
            @JoinColumn(name = "roleId") })
    private List<SysRole> roles;

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }
}
