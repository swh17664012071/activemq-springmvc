package com.etoak.init;

import com.etoak.config.MvcConfig;
import com.etoak.config.RootConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * 相当于web.xml
 */
public class MvcInitializer extends
        AbstractAnnotationConfigDispatcherServletInitializer {

    /** Spring Root容器 */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfig.class };
    }

    /** Spring MVC容器 */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { MvcConfig.class };
    }

    /** DispatcherServlet的拦截路径 */
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    /** 配置过滤器 */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter =
                new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        return new Filter[] {encodingFilter};
    }
}
