package com.example.demo;

import java.io.File;
import java.io.IOException;
import javax.sql.DataSource;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import com.example.config.ApplicationConfigReader;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import utility.database.ConnectionBean;
import utility.database.DBConnectionFile;

@EnableRabbit
@SpringBootApplication
@EnableSwagger2
public class MsgqApplication extends SpringBootServletInitializer implements RabbitListenerConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(MsgqApplication.class, args);
	}

	@Autowired
	private ApplicationConfigReader applicationConfig;

	public ApplicationConfigReader getApplicationConfig() {
		return applicationConfig;
	}

	public void setApplicationConfig(ApplicationConfigReader applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MsgqApplication.class);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
				.paths(PathSelectors.any()).build();
	}

	/* This bean is to read the properties file configs */
	@Bean
	public ApplicationConfigReader applicationConfig() {
		return new ApplicationConfigReader();
	}

	/* Creating a bean for the Message queue Exchange */
	@Bean
	public TopicExchange getApp1Exchange() {
		return new TopicExchange(getApplicationConfig().getApp1Exchange());
	}

	/* Creating a bean for the Message queue */
	@Bean
	public Queue getApp1Queue() {
		return new Queue(getApplicationConfig().getApp1Queue());
	}

	/* Binding between Exchange and Queue using routing key */
	@Bean
	public Binding declareBindingApp1() {
		return BindingBuilder.bind(getApp1Queue()).to(getApp1Exchange())
				.with(getApplicationConfig().getApp1RoutingKey());
	}

	/*
	 * Creating a bean for the Message queue Exchange
	 * 
	 * @Bean public TopicExchange getApp2Exchange() { return new
	 * TopicExchange(getApplicationConfig().getApp2Exchange()); }
	 * 
	 * Creating a bean for the Message queue
	 * 
	 * @Bean public Queue getApp2Queue() { return new
	 * Queue(getApplicationConfig().getApp2Queue()); }
	 * 
	 * Binding between Exchange and Queue using routing key
	 * 
	 * @Bean public Binding declareBindingApp2() { return
	 * BindingBuilder.bind(getApp2Queue()).to(getApp2Exchange()).with(
	 * getApplicationConfig().getApp2RoutingKey()); }
	 */

	/* Bean for rabbitTemplate */
	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
		return new MappingJackson2MessageConverter();
	}

	@Bean
	public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
		DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
		factory.setMessageConverter(consumerJackson2MessageConverter());
		return factory;
	}

	@Override
	public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
		registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
	}

	/*
	 * @Bean
	 * 
	 * @Primary public DataSource dataSource() throws SQLException, DBException,
	 * IOException, InvalidKeyException, NoSuchAlgorithmException,
	 * NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException { //
	 * ConnectionBean con = SecuredConnect.createConnection("DBCommunication",
	 * true); File fileEncrypted = new ClassPathResource("dbconfig.cfg").getFile();
	 * File EncryptKey = new ClassPathResource("dbkey.cfg").getFile();
	 * DBConnectionFile fil = new
	 * DBConnectionFile(fileEncrypted,EncryptKey,"DBagmcore");
	 * ArrayList<ConnectionBean> s = fil.getList(); for (int i = 0; i < s.size();
	 * i++) { System.out.println(s.get(i).getUserName());
	 * System.out.println(s.get(i).getPassword()); } utility.database.SecuredFile
	 * secure = new utility.database.SecuredFile();
	 * 
	 * String con1 = secure.getDecryptedConfigFile(fileEncrypted, EncryptKey);
	 * System.out.println(">>>"+con1); String urlPostgres =
	 * "jdbc:postgresql://"+con.getHostName()+":"+con.getPortNo()+"/"+con.
	 * getDatabaseName(); //System.out.println(con.getUrlPatternStr()); DataSource
	 * data = DataSourceBuilder.create().username(con.getUserName()).password(con.
	 * getPassword()) .url(urlPostgres) //.driverClassName("org.postgresql.Driver")
	 * .build(); return data;
	 * 
	 * }
	 */

	@Bean
	@Primary
	public DataSource dataSource() throws IOException {
		File fileEncrypted = new ClassPathResource("dbconfig.cfg").getFile();
		File EncryptKey = new ClassPathResource("dbkey.cfg").getFile();
//		File fileEncrypted = new File("/c/tools/dbconfig.cfg");
//		File EncryptKey = new File("/c/tools/dbkey.cfg");
		DBConnectionFile fileDB = new DBConnectionFile(fileEncrypted, EncryptKey, "DBCommunication");
		System.out.println();
		ConnectionBean con = fileDB.getConnectionBean();
		String urlPostgres = "jdbc:postgresql://" + con.getHostName() + ":" + con.getPortNo() + "/"
				+ con.getDatabaseName();
		System.out.println(urlPostgres);
		DataSource data = DataSourceBuilder.create().username(con.getUserName()).password(con.getPassword())
				.url(urlPostgres).build();
		return data;

	}

	/*
	 * @Autowired private JdbcTemplate jdbcTemplate;
	 */

}
