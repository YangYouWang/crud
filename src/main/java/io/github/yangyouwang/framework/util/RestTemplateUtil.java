package io.github.yangyouwang.framework.util;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Description: <br/>
 * date: 2022/8/3 19:30<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
public class RestTemplateUtil {
    /**
     * 实际执行请求的template
     */
    private static RestTemplate restTemplate = new RestTemplate();


    public static String put( Object entity, String url){
        return put(entity,url,String.class);
    }

    public static  <T>T put(Object entity,String url, Class<T> cls){
        logParams(RequestMethod.PUT,url,entity);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Object> request = new HttpEntity<>(entity,headers);
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.PUT, request, cls);
        T result = exchange.getBody();
        return result;
    }

    public static String post(Object entity,String url){
        return post(url,entity,String.class);
    }

    /**
     * 得到 参数的字符串
     * @param entity
     * @return
     */
    private static String getStringParams(Object entity){
        if(entity instanceof String){
            return entity.toString();
        }
        return JSON.toJSONString(entity);
    }

    private static void logParams(RequestMethod method, String url, Object entity){
        String params=getStringParams(entity);
    }
    /**
     * json请求
     */
    public static <T>T post(String url, Object entity, Class<T> cls){
        logParams(RequestMethod.POST,url,entity);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Object> request = new HttpEntity<>(entity,headers);
        return executePost(url,request,cls);
    }

    /**
     * 表单请求
     */
    public static <T>T formPost(String url, MultiValueMap<String, Object> params, Class<T> cls){
        logParams(RequestMethod.POST,url,params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Object> request = new HttpEntity<>(params,headers);
        return executePost(url,request,cls);
    }

    /**
     * post 请求
     */
    private static <T>T executePost(String url, HttpEntity<Object> request, Class<T> cls){
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.POST, request, cls);
        T body = exchange.getBody();
        return body;
    }


    public static  String get(String url){
        return get(url,null,String.class);
    }
    /**
     * get 请求
     */
    public static  <T>T get(String url, Map<String,Object> params, Class<T> cls){
        StringBuilder urlBuiler=new StringBuilder(url);
        if(!url.contains("?")){
            urlBuiler.append("?");
        }
        if(!CollectionUtils.isEmpty(params)){
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                urlBuiler.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String reqUrl = urlBuiler.toString();
        T result = restTemplate.getForObject(reqUrl, cls);
        return result;
    }
}
