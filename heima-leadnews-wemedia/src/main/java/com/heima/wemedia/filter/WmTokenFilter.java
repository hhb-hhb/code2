package com.heima.wemedia.filter;

import com.heima.model.media.pojos.WmUser;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
import com.heima.utils.threadlocal.WmThreadLocalUtils;
import groovy.util.logging.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@WebFilter(filterName = "WmTokenFilter", urlPatterns = "/*")
@Log4j2
public class WmTokenFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String userId = servletRequest.getHeader("userId");
        if (userId != null) {
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            WmThreadLocalUtils.setUser(wmUser);
        }
        chain.doFilter(request,response);
    }
}
