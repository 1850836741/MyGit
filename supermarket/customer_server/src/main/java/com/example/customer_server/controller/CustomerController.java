package com.example.customer_server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.customer_server.entity.Customer;
import com.example.customer_server.entity.Goods;
import com.example.customer_server.entity.Orders;
import com.example.customer_server.mapper.CustomerMapper;
import com.example.customer_server.service.CustomerService;
import com.example.customer_server.service.cache.FindGoodsFromCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionAttributes(value = {"customer","shopCart"})
@Controller
public class CustomerController {

    @Bean
    public JSON json(){
        return new JSONObject();
    }

    @Autowired(required = false)
    HttpServletRequest httpsServletRequest;

    @Autowired(required = false)
    CustomerMapper customerMapper;

    @Autowired(required = false)
    RedisTemplate redisTemplate;

    @Autowired
    FindGoodsFromCache findGoodsFromCache;

    @Autowired
    CustomerService customerService;
    /**
     * 主页返回50个热点商品信息
     * @param model
     * @return
     */
    public String getHotGoods(Model model){
        model.addAttribute("goodsMap",customerService.findAllHotGoods(50));
        return "index";
    }

    /**
     * 查找对应名字的商品
     * @param model
     * @return
     */
    public String findGoodsByName(Model model){
        String goods_name = httpsServletRequest.getParameter("goods_name");
        Map<String, Goods> goodsMap = findGoodsFromCache.findGoodsByName(goods_name);
        model.addAttribute("goodsMap",goodsMap);
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/q")
    public double getOrderTotal(String time){
       return customerService.findOrderLimitTime(time);
    }

    @RequestMapping(value = "/toIndex",method = RequestMethod.GET)
    public String toIndex(Model model){
        Map<String,Goods> goodsMap = findGoodsFromCache.findAllHotGoods(50);
        model.addAttribute("goodsMap",goodsMap);
        return "index";
    }

    @RequestMapping(value = "/loginSuccess",method = RequestMethod.POST)
    public String loginSuccess(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Customer customer = customerService.findCustomerInformation(user.getUsername());
        model.addAttribute("customer",customer);
        return "redirect:/toIndex";
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String registerCustomer(String customer_account,String customer_password){
        Customer customer = new Customer();
        customer.setCustomer_account(customer_account);
        customer.setCustomer_password(customer_password);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        customer.setRegistration_time(simpleDateFormat.format(date));
        customer.setIs_vip("T");
        customer.setCustomer_role("ROLE_USER");
        customerService.registerCustomer(customer);
        return "redirect:/login";
    }


    @RequestMapping(value = "/goodsInformation",method = RequestMethod.GET)
    public String goodsInformation(String goods_id,Model model){
        Goods goods = findGoodsFromCache.findGoodsById(Integer.parseInt(goods_id));
        model.addAttribute("goods",goods);
        return "goods";
    }

    @RequestMapping(value = "/addCart",method = RequestMethod.POST)
    public String addCart(HttpServletRequest httpServletRequest,String number,int goods_id,Model model){
        Customer customer = (Customer) httpServletRequest.getSession().getAttribute("customer");
        if (customer==null){
            return "redirect:/login";
        }
        Goods goods = findGoodsFromCache.findGoodsById(goods_id);
        customerService.setShoppingCart(httpServletRequest,goods,Integer.parseInt(number));
        model.addAttribute("shopCart",httpServletRequest.getSession().getAttribute("shopCart"));
        return "shopCart";
    }

    @RequestMapping(value = "/deleteCartGoods",method = RequestMethod.GET)
    public String deleteCartGoods(HttpServletRequest httpServletRequest,int goods_id){
        customerService.deleteCartOne(httpServletRequest,String.valueOf(goods_id));
        return "shopCart";
    }

    @RequestMapping(value = "/buyCart",method = RequestMethod.POST)
    public String buyCart(HttpServletRequest httpServletRequest){
        Customer customer = (Customer) httpServletRequest.getSession().getAttribute("customer");
        if (customer==null){
            return "redirect:/login";
        }
        Map<Goods,Integer> shopCart = (Map<Goods, Integer>) httpServletRequest.getSession().getAttribute("shopCart");
        customerService.setOrder(customer,shopCart);
        return "";
    }

    @RequestMapping(value = "/buy",method = RequestMethod.POST)
    public String buy(HttpServletRequest httpServletRequest,int goods_id,int number){
        Customer customer = (Customer) httpServletRequest.getSession().getAttribute("customer");
        if (customer==null){
            return "redirect:/login";
        }
        Map<Goods,Integer> buy = new HashMap<>();
        buy.put(findGoodsFromCache.findGoodsById(goods_id),number);
        customerService.setOrder(customer,buy);
        return "";
    }

    @RequestMapping(value = "/toShopCart",method = RequestMethod.GET)
    public String toShopCart(HttpServletRequest httpServletRequest){
        Customer customer = (Customer) httpServletRequest.getSession().getAttribute("customer");
        if (customer==null){
            return "redirect:/login";
        }
        return "shopCart";
    }

    @RequestMapping(value = "/toMyOrder",method = RequestMethod.GET)
    public String toCustomerOrder(HttpServletRequest httpServletRequest,Model model){
        Customer customer = (Customer) httpServletRequest.getSession().getAttribute("customer");
        if (customer==null){
            return "redirect:/login";
        }
        List<Orders> orders = customerService.findMyOrder(customer.getCustomer_account());
        model.addAttribute("orders",orders);
        return "MyOrder";
    }

    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public String search(String goods_name,Model model){
        Map<String,Goods> goodsMap = customerService.findGoodsByName(goods_name);
        model.addAttribute("goodsMap",goodsMap);
        return "index";
    }
}
