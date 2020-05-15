/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package cn.citms.icw.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置feign、mybatis包名、properties
 *
 * @author Chill
 */
@Configuration
@MapperScan({"cn.citms.icw.**.mapper.**"})
public class IcwConfiguration implements WebMvcConfigurer {

    /**
     * 根路径转发到index首页文件
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /**
     * 静态资源路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/web/");
        registry.addResourceHandler("/TempFiles/ErrorExcel/**")
                .addResourceLocations("file:///" + System.getProperties().getProperty("user.home") + "/TempFiles/ErrorExcel/");
    }

    /**
     * 跨域处理
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("*");
    }

}
