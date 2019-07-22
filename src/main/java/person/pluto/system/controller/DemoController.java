package person.pluto.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import person.pluto.system.model.ResultModel;

/**
 * <p>
 * demo 供测试用
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-05-10 14:48:00
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/index")
    public Object index() {
        return ResultModel.ofSuccess();
    }

}
