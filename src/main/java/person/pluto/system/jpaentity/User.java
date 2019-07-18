package person.pluto.system.jpaentity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String userName; // 登录用户名

    @Column(nullable = false)
    private String name;// 名称（昵称或者真实姓名，根据实际情况定义）

    @Column(nullable = false)
    private String password;

    private String salt;// 加密密码的盐

    private String email;

    private byte state;// 用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredDate;// 过期日期

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;// 创建时间

    @ManyToMany(fetch = FetchType.EAGER) // 立即从数据库中进行加载数据;
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = {
            @JoinColumn(name = "roleId") })
    private List<SysRole> roleList;// 一个用户具有多个角色

    @JsonIgnore
    public List<SysRole> getRoleList() {
        return roleList;
    }

    /**
     * 密码盐. 重新对盐重新进行了定义，用户名+salt，这样就不容易被破解，可以采用多种方式定义加盐
     * 
     * @return
     */
    @JsonIgnore
    public String getCredentialsSalt() {
        return this.userName + this.salt;
    }

}
