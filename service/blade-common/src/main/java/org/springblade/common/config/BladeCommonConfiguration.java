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
package org.springblade.common.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 公共封装包配置类
 *
 * @author Chill
 */
@Configuration
@AllArgsConstructor
public class BladeCommonConfiguration {

    /**
     * 初始化公共Http请求对象
     * @return
     */
    @Bean
    RestTemplate restTemplate(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        // 初始化restTemplate对象
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        // 增加UTF-8字符集
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        HttpMessageConverter<?> converterTarget = null;
        for (HttpMessageConverter<?> item : converterList) {
            if (item.getClass() == StringHttpMessageConverter.class) {
                converterTarget = item;
                break;
            }
        }
        if (converterTarget != null) {
            converterList.remove(converterTarget);
        }
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converterList.add(converter);
        return restTemplate;
    }
}
