package com.example.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 拦截器

/**
 * @author dsh
 */
public class JWTInterceptors implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 直接使用 request 参数 → 正确
            Map<String,Object> map = new HashMap<>();
            // 获取请求头中令牌
            String token = request.getHeader("token");
            if (token == null){
                map.put("state",false);
                map.put("msg","请先登录！");
                // 将map以json的形式响应到前台  map --> json  (jackson)
                String json = new ObjectMapper().writeValueAsString(map);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(json);
                return false;
            }
            try {
                // 验证令牌
               JWTUtils.verify(token);
                // 放行请求
                return true;
            } catch (SignatureVerificationException e) {
                e.printStackTrace();
                map.put("msg","无效签名！");
            }catch (TokenExpiredException e){
                e.printStackTrace();
                map.put("msg","token过期");
            }catch (AlgorithmMismatchException e){
                e.printStackTrace();
                map.put("msg","算法不一致");
            }catch (Exception e){
                e.printStackTrace();
                map.put("msg","token无效！");
            }
            map.put("state",false);  // 设置状态
            // 将map以json的形式响应到前台  map --> json  (jackson)
            String json = new ObjectMapper().writeValueAsString(map);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(json);
            return false;
        }
}
