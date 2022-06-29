package com.choice.cloud.sysmonitor.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.choice.cloud.sysmonitor.config.validate.ParamsValidator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Java
 * @date 2020/2/11 9:12
 */
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {

    /**
     * 自定义参数校验器
     */
    @Override
    public Validator getValidator() {
        return createValidator();
    }

    /**
     * 自定义参数校验器
     *
     * @return
     */
    @Bean
    public ParamsValidator createValidator() {
        return new ParamsValidator();
    }

    /**
     * 使用fastjson代替jackson
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //先把JackSon的消息转换器删除.
        //(1)源码分析可知，返回json的过程为:
        //  Controller调用结束后返回一个数据对象，for循环遍历conventers，找到支持application/json的HttpMessageConverter，然后将返回的数据序列化成json。
        //  具体参考org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor的writeWithMessageConverters方法
        //(2)由于是list结构，我们添加的fastjson在最后。因此必须要将jackson的转换器删除，不然会先匹配上jackson，导致没使用fastjson
        for (Iterator<HttpMessageConverter<?>> iterator = converters.iterator(); iterator.hasNext(); ) {
            HttpMessageConverter httpMessageConverter = iterator.next();
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {

                MappingJackson2HttpMessageConverter json = (MappingJackson2HttpMessageConverter) httpMessageConverter;
                ObjectMapper mapper = json.getObjectMapper();
                // 自定义解析格式，自动兼容多种格式
                mapper.setDateFormat(new DateFormat());
                // 设置为东八区的时区
                mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                //忽略null值
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                iterator.remove();
            }
        }

        //自定义fastjson配置
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                // 是否输出值为null的字段,默认为false,我们将它打开
                SerializerFeature.WriteMapNullValue,
                // 将Collection类型字段的字段空值输出为[]
                SerializerFeature.WriteNullListAsEmpty,
                // 将字符串类型字段的空值输出为空字符串
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteDateUseDateFormat,
                // 禁用循环引用
                SerializerFeature.DisableCircularReferenceDetect
        );


        // 添加支持的MediaTypes;不添加时默认为*/*,也就是默认支持全部
        // 但是MappingJackson2HttpMessageConverter里面支持的MediaTypes为application/json
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.setFastJsonConfig(config);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(fastJsonHttpMessageConverter);
    }
}

