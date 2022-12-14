package com.carroll.biz.monitor.analyzer;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author hehongbo
 * @Date 2017-08-14 14:50
 **/
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
        org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.class})
@EnableSwagger2
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = {"com.carroll.biz.monitor.analyzer.repository"},
        repositoryFactoryBeanClass = MongoRepositoryFactoryBean.class)
@ComponentScan(basePackages = {"com.carroll", "com.carroll.biz", "com.xxl"})
@Slf4j
@EnableMongoAuditing
//@EnableEurekaClient
public class AnalyzerApplication {

    @Value("${restTemplateConnTimeout}")
    private int restTemplateConnTimeout;

    public static void main(String[] args) {
        SpringApplication.run(AnalyzerApplication.class, args);

        HystrixPlugins.getInstance().registerCommandExecutionHook(new HystrixCommandExecutionHook() {
            @Override
            public <T> T onEmit(HystrixInvokable<T> commandInstance, T response) {
                if (commandInstance instanceof HystrixCommand) {
                    Throwable exception = ((HystrixCommand) commandInstance).getExecutionException();
                    log.error("", exception);
                }
                return response;
            }
        });
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) template.getRequestFactory();
        factory.setConnectTimeout(restTemplateConnTimeout);
        factory.setReadTimeout(restTemplateConnTimeout);
        return template;
    }

}
