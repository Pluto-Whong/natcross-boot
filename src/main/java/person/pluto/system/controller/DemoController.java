package person.pluto.system.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/getParam")
    public ResultModel getParamForGet(String username) {
        System.err.println("getParamForGet");
        return ResultModel.ofSuccess(username);
    }

    @RequestMapping("/tencentYunSms")
    public Object tencentYunSms(Map<String, String> param) {
        System.err.println(param);
        return ResultModel.ofSuccess();
    }

}
