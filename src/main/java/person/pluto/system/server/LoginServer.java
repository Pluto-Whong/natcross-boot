package person.pluto.system.server;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import person.pluto.system.model.ReturnModel;

@Service
public class LoginServer {

    /**
     * 登录方法
     *
     * @author Pluto
     * @since 2019-07-22 13:32:13
     * @param userName
     * @param password
     * @return
     */
    public ReturnModel login(String userName, String password) {
        if (userName == null || userName.isEmpty()) {
            return ReturnModel.ofFail("用户名为空");
        }

        Subject currentUser = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        try {
            // 传到MyAuthorizingRealm类中的方法进行认证
            currentUser.login(token);
        } catch (UnknownAccountException e) {
            return ReturnModel.ofFail("UnknownAccountException -- > 账号不存在");
        } catch (IncorrectCredentialsException e) {
            return ReturnModel.ofFail("IncorrectCredentialsException -- > 密码不正确");
        } catch (AuthenticationException e) {
            return ReturnModel.ofFail("用户验证失败");
        }

        Session session = currentUser.getSession();
        session.setAttribute("userName", userName);

        return ReturnModel.ofSuccess();
    }

    /**
     * 登出
     *
     * @author Pluto
     * @since 2019-07-22 13:32:19
     */
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
