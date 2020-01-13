package com.example.zuul_server.filter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 关联ID前置过滤器，用来给每个用户的请求链关联一个随机ID，便于链路追踪和日志分析
 */
@Component
public class RelationFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(RelationFilter.class);

    @Autowired
    RelationUnits relationUnits;

    @Override
    public String filterType() {
        return "pre";                  //指定过滤器类型，pre为前置过滤器
    }

    @Override
    public int filterOrder() {
        return 0;                     //过滤器顺序，数字越小越先执行
    }

    @Override
    public boolean shouldFilter() {
        return true;                  //是否执行
    }

    /*创建关联id*/
    private String creatRelationId(){
        return UUID.randomUUID().toString();
    }

    /*请求是否包含CORRELATION_ID*/
    private boolean contain(){
        return relationUnits.getRelationId()!=null;
    }

    /*真正执行的逻辑，并打印日志*/
    @Override
    public Object run() throws ZuulException {
        if (!contain()){
            String ID = creatRelationId();
            relationUnits.setRelationId(ID);
            logger.debug("创建新的请求关联ID:{}",ID);
        }
        RequestContext requestContext = RequestContext.getCurrentContext();
        logger.debug("正在处理传入请求{}",requestContext.getRequest().getRequestURI());
        return null;
    }
}
