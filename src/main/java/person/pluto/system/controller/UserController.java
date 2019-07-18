package person.pluto.system.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/userList")
    @RequiresPermissions("user:view") // 权限管理;
    public String userList() {
        return "/user/userList";
    }

}
