package com.choice.cloud.sysmonitor.config;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * feign上传文件配置
 */
@Configuration
public class FeignMultipartSupportConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;


    /**
     * 支持提交文件的编码格式
     *
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Encoder feignFormEncoder() {
        return new MultiFileSpringFormEncoder(new SpringEncoder(messageConverters));
    }


    public static class MultiFileSpringFormEncoder extends SpringFormEncoder {
        public MultiFileSpringFormEncoder(Encoder delegate) {
            super(delegate);
        }
        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            if (!bodyType.equals(MultipartFile[].class)) {
                super.encode(object, bodyType, template);
                return;
            }
            MultipartFile[] files = (MultipartFile[]) object;
            Map<String, Object> data = singletonMap(files[0].getName(), object);
            super.encode(data, MAP_STRING_WILDCARD, template);
        }
    }

}
