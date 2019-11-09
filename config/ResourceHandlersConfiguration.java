package com.zhihui.dontStarve.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceHandlersConfiguration  implements WebMvcConfigurer {
	@Value("${dontStarve.path.PICTURE}")
	String PICTURE;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        /**
//         * @Description: 对文件的路径进行配置, 创建一个虚拟路径/templates/** ，即只要在<img src="/templates/picName.jpg" />便可以直接引用图片
//         *这是图片的物理路径  "file:/+本地图片的地址"
//         */
//        registry.addResourceHandler("/templates/**").addResourceLocations
//                ("file:/E:/IdeaProjects/gaygserver/src/main/resources/static/");
////        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/static/");
//        super.addResourceHandlers(registry);
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //和页面有关的静态目录都放在项目的static目录下
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //上传的图片在D盘下的OTA目录下，访问路径如：http://localhost:8081/OTA/d3cf0281-bb7f-40e0-ab77-406db95ccf2c.jpg
        //其中OTA表示访问的前缀。"file:D:/OTA/"是文件真实的存储路径
        registry.addResourceHandler("/img/**").addResourceLocations(PICTURE);
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
    }

@Override
public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void addCorsMappings(CorsRegistry arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void addFormatters(FormatterRegistry arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void addInterceptors(InterceptorRegistry arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void addViewControllers(ViewControllerRegistry arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void configureAsyncSupport(AsyncSupportConfigurer arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void configureContentNegotiation(ContentNegotiationConfigurer arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void configureDefaultServletHandling(DefaultServletHandlerConfigurer arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void configureHandlerExceptionResolvers(
		List<HandlerExceptionResolver> arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void configureMessageConverters(List<HttpMessageConverter<?>> arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void configurePathMatch(PathMatchConfigurer arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void configureViewResolvers(ViewResolverRegistry arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void extendMessageConverters(List<HttpMessageConverter<?>> arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public MessageCodesResolver getMessageCodesResolver() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Validator getValidator() {
	// TODO Auto-generated method stub
	return null;
}
}