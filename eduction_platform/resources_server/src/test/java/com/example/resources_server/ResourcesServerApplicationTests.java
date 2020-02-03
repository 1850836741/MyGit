package com.example.resources_server;

import com.example.resources_server.entity.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourcesServerApplicationTests {

    @Test
    public void contextLoads() {
        Book book = new Book();
    }

}
