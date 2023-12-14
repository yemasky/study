package demo2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String index() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/findAll")
    @ResponseBody
    public String findAll() {
        return "findAll";
    }
    
    @PostMapping("/success")
    public String success() {
    	return "success";
    }
    
    @PostMapping("/fail")
    public String fail() {
    	return "fail";
    }
}
