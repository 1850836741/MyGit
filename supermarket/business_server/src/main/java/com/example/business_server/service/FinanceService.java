package com.example.business_server.service;

import com.example.business_server.entity.QuarterlyState;
import com.example.business_server.entity.StaffSalaryOrder;
import com.example.business_server.mapper.QuarterlyStateMapper;
import com.example.business_server.mapper.StaffSalaryOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class FinanceService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired(required = false)
    StaffSalaryOrderMapper staffSalaryOrderMapper;

    @Autowired(required = false)
    QuarterlyStateMapper quarterlyStateMapper;

    @Autowired
    SourceService sourceService;
    /**
     * 查询指定ID工资单
     * @param staff_order_id
     * @return
     */
    public StaffSalaryOrder findStaffSalaryOrderById(String staff_order_id){
        return staffSalaryOrderMapper.findStaffSalaryOrderById(staff_order_id);
    }

    /**
     * 查询一定数量的工资单
     * @param number
     * @return
     */
    public List<StaffSalaryOrder> findSalaryOrderLimitNumber(long number){
        return staffSalaryOrderMapper.findSalaryOrderLimitNumber(number);
    }

    /**
     * 查询时间段内的工资单
     * @param staff_time
     * @return
     */
    public List<StaffSalaryOrder> findSalaryOrderByTime(String staff_time){
        return staffSalaryOrderMapper.findSalaryOrderByTime(staff_time);
    }

    /**
     * 添加工资单
     * @param staffSalaryOrder
     */
    public void addSalaryOrder(StaffSalaryOrder staffSalaryOrder){
        staffSalaryOrderMapper.addSalaryOrder(staffSalaryOrder);
    }

    /**
     * 删除工资单
     * @param staff_order_id
     */
    public void deleteSalaryOrder(String staff_order_id){
        staffSalaryOrderMapper.deleteSalaryOrderById(staff_order_id);
    }

    /**
     * 查询对应ID的季度表
     * @param quarterly_id
     * @return
     */
    public QuarterlyState findQuarterlyState(int quarterly_id){
        return quarterlyStateMapper.findQuarterlyState(quarterly_id);
    }

    /**
     * 添加季度报表
     * @param quarterlyState
     */
    public void addQuarterlyState(QuarterlyState quarterlyState){
        quarterlyStateMapper.addQuarterlyState(quarterlyState);
    }

    /**
     * 删除季度报表
     * @param quarterly_id
     */
    void deleteQuarterlyState(int quarterly_id){
        quarterlyStateMapper.deleteQuarterlyState(quarterly_id);
    }

    /**
     * 计算指定月份,年份的工资总价
     * @param time
     * @return
     */
    public double CalculationSalary(String time){
        List<StaffSalaryOrder> staffSalaryOrders = staffSalaryOrderMapper.findSalaryOrderByTime(time);
        double Total = 0.0;
        for (StaffSalaryOrder staffSalaryOrder:staffSalaryOrders){
            Total+=staffSalaryOrder.getStaff_real_salary();
        }
        return Total;
    }

    /**
     * 计算指定月份,年份的订单总价
     * @param time
     * @return
     */
    public double CalculationOrder(String time){
        Double Total = restTemplate.getForEntity("",Double.class).getBody();
        return Total;
    }

    public double CalculationSource(String time){
        return sourceService.CalculationSource(time);
    }

    public double CalculationQuarterlyState(String time){
        return CalculationOrder(time)-(CalculationSalary(time)+CalculationSource(time));
    }
}
