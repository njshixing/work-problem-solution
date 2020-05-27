package cn.raulism.wx;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import weixin.popular.bean.user.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "authFilter", urlPatterns = "/wx/test/*")
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.err.println("AuthorizationFilter init~");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 从session取user，判断是否已经登录
        User user = (User) request.getSession().getAttribute("user");
        if (null == user || null == user.getNickname()) {
            // 未登录，跳转授权页面
            String uri = ((HttpServletRequest) req).getRequestURI();
            System.out.println("uri=" + uri);
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WxConstants.APP_ID
                    + "&redirect_uri=" + WxConstants.DOMAIN + "/wx/auth?uri="
                    + uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
            System.out.println(url);
            response.sendRedirect(url);
        } else {
            System.err.println("user:" + ToStringBuilder.reflectionToString(user));
            // 检查用户是否已注册
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

}
