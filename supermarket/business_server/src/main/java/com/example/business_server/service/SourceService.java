package com.example.business_server.service;

import com.example.business_server.entity.Goods;
import com.example.business_server.entity.Source;
import com.example.business_server.entity.SourceDetailed;
import com.example.business_server.mapper.GoodsMapper;
import com.example.business_server.mapper.SourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class SourceService {

    @Autowired(required = false)
    SourceMapper sourceMapper;

    @Autowired(required = false)
    GoodsMapper goodsMapper;

    @RequestMapping(value = "/getSourceList",method = RequestMethod.GET)
    public List<Source> getSourceList(){
        return sourceMapper.findSource(20);
    }

    @RequestMapping(value = "/getSourceById",method = RequestMethod.GET)
    public Source getSourceById(String source_order_id){
        return sourceMapper.findSourceById(source_order_id);
    }

    @RequestMapping(value = "/getSourceWithTime",method = RequestMethod.GET)
    public List<Source> getSourceWithTime(String source_time){
        return sourceMapper.findSourceWithTime(source_time);
    }

    @RequestMapping(value = "/getSourceDetailedById",method = RequestMethod.GET)
    public List<SourceDetailed> getSourceDetailedById(String source_order_id){
        return sourceMapper.findSourceDetailedById(source_order_id);
    }

    @RequestMapping(value = "/addSource",method = RequestMethod.POST)
    public void addSource(@RequestBody List<Goods> sources){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int source_total_price = 0;
        String source_order_id = UUID.randomUUID().toString();
        sources.stream().forEach(x->{
            goodsMapper.addGoodsInformation(x);
        });
        for (int i = 0;i < sources.size();i++){
            SourceDetailed sourceDetailed = new SourceDetailed();
            sourceDetailed.setSource_order_id(source_order_id);
            sourceDetailed.setSource_goods_name(sources.get(i).getGoods_name());
            sourceDetailed.setSource_subtotal(sources.get(i).getGoods_original_price()*sources.get(i).getGoods_number());
            sourceDetailed.setSource_goods_id(sources.get(i).getGoods_id());
            sourceDetailed.setSource_time(simpleDateFormat.format(date));
            sourceMapper.addSourceDetailed(sourceDetailed);
            source_total_price += sources.get(i).getGoods_original_price()*sources.get(i).getGoods_number();
        }
        Source source = new Source();
        source.setSource_order_id(source_order_id);
        source.setSource_of_supply("河南");
        source.setSource_time(simpleDateFormat.format(date));
        source.setSource_total_price(source_total_price);
        sourceMapper.addSource(source);
    }

    @RequestMapping(value = "/deleteSource",method = RequestMethod.GET)
    public void deleteSource(String source_order_id){
        sourceMapper.deleteSource(source_order_id);
    }
}
