package zoua.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;

import io.seata.rm.datasource.DataSourceProxy;

@Configuration
@EnableConfigurationProperties({MybatisProperties.class})
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean
    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSourceProxy dataSourceProxy,
                                                       MybatisProperties mybatisProperties) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSourceProxy);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] mapperLocaltions = resolver.getResources(mybatisProperties.getMapperLocations()[0]);
            bean.setMapperLocations(mapperLocaltions);

            if (StringUtils.isNotBlank(mybatisProperties.getConfigLocation())) {
                Resource[] resources = resolver.getResources(mybatisProperties.getConfigLocation());
                bean.setConfigLocation(resources[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }
}