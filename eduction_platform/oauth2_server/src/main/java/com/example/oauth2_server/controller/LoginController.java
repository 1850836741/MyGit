package com.example.oauth2_server.controller;

import com.example.oauth2_server.entity.User;
import com.example.oauth2_server.service.UserService;
import com.example.oauth2_server.tool.CloseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    /**
     * 登陆后自动调用此方法，请求token并返回
     * @return
     */
    @RequestMapping(value = "/ee",method = RequestMethod.POST)
    public String getToken(HttpServletRequest httpServletRequest){
        HttpURLConnection httpURLConnection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL("http://localhost:8082/oauth/token?grant_type=password&client_id=client&client_secret=secret&username=100001&password=123456");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200){
                is = httpURLConnection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                while ((result = br.readLine())!=null){
                    stringBuilder.append(result);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            CloseTool.Close(1,br , is);
            httpURLConnection.disconnect();
        }
        return stringBuilder.toString();
    }

    /**
     * 添加用户信息到缓存(暴露出来，供注册直接登陆的接口使用)
     * @param user
     */
    @RequestMapping(value = "/addUserToCache",method = RequestMethod.POST)
    public void addUserToCache(@RequestBody User user){
        userService.addUser(user);
    }
}
