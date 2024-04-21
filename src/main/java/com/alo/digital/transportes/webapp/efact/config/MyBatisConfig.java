package com.alo.digital.transportes.webapp.efact.config;

import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

@Configuration
@MapperScan("com.alo.digital.transportes.webapp.efact.mapper")
@PropertySource("classpath:application.properties")
public class MyBatisConfig {
	
	
	@Autowired
    private ResourceLoader resourceLoader;
	
	@Autowired
	private Environment env;


	@Bean
	public DataSource dataSource(){
		
	 	SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(SQLServerDriver.class);
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
		
		return dataSource;
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager(){
		return new DataSourceTransactionManager(dataSource());
	}
	
	@Bean
	public SqlSessionFactoryBean  sqlSessionFactory() throws Exception{
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setTypeAliasesPackage("com.alo.digital.transportes.webapp.efact.beans");
		sessionFactory.setMapperLocations(ResourcePatternUtils.getResourcePatternResolver(resourceLoader).
	            getResources("classpath:mapper/*.xml"));
		
		return sessionFactory;
	}

}
