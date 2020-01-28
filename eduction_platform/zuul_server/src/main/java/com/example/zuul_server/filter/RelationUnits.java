package com.example.zuul_server.filter;

import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 封装操作Http对象中关联ID的工具类
 */
@Component
public class RelationUnits {

    /*存储sessionId对应的请求关联值*/
    @Autowired
    ConcurrentHashMap<String,String> concurrentHashMap;

    /*获取关联id*/
    public String getRelationId(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        if (requestContext.getRequest().getHeader("CORRELATION_ID") != null){
            return requestContext.getRequest().getHeader("CORRELATION_ID");
        }else {
            if (concurrentHashMap.get(requestContext.getRequest().getSession().getId())!=null){
                return concurrentHashMap.get(requestContext.getRequest().getSession().getId());
            }
            return requestContext.getZuulRequestHeaders().get("CORRELATION_ID");
        }
    }

    /*设置关联id*/
    public void setRelationId(String CORRELATION_ID){
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.addZuulRequestHeader("CORRELATION_ID",CORRELATION_ID);
        concurrentHashMap.put(requestContext.getRequest().getSession().getId(),CORRELATION_ID);
    }
}
