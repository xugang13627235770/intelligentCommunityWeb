package cn.citms.icw.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 全局日期格式化，不在需要@JsonFormat,除非有特殊的格式需求；
 * 支持 Date 类型和 LocalDateTime 类型并存
 * @author cyh
 */
@Configuration
public class LocalDateTimeSerializerConfig {

    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }

}
