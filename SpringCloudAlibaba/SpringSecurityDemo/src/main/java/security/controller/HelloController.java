package security.controller;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    /*
     * 获取登录信息
     */
    @RequestMapping("/info")
    public String info() {
        String userDetails = null;
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userDetails = ((UserDetails) principal).getUsername();
        } else {
            userDetails = principal.toString();
        }
        return userDetails;
    }
}
