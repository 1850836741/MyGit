package com.example.business_server.service;

<<<<<<< HEAD
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
=======
import com.example.business_server.entity.SourceOrder;
import com.example.business_server.entity.SourceOrderInformation;
import com.example.business_server.mapper.SourceOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceService {

    @Autowired(required = false)
    SourceOrderMapper sourceOrderMapper;

    /**
     * 查询对应ID的订单
     * @param source_order_id
     * @return
     */
    public SourceOrder findSourceById(String source_order_id){
        return sourceOrderMapper.findSourceById(source_order_id);
    }

    /**
     * 添加货物订单
     * @param sourceOrder
     */
    public void addSource(SourceOrder sourceOrder){
        sourceOrderMapper.addSource(sourceOrder);
    }

    /**
     * 添加货物明细订单
     * @param sourceOrderInformation
     */
    public void addSourceInformation(SourceOrderInformation sourceOrderInformation){
        sourceOrderMapper.addSourceInformation(sourceOrderInformation);
    }

    /**
     * 查询货源订单明细
     * @param source_order_id
     * @return
     */
    public List<SourceOrderInformation> findSourceInformationById(String source_order_id){
        return sourceOrderMapper.findSourceInformationById(source_order_id);
    }

    /**
     * 查询一定数量的订单
     * @param number
     * @return
     */
    public List<SourceOrder> findSourceOrderLimitNumber(long number){
        return sourceOrderMapper.findSourceLimitNumber(number);
    }

    /**
     * 删除货物订单(包括明细)
     * @param source_order_id
     */
    public void deleteSource(String source_order_id){
        sourceOrderMapper.deleteSource(source_order_id);
    }

    /**
     * 计算货源订单总价
     * @param time
     * @return
     */
    public Double CalculationSource(String time){
        List<SourceOrder> list = sourceOrderMapper.findSourceLimitTime(time);
        double Total = 0.0;
        for (SourceOrder sourceOrder:list){
            Total+=sourceOrder.getSource_order_total_sum();
        }
        return Total;
    }

>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
}
