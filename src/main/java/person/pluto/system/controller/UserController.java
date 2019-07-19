package person.pluto.system.controller;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import person.pluto.system.entity.User;
import person.pluto.system.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/ulist")
    @RequiresPermissions("user:view") // 权限管理;
    public String ulist() {
        return "/user/userList";
    }

    @RequestMapping("getUserList")
    @ResponseBody
    public Object getUserList(Page<User> page, User user, Map<String, Object> map) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(user);
        IPage<User> page2 = userService.page(page, queryWrapper);

        return page2;
    }

}
