package com.chao.springcloud_common.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate getRestTemplate(HttpComponentsClientHttpRequestFactory requestFactory, FastJsonHttpMessageConverter messageConverter){

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while (iterator.hasNext()){
            if(iterator.next().getClass() == StringHttpMessageConverter.class){
                iterator.remove();
            }
        }
        converters.add(messageConverter);
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converters.add(stringHttpMessageConverter);
        return restTemplate;

    }

    // 消息转换器设置
    @Bean(value = "fastJsonHttpMessageConverter")
    public FastJsonHttpMessageConverter getFastConverter(){
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 第一个设置为属性为null的话也打印出来，不设置的话默认是不打印的；第二个设置为字符串为null时输出""而不是null
        // 第三个设置为如果list集合为null输出为[]
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullListAsEmpty);
        fastJsonConfig.setSerializeFilters(new ValueFilterImpl());
        fastJsonConfig.setDateFormat("yy-MM-dd HH:mm:ss");
        // 处理乱码问题
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypeList);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        return fastJsonHttpMessageConverter;
    }

    // 处理BigDecimal小数点问题
    private class ValueFilterImpl implements ValueFilter{
        @Override
        public Object process(Object o, String s, Object o1) {
            if(null != o1){
                if(o1 instanceof BigDecimal){
                   return  ((BigDecimal)o1).setScale(2,BigDecimal.ROUND_DOWN);
                }
            }
            return o1;
        }
    }

    //前端连接后端的连接器配置
    @Bean
    public HttpComponentsClientHttpRequestFactory getRequestFactory(PoolingHttpClientConnectionManager clientConnectionManager){
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(clientConnectionManager).build();
        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectTimeout(3000);//连接池的超时时间
        requestFactory.setConnectionRequestTimeout(2000);//客户端连接服务端的超时时间
        requestFactory.setReadTimeout(2000);// 响应给客户端具体数据的超时时间
        return requestFactory;

    }

    // 连接池配置
    @Bean
    public PoolingHttpClientConnectionManager clientConnectionManager(){
        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(200);// 连接池最大连接数
        clientConnectionManager.setDefaultMaxPerRoute(200);// 单路由最大连接数、、单路由个人认为是单个项目及同一个映射的controller层
        return clientConnectionManager;
    }
}
