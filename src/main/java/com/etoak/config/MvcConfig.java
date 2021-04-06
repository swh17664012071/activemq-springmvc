package com.etoak.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * <beans>
 *   <context:component-scan base-package="com.etoak">
 *     <context:include-filter type="annotation" expression="Controller" />
 *     <context:exclude-filter />
 *   </context:component-scan>
 *   <mvc:annotation-driven></mvc:annotation-driven>
 *   <mvc:default-servlet-handler />
 *
 *   <bean class="SpringResourceTemplateResolver"></bean>
 *   <bean class="SpringTemplateEngine"></bean>
 *   <bean class="ThymeleafViewRsolver"></bean>
 * </beans>
 */
@Configuration
@ComponentScan(basePackages = {"com.etoak"},
includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,
    classes = {Controller.class, RestController.class,
                ControllerAdvice.class, EnableWebMvc.class})},
excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,
    classes = {Service.class, Repository.class})}
)
@EnableWebMvc // <mvc:annotation-driven />
public class MvcConfig implements WebMvcConfigurer {

    /** <mvc:default-servlet-handler /> */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /** SpringResourceTemplateResolver */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new
                SpringResourceTemplateResolver();

        // prefix、suffix、templateMode、characterEncoding、cacheable
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(
        @Qualifier("templateResolver") SpringResourceTemplateResolver templateResolver) {

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(
            @Qualifier("templateEngine") SpringTemplateEngine templateEngine) {

        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

}