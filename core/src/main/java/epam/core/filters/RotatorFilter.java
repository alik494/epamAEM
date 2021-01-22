package epam.core.filters;

import org.apache.sling.engine.EngineConstants;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.*;
import java.io.IOException;

//@Component(service = Filter.class,
//        property = {
//                Constants.SERVICE_DESCRIPTION + "=Demo to filter incoming images",
//                EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
//                Constants.SERVICE_RANKING + ":Integer=-700",
//                "filter.pattern=/content/we-retail/us/.*",
//                "filter.extensions=jpeg",
//                "methods=GET",
//                "filter.order=" + Integer.MIN_VALUE
//
//        })
//@SlingServletFilter(
//        scope = {SlingServletFilterScope.REQUEST}, // REQUEST, INCLUDE, FORWARD, ERROR, COMPONENT (REQUEST, INCLUDE, COMPONENT)
//        pattern = "/content/we-retail/us/.*",
//        extensions = {"jpeg", "jpg"},
//        methods = {"GET"}
//)
public class RotatorFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
