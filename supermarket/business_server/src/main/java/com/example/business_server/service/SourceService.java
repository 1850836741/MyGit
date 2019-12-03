package com.example.business_server.service;

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

}
