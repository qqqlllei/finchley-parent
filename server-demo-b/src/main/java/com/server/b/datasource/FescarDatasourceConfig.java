package com.server.b.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fescar.rm.datasource.DataSourceProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by 李雷 on 2019/4/2.
 */
@Configuration
public class FescarDatasourceConfig {


    private final ApplicationContext applicationContext;

    public FescarDatasourceConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(destroyMethod = "close", initMethod = "init")
    public DruidDataSource druidDataSource() throws SQLException {

        Environment environment = applicationContext.getEnvironment();

        String url = environment.getProperty("spring.datasource.url");

        String userName = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(password);
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setInitialSize(0);
        druidDataSource.setMaxActive(180);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(0);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(25200000);
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeout(1800);
        druidDataSource.setLogAbandoned(true);
        druidDataSource.setFilters("mergeStat");
        return druidDataSource;
    }

    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    @Bean("dataSource")
    public DataSource dataSource(DruidDataSource druidDataSource) {
        DataSourceProxy dataSourceProxy = new DataSourceProxy(druidDataSource);
        return dataSourceProxy;
    }

}
