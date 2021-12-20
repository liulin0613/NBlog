package com.nblog.interceptor;

import com.nblog.annotation.PassToken;
import com.nblog.dto.Audience;
import com.nblog.enums.StatusCodeEnum;
import com.nblog.exception.BizException;
import com.nblog.utils.JwtTokenUtil;
import com.nblog.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.nblog.constant.CommonConst.AUTH_HEADER_KEY;
import static com.nblog.constant.CommonConst.TOKEN_PREFIX;

@Configuration
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    private Audience audience;

    @Autowired
    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 忽略带 PassToken 注解的请求, 不做后续 token 认证校验
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            PassToken jwtIgnore = handlerMethod.getMethodAnnotation(PassToken.class);
            if (jwtIgnore != null) {
                return true;
            }
        }

        // 直接放行 OPTIONS 请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 获取请求头 authorization 信息
        final String authHeader = request.getHeader(AUTH_HEADER_KEY);
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(TOKEN_PREFIX)) {
            throw new BizException(StatusCodeEnum.NOT_LOGIN);
        }

        // 获取token
        final String token = authHeader.substring(7);
        if(audience == null){
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            audience = (Audience) factory.getBean("audience");
        }

        // 验证 token 是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
        JwtTokenUtil.parseJWT(token, audience.getBase64Secret());

        return true;
    }
}
