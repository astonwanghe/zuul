package com.melot.kk.springcloud.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccessFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        LOGGER.info("AccessFilter, request method:{}, url:{}", request.getMethod(), request.getRequestURL().toString());

        String userIdStr = request.getParameter("userId");

        JSONObject result = new JSONObject();

        if (userIdStr == null) {
            result.put("TagCode", "00000001");

            LOGGER.warn("AccessFilter, check fail, userId is null.");
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseBody(result.toJSONString());

            return null;
        }

        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            result.put("TagCode", "00000002");

            LOGGER.warn("AccessFilter, check fail, token is null.");
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseBody(result.toJSONString());

            return null;
        }

        LOGGER.info("AccessFilter, check success!");
        return null;
    }
}
