package zoua.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

	@RequestMapping("/demo")
	public String demo() {
		return "demo ok!!!";
	}
	
	@RequestMapping("/testA")
    public String testA(){
        return "-----------------testA";
    }

	@RequestMapping("/testB")
    public String testB(){
        return "-----------------testB";
    }
}
