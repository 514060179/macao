package com.yinghai.macao.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yinghai.macao.common.model.User;
import com.yinghai.macao.common.util.Page;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by TaxiGo02 on 2017/1/17.
 */
//@Controller
@RequestMapping("/user")
public class UserController {
////    @Autowired
//    private UserService userService;
//    private Logger log = Logger.getLogger(this.getClass());
//    @RequestMapping(value = "login", method = RequestMethod.GET)
//    public void loginIn(HttpServletRequest request, HttpServletResponse response){
//        User user = new User();
//        user.setName("simon");
//        user.setPassword("123");
//        System.out.println(userService.createUser(user));
//        log.info("====info===="+user);
//        System.out.println(userService.findById(1));
//        User u = new User();
//        Page<User> page = userService.findByPage(0,3,u);
//        long tatol = page.getTotal();
//        System.out.println(tatol);
//        List<User> userList = page.getResult();
//        System.out.println(userList);
//        JSONObject jsonObject = new JSONObject();
//        log.error("====error===="+user);
//        JSONObject.parseObject("");
//        System.out.println(JSON.toJSON(user));
//        System.out.println(JSONObject.parseObject("{'id':123,'name':21}"));
//    }
    @RequestMapping(value = "driver", method = RequestMethod.GET)
    public String test(HttpServletRequest request, HttpServletResponse response){
        return "login";

    }
}
