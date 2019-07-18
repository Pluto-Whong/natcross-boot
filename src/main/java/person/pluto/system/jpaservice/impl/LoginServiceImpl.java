package person.pluto.system.jpaservice.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import person.pluto.system.jpaservice.ILoginService;
import person.pluto.system.model.LoginResult;

@Service
public class LoginServiceImpl implements ILoginService {
    @Override
    public LoginResult login(String userName, String password) {
        LoginResult loginResult = new LoginResult();
        if (userName == null || userName.isEmpty()) {
            loginResult.setLogin(false);
            loginResult.setResult("用户名为空");
            return loginResult;
        }
        String msg = "";
        Subject currentUser = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        try {
            currentUser.login(token); // 传到MyAuthorizingRealm类中的方法进行认证
            Session session = currentUser.getSession();
            session.setAttribute("userName", userName);
            loginResult.setLogin(true);
            return loginResult;
        } catch (UnknownAccountException e) {
            msg = "UnknownAccountException -- > 账号不存在：";
        } catch (IncorrectCredentialsException e) {
            msg = "IncorrectCredentialsException -- > 密码不正确：";
        } catch (AuthenticationException e) {
            msg = "用户验证失败";
        }

        loginResult.setLogin(false);
        loginResult.setResult(msg);

        return loginResult;
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
