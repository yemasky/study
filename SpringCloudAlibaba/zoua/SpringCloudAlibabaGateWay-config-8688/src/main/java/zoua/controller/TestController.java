package zoua.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test/")
@RestController
public class TestController {
    @RequestMapping("gwy")
    public String testNotParamFunction(){
        return "success";
    }
}
