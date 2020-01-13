package com.example.zuul_server.filter;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

/**
 * 封装操作Http对象中关联ID的工具类
 */
@Component
public class RelationUnits {

    /*获取关联id*/
    public String getRelationId(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        if (requestContext.getRequest().getHeader("CORRELATION_ID") != null){
            return requestContext.getRequest().getHeader("CORRELATION_ID");
        }else {
            return requestContext.getZuulRequestHeaders().get("CORRELATION_ID");
        }
    }

    /*设置关联id*/
    public void setRelationId(String CORRELATION_ID){
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.addZuulRequestHeader("CORRELATION_ID",CORRELATION_ID);
    }
}
