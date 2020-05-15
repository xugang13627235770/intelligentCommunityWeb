package cn.citms.icw.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xiaoleilu.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.constant.CitmsAppConstant;
import org.springblade.common.utils.CommonComponent;
import org.springblade.common.utils.HttpContextUtils;
import org.springblade.common.utils.UserContextHolder;
import org.springblade.common.vo.UserModel;
import org.springblade.common.vo.UserRightViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 全局拦截器，作用所有的微服务
 * pre：路由之前
 * routing：路由之时
 * post： 路由之后
 * error：发送错误调用
 * filterOrder：过滤的顺序
 * shouldFilter：这里可以写逻辑判断，是否要开启过滤
 * run：过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问。
 * <p>
 */
@Slf4j
@Component
class AccessFilter extends ZuulFilter {

    @Autowired
    private CommonComponent commonComponent;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //优先级为0，数字越大，优先级越低
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        //过滤各种POST请求
        if(request.getMethod().equals(RequestMethod.OPTIONS.name())){
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        String accessToken = UserContextHolder.getAuthorization();
        String webtoken = "";
        String userInfo = "";
        String tempAccessToken = "";
        String defaulToken = "";

        HttpServletRequest req = HttpContextUtils.getHttpServletRequest();
        String url = req.getRequestURI();

        HttpServletResponse response = ctx.getResponse();


        if (StringUtils.isNotEmpty(url)) {
            if (url.indexOf("/" + CitmsAppConstant.APPLICATION_MBD_NAME) > -1) {
                if (StringUtils.isNotEmpty(accessToken)) {
                    if (accessToken.contains("Basic ")) {
                        tempAccessToken = accessToken.replace("Basic ", "").trim();
                        webtoken = commonComponent.getRedisData(CitmsAppConstant.WEBTOKEN, tempAccessToken);
                        defaulToken = commonComponent.getRedisData(CitmsAppConstant.DEFAULTTOKEN, tempAccessToken);
                    }
                    if (StringUtils.isEmpty(accessToken)) {
                        try {
                            ctx.setSendZuulResponse(false);
                            ctx.getResponse().setHeader("Content-Type", "text/html;charset=UTF-8");
                            ctx.setResponseBody("{\"status\":401,\"msg\":\"请求未授权！\"}");
                            log.info("请求未授权{}{}>>>>>>请求未授权!");
                            ctx.set("isSuccess", false);
                        } catch (Exception e) {
                            log.info("response io异常");
                            e.printStackTrace();
                        }
                    } else if (accessToken.length() < 10) {
                        try {
                            ctx.setSendZuulResponse(false);
                            ctx.setResponseBody("{\"status\":500,\"msg\":\"Token格式不正确！\"}");
                            log.info("Token格式不正确{}{}>>>>>>Token格式不正确!");
                            ctx.set("isSuccess", false);
                        } catch (Exception e) {
                            log.info("response io异常");
                            e.printStackTrace();
                        }
                    } else if (StringUtils.isNotEmpty(defaulToken) || StringUtils.isNotEmpty(webtoken)) {
                        if (StringUtils.isEmpty(defaulToken)) {
                            defaulToken = webtoken;
                        }
                        log.info("WebToken在线用户信息或默认用户信息为{}{}>>>>>>" + defaulToken);
                        ctx.setSendZuulResponse(true);
                        ctx.setResponseStatusCode(200);
                        ctx.set("isSuccess", true);
                        UserModel userModel = JSONUtil.toBean(defaulToken, UserModel.class);
                        userInfo = commonComponent.getRedisData(CitmsAppConstant.USERINFO, userModel.getUserGUID());
                        UserRightViewModel userRightViewModel = JSONUtil.toBean(userInfo, UserRightViewModel.class);
                        UserContextHolder.setUserInfo(userRightViewModel);

                    } else {
                        try {
                            ctx.setSendZuulResponse(false);
                            ctx.setResponseStatusCode(200);
                            ctx.getResponse().setHeader("Content-Type", "text/html;charset=UTF-8");
                            ctx.setResponseBody("{\"status\":401,\"msg\":\"请求未授权！\"}");
                            log.info("请求未授权{}{}>>>>>>请求未授权!");
                            ctx.set("isSuccess", false);
                        } catch (Exception e) {
                            log.info("response io异常");
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        ctx.setSendZuulResponse(false);
                        ctx.setResponseStatusCode(200);
                        ctx.getResponse().setHeader("Content-Type", "text/html;charset=UTF-8");
                        ctx.setResponseBody("{\"status\":401,\"msg\":\"请求未授权！\"}");
                        log.info("请求未授权{}{}>>>>>>请求未授权!");
                        ctx.set("isSuccess", false);
                    } catch (Exception e) {
                        log.info("response io异常");
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}

