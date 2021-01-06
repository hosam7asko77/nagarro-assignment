package com.nagarro.assignment.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * 
 */
@Configuration
@EnableSwagger2
public class AssignmentConfig {
    @Bean
    public DataSource createDataSource() throws Exception {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        
     
        ds.setJdbcUrl("jdbc:ucanaccess://C:/Users/hosam7asko/Desktop/my assaigment/accountsdb.accdb;showSchema=true");
        ds.setDriverClass("net.ucanaccess.jdbc.UcanaccessDriver");
        
        return ds;
        
    }
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
    	return new BCryptPasswordEncoder();
    } 
	@Bean
	public Docket retApi() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.nagarro.assignment.controller"))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(apiInfo());
	}

	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
		Contact contact=new Contact(
				"Hosam IT", "www.hosamit.com", "hosam7asko1993@gmail.com");
		return new ApiInfo("Spring Boot Rest Api",
				"This for desc ",
				"v1.0",
				"URL", 
				contact.toString(), 
				"Apache License Version 2.0", 
				"http//www.apache.org/licenses/LICENSE-2.0");
	}
    

}
