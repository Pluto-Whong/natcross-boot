package person.pluto.system.frameword.shiro;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import person.pluto.system.entity.UserInfo;
import person.pluto.system.service.IUserInfoService;

public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private IUserInfoService userInfoService;

    /**
     * 权限信息，包括角色以及权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo user = (UserInfo) principals.getPrimaryPrincipal();

        authorizationInfo.addRole(user.getUserName());
        // 没用，只是做注入，日后要抄的时候方便理解
        authorizationInfo.addStringPermission("*");
        return authorizationInfo;
    }

    /**
     * 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfo::getUserName, userName);
        UserInfo userInfo = userInfoService.getOne(queryWrapper);

        if (userInfo == null) {
            return null;
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                // 这里传入的是user对象，比对的是用户名，直接传入用户名也没错，但是在授权部分就需要自己重新从数据库里取权限
                userInfo,
                // 密码
                userInfo.getPassword(),
                // salt
                ByteSource.Util.bytes(userInfo.getPasswordSalt()),
                // realm name
                userInfo.getNickName());

        return authenticationInfo;
    }

}
