package cn.raulism.wx;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import weixin.popular.api.MenuAPI;
import weixin.popular.api.MessageAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.massmessage.MassTextMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.bean.token.Token;
import weixin.popular.bean.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Map;


@RestController
@RequestMapping("/wx")
public class MainController {

    @GetMapping("/createMenu")
    public String createMenu(@RequestParam Map<String, String> param) {
        Token token = TokenAPI.token(WxConstants.APP_ID, WxConstants.APP_SECRET);
        System.err.println(token.getAccess_token());
        String menu = "{\"button\":[{\"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"},{\"name\":\"菜单\",\"sub_button\":[{\"type\":\"view\",\"name\":\"搜索\",\"url\":\"http://www.soso.com/\"},{\"type\":\"view\",\"name\":\"搜\",\"url\":\"http://g5ccvm.natappfree.cc/wx/test\"}]}]}";
        BaseResult result = MenuAPI.menuCreate(token.getAccess_token(), menu);
        return "shixing";
    }

    @GetMapping("/test")
    public String test(HttpServletResponse response, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(JSON.toJSONString(user));
        Message message = new TextMessage(user.getOpenid(), "来自" + user.getCity() + "的朋友,你好," + user.getNickname());
        Token token = TokenAPI.token(WxConstants.APP_ID, WxConstants.APP_SECRET);
        MessageAPI.messageCustomSend(token.getAccess_token(), message);
        return user.getNickname();
    }
}
