package com.example.customer_server.service;

import com.example.customer_server.entity.Customer;
import com.example.customer_server.entity.Goods;
import com.example.customer_server.entity.Orders;
import com.example.customer_server.mapper.CustomerMapper;
import com.example.customer_server.mapper.OrderMapper;
import com.example.customer_server.service.cache.FindCustomerFromCache;
import com.example.customer_server.service.cache.FindGoodsFromCache;
import com.example.customer_server.service.queue.CustomerQueueService;
import com.example.customer_server.service.queue.GoodsQueueService;
import com.example.customer_server.service.safe.LockConfig;
import com.example.customer_server.service.safe.ZookeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerService {

    @Autowired
    ZookeeperService zookeeperService;

    @Autowired
    CustomerQueueService customerQueueService;

    @Autowired
    FindCustomerFromCache findCustomerFromCache;

    @Autowired
    FindGoodsFromCache findGoodsFromCache;

    @Autowired
    GoodsQueueService goodsQueueService;

    @Autowired(required = false)
    OrderMapper orderMapper;

    @Autowired(required = false)
    CustomerMapper customerMapper;

    public Customer findCustomerById(String customer_account){

        return customerMapper.findCustomerById(customer_account);
    }
    /**
     * 注册用户
     * @param customer
     */
    public void registerCustomer(Customer customer){
        customerQueueService.addCustomer("customer",customer);
    }

    /**
     * 修改账户密码
     * @param customer
     * @param updateMap
     */
    public void updateCustomerPassword(Customer customer, Map<String,String> updateMap){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LockConfig.CUSTOMER_LOCK_PATH).append(customer.getCustomer_account());
        zookeeperService.getWriteLock(stringBuilder.toString());
        findCustomerFromCache.deleteCustomerById(customer.getCustomer_account());
        customerQueueService.Update("customer",customer.getCustomer_account(),updateMap);
    }

    /**
     * 购买vip
     * @param customer
     */
    public void beVip(Customer customer){
        Map<String,String> updateMap = new HashMap<>();
        updateMap.put("is_vip","T");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LockConfig.CUSTOMER_LOCK_PATH).append(customer.getCustomer_account());
        zookeeperService.getWriteLock(stringBuilder.toString());
        findCustomerFromCache.deleteCustomerById(customer.getCustomer_account());
        customerQueueService.Update("customer",customer.getCustomer_account(),updateMap);
    }

    /**
     * 查找顾客信息
     * @param customer_account
     */
    public Customer findCustomerInformation(String customer_account){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LockConfig.CUSTOMER_LOCK_PATH).append(customer_account);
        zookeeperService.getReadLock(stringBuilder.toString());
        return findCustomerFromCache.findCustomerById(customer_account);
    }

    public List<Orders> findMyOrder(String customer_account){
        return orderMapper.findMyOder(customer_account);
    }
    /**
     * 修改顾客信息
     * @param customer
     * @param updateMap
     */
    public void updateCustomerInformation(Customer customer, Map<String,String> updateMap){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LockConfig.CUSTOMER_LOCK_PATH).append(customer.getCustomer_account());
        zookeeperService.getWriteLock(stringBuilder.toString());
        findCustomerFromCache.deleteCustomerById(customer.getCustomer_account());
        customerQueueService.Update("customer",customer.getCustomer_account(),updateMap);
    }


    /**
     * 顾客购买商品
     * @param customer
     * @param goodsMap
     * @return
     */
    public Map<String,String> setOrder(Customer customer, Map<Goods,Integer> goodsMap){
        Map<String,String> errorMap = new HashMap<>();
        for (Goods goods : goodsMap.keySet()){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LockConfig.GOODS_LOCK_PATH).append(goods.getGoods_id());
            String nodePath = zookeeperService.getWriteLock(stringBuilder.toString());
            Goods thisGoods = findGoodsFromCache.findGoodsById(goods.getGoods_id());
            int number = thisGoods.getGoods_number();
            if (number > goodsMap.get(goods)){
                thisGoods.setGoods_number(number);
                findGoodsFromCache.updateGoods(thisGoods.getGoods_id());
                Map<String,String> updateMap = new HashMap<>();
                updateMap.put("goods_number", String.valueOf(number));
                goodsQueueService.updateGoods("goods",goods.getGoods_id(),updateMap);
            }else {
                errorMap.put(goods.getGoods_name(),"库存不足");
                zookeeperService.deleteLock(nodePath);
            }
        }
        return errorMap;
    }


    /**
     * 获取购物车
     * @param httpServletRequest
     * @return
     */
    public Map<Goods,Integer> getShoppingCart(HttpServletRequest httpServletRequest){
        Map<Goods,Integer> goodsMap = (Map<Goods, Integer>) httpServletRequest.getSession().getAttribute("shopCart");
        return goodsMap;
    }

    /**
     * 向购物车里添加商品
     * @param httpServletRequest
     * @param goods
     * @param number
     */
    public void setShoppingCart(HttpServletRequest httpServletRequest, Goods goods, int number){
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute("shopCart")!=null){
            Map<Goods,Integer> goodsMap = (Map<Goods, Integer>) httpSession.getAttribute("shopCart");
            goodsMap.put(goods,number);
        }else {
            Map<Goods,Integer> goodsMap = new HashMap<>();
            goodsMap.put(goods,number);
            httpSession.setAttribute("shopCart",goodsMap);
        }
    }

    /**
     * 清空购物车
     * @param httpServletRequest
     */
    public void deleteAllCart(HttpServletRequest httpServletRequest){
        HttpSession httpSession = httpServletRequest.getSession();
        Map<Goods,Integer> goodsMap = new HashMap<>();
        httpSession.setAttribute("shopCart",goodsMap);
    }

    /**
     * 删除购物车中的某个商品
     * @param httpServletRequest
     * @param goods_id
     */
    public void deleteCartOne(HttpServletRequest httpServletRequest,String goods_id){
        Map<Goods,Integer> goodsMap = (Map<Goods, Integer>) httpServletRequest.getSession().getAttribute("shopCart");
        for (Goods goods : goodsMap.keySet()){
            if (goods.getGoods_id() == Integer.parseInt(goods_id)){
                goodsMap.remove(goods);
            }
        }
    }

    /**
     * 查找热点key
     * @param number
     * @return
     */
    public Map<String,Goods> findAllHotGoods(long number){
        return findGoodsFromCache.findAllHotGoods(number);
    }

    /**
     * 根据名字查找
     * @param goods_name
     * @return
     */
    public Map<String,Goods> findGoodsByName(String goods_name){
        return findGoodsFromCache.findGoodsByName(goods_name);
    }

    /**
     * 计算指定时间的订单的总计
     * @param order_time
     * @return
     */
    public double findOrderLimitTime(String order_time){
        List<Integer> list = orderMapper.findOrderLimitTime(order_time);
        double Total = 0.0;
        for (Integer integer : list){
            Total+=integer;
        }
        return Total;
    }

}
