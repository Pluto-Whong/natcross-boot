package person.pluto.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import person.pluto.system.jpaservice.IJpaLoginService;
import person.pluto.system.model.LoginResult;
import person.pluto.system.model.enumeration.ResultEnum;

@Controller
public class HomeController {

    @Autowired
    private IJpaLoginService loginService;

    @RequestMapping({ "/", "/index" })
    public String index() {
        return "/index";
    }

    @RequestMapping("/403")
    public String unauthorizedRole() {
        return "/403";
    }

    @GetMapping(value = "/login")
    public String toLogin() {
        loginService.logout();
        return "/login";
    }

    @PostMapping(value = "/login")
    public String login(Model model, String userName, String password) {
        LoginResult loginResult = loginService.login(userName, password);
        if (loginResult.isLogin()) {
            return "/index";
        } else {
            model.addAttribute("msg", loginResult.getResult());
            model.addAttribute("userName", userName);
            return "/login";
        }
    }

    @PostMapping(value = "/loginForJson")
    @ResponseBody
    public Object loginForJson(String userName, String password) {
        LoginResult loginResult = loginService.login(userName, password);
        if (loginResult.isLogin()) {
            return ResultEnum.SUCCESS.toResultModel();
        } else {
            return ResultEnum.LOGIN_EXCEPTION.toResultModel().setData(loginResult.getResult());
        }
    }

    @RequestMapping("/logout")
    public String logOut() {
        loginService.logout();
        return "/user/login";
    }

    @RequestMapping("/logoutForJson")
    @ResponseBody
    public Object logoutForJson() {
        loginService.logout();
        return ResultEnum.SUCCESS.toResultModel();
    }
}