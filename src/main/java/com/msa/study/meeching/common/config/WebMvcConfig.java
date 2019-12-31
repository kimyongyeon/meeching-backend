package com.msa.study.meeching.common.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
        viewControllerRegistry.addViewController("/").setViewName("/html/main/index.html");
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setCharacterEncoding("UTF-8"); // forcing UTF-8
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("classpath:/templates/");	// HTML 파일 위치
        templateResolver.setSuffix(".html");					// HTML 확장명 사용
        templateResolver.setTemplateMode(TemplateMode.HTML);	// HTML5 값은 비권장 됨
        templateResolver.setCacheable(false);					// 캐시 사용 안함
        return templateResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")			// 리소스와 매칭될 url
                .addResourceLocations("classpath:/static/");		// 리소스 위치
        // .setCachePeriod(60);							// 캐시 지속 시간
        //.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic());

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/");
    }

    /**
     * core에서 설정함.
     * @return
     */
    @Bean
    @Deprecated
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*")
                        .allowedHeaders("*");
            }
        };
    }
}
