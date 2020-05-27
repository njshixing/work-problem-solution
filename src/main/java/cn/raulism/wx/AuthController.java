package cn.raulism.wx;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weixin.popular.api.MessageAPI;
import weixin.popular.api.SnsAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.token.Token;
import weixin.popular.bean.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
@RequestMapping("/wx")
public class AuthController {

    String appid = "wx118f440fc3258afe";
    String secret = "f8993f4129f2953d122bac9261249f1d";

    @GetMapping("/checkToken")
    @ResponseBody
    public String auth(@RequestParam Map<String, String> param) {
        System.out.println(JSON.toJSONString(param));
        return param.get("echostr");
    }

    @GetMapping("/auth")
    public String auth(@RequestParam Map<String, String> param, HttpServletRequest request, HttpServletResponse response) {
        String code = param.get("code");
        SnsToken stoken = SnsAPI.oauth2AccessToken(appid, secret, code);
        User user = SnsAPI.userinfo(stoken.getAccess_token(), appid, "zh_CN");
        // 微信登录信息
        request.getSession().setAttribute("user", user);
        // 登录前请求地址
        String uri = param.get("uri");
        // 跳转到登录前
        return "redirect:" + uri;
    }
}
