package site.hclub.hyndai.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"site.hclub.hyndai.*.mapper"})
@PropertySource("classpath:application.yml")
public class DataConfig {

    @Value("${driver-class-name}")
    private String driverClassName;
    @Value("${jdbc-url}")
    private String jdbcUrl;
    @Value("${user-name}")
    private String userName;
    @Value("${password}")
    private String password;
    
    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(userName);
        config.setPassword(password);
        return config;
    }

    @Bean
    public DataSource dataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setTypeAliasesPackage("site.hclub.hyndai");
        return sessionFactoryBean.getObject();
    }
}
