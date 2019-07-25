package person.pluto.system.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import person.pluto.system.entity.UserInfo;
import person.pluto.system.model.ReturnModel;
import person.pluto.system.model.enumeration.ResultEnum;
import person.pluto.system.server.LoginServer;

@Controller
public class HomeController {

    @Autowired
    private LoginServer loginServer;

    @RequestMapping({ "/", "/index" })
    public String index(Model model) {
        Subject subject = SecurityUtils.getSubject();
        UserInfo userInfo = (UserInfo) subject.getPrincipal();
        model.addAttribute("userName", userInfo.getUserName());
        model.addAttribute("nickName", userInfo.getNickName());
        return "index";
    }

    @RequestMapping("/403")
    public String unauthorizedRole() {
        return "403";
    }

    @GetMapping(value = "/login")
    public String toLogin() {
        loginServer.logout();
        return "login";
    }

    @PostMapping(value = "/login")
    public String login(Model model, String userName, String password) {
        ReturnModel loginResult = loginServer.login(userName, password);
        if (loginResult.isSuccess()) {
            return "redirect:/index";
        } else {
            model.addAttribute("msg", loginResult.getData());
            model.addAttribute("userName", userName);
            return "login";
        }
    }

    @PostMapping(value = "/loginForJson")
    @ResponseBody
    public Object loginForJson(String userName, String password) {
        ReturnModel loginResult = loginServer.login(userName, password);
        if (loginResult.isSuccess()) {
            return ResultEnum.SUCCESS.toResultModel();
        } else {
            return ResultEnum.LOGIN_EXCEPTION.toResultModel().setData(loginResult.getData());
        }
    }

    @RequestMapping("/logout")
    public String logOut() {
        loginServer.logout();
        return "user/login";
    }

    @RequestMapping("/logoutForJson")
    @ResponseBody
    public Object logoutForJson() {
        loginServer.logout();
        return ResultEnum.SUCCESS.toResultModel();
    }
}