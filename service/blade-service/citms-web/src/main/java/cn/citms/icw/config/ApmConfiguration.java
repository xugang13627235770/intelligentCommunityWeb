package cn.citms.icw.config;

import co.elastic.apm.attach.ElasticApmAttacher;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RefreshScope
public class ApmConfiguration {


    @Value("${elasticApm.serviceName}")
    private String serviceName;

    @Value("${elasticApm.applicationPackages}")
    private String applicationPackages;

    @Value("${elasticApm.serverUrl}")
    private String serverUrl;

    @Value("${elasticApm.logLevel}")
    private String logLevel;

    //elastic APM主要是自动监控HTTP和SQL，对于非用户访问接口，或者非DB访问的任务，我们是否也能够监控呢
    @Bean
    public void elasticApmAttacher() {
        Map<String, String> conf = Maps.newHashMap();
        conf.put("service_name", serviceName);
        conf.put("application_packages", applicationPackages);
        conf.put("server_url", serverUrl);
        conf.put("log_level", logLevel);
        ElasticApmAttacher.attach(conf);
    }

}
