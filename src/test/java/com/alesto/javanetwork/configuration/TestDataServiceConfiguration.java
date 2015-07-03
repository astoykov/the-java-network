package com.alesto.javanetwork.configuration;

import com.alesto.javanetwork.repository.FollowsRepository;
import com.alesto.javanetwork.repository.TimelineRepository;
import com.alesto.javanetwork.service.DataExchangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@ComponentScan(basePackages = {"com.alesto.javanetwork.service"})
public class TestDataServiceConfiguration {
    
    /*
     * ======================================================================
     * ----- Fields
     * ======================================================================
     */



    @Bean
    public FollowsRepository followsRepository()
    {
        return Mockito.mock(FollowsRepository.class);
    }

    @Bean
    public TimelineRepository timelineRepository()
    {
        return Mockito.mock(TimelineRepository.class);
    }


}
