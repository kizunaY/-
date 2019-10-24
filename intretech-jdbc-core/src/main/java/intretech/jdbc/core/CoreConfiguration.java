package intretech.jdbc.core;

import com.alibaba.druid.pool.DruidDataSource;
import intretech.jdbc.core.properties.PropertiesEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableConfigurationProperties(PropertiesEntity.class)
public class CoreConfiguration {

    @Bean
    @ConditionalOnProperty(value = "enable.intretech.auto",matchIfMissing = true)
    public DruidDataSource dataSource(PropertiesEntity propertiesEntity){
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setDriverClassName(propertiesEntity.getDriverClassName());
        dataSource.setPassword(propertiesEntity.getPassword());
        dataSource.setUsername(propertiesEntity.getUsername());
        dataSource.setUrl(propertiesEntity.getUrl());
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DruidDataSource dataSource){
        System.out.println("执行自定义内容");
        return new JdbcTemplate(dataSource);
    }
}
