package cn.citms.icw;

import org.frameworkset.elasticsearch.boot.ElasticSearchBoot;
import org.springblade.common.constant.CitmsAppConstant;
import org.springblade.core.cloud.feign.EnableBladeFeign;
import org.springblade.core.launch.BladeApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 服务启动器
 */
@SpringCloudApplication
@EnableZuulProxy
@EnableScheduling
@EnableBladeFeign(basePackages = { "cn.citms"})
@ComponentScan(basePackages = {"cn.citms","org.springblade.common.utils"})
public class IcwApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =   BladeApplication.run(CitmsAppConstant.APPLICATION_MBD_NAME, IcwApplication.class, args);
        String profile = context.getEnvironment().getProperty("spring.profiles.active");
        ElasticSearchBoot.boot("application-" + profile + ".yml");
    }

}
