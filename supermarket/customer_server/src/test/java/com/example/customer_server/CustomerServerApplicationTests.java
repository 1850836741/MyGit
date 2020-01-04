package com.example.customer_server;

<<<<<<< HEAD
import org.junit.Test;
import org.junit.runner.RunWith;
=======
import com.example.customer_server.service.safe.LockConfig;
import com.example.customer_server.service.safe.ZookeeperService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServerApplicationTests {

<<<<<<< HEAD
    @Test
    public void contextLoads() {
=======
    @Autowired
    ZookeeperService zookeeperService;
    @Test
    public void contextLoads() {

>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
    }

}
