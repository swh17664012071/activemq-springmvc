package com.etoak.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * <beans>
 *   <context:component-scan base-package="com.etoak">
 *     <context:include-filter type="annotation" expression="Controller" />
 *     <context:exclude-filter />
 *   </context:component-scan>
 *   引入db.properties <context:property-placeholder location="classpath:db.properties" />
 *   数据源
 *   SqlSessionFactoryBean
 *   MapperScannerConfigurer
 *   事务管理器
 *   注解事务
 * </beans>
 */
@Configuration
@ComponentScan(basePackages = {"com.etoak"},
    excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = {Controller.class, RestController.class,
                ControllerAdvice.class, EnableWebMvc.class})},
    includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = {Service.class, Repository.class})}
)
@PropertySource(value = "classpath:db.properties") // 引入配置文件
@MapperScan(basePackages = "com.etoak.mapper") // MapperScannerConfigurer
@EnableTransactionManagement // <tx:annotation-driven />
@Import(ActiveMQConfig.class) // 导入一个容器
public class RootConfig {

    @Value("${db.driver}")
    private String driverClassName;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    /** 数据源 */
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(10); // 初始化10个连接
        dataSource.setMaxActive(30); // 最大连接数30
        return dataSource;
    }

    /** SqlSessionFactoryBean */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        // 数据源、typeAliasesPackage、mapperLocations、plugins
        factoryBean.setDataSource(this.dataSource());
        factoryBean.setTypeAliasesPackage("com.etoak.bean");

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath:mapper/*.xml");
            factoryBean.setMapperLocations(resources);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // plugins
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        pageInterceptor.setProperties(properties);
        factoryBean.setPlugins(new Interceptor[]{pageInterceptor});
        return factoryBean;
    }

    /** 事务管理器 */
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

}