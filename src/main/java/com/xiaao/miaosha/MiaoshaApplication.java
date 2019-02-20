package com.xiaao.miaosha;

import com.xiaao.miaosha.dao.UserDoMapper;
import com.xiaao.miaosha.dataobject.UserDo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.xiaao.miaosha"})
@RestController
@MapperScan("com.xiaao.miaosha.dao")
public class MiaoshaApplication {


    @Autowired
    private UserDoMapper userDoMapper;

    @RequestMapping("/")
    public String home(){
        UserDo userDo = userDoMapper.selectByPrimaryKey(1);
        if(userDo ==null){
            return "用户对象不存在";
        }else{
            return userDo.getName();
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaApplication.class, args);
    }

}

