package zoua.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import zoua.entity.User;
import zoua.service.IUserService;

@RestController
public class ProductsController {

	@Value("${server.port}")
	private int port;

	@GetMapping("/product/{id}")
	public String products(@PathVariable("id") Integer id) {
		return "调用商品服务返回：" + id + ",当前提供服务的端口为：" + port;
	}
	
    @Autowired
    private IUserService iUserService;
    /*@Autowired
    public ProductsController(IUserService iUserService) {
        this.iUserService = iUserService;
    }*/
 
    @GetMapping("/simple/{id}")
    public User findUserById(@PathVariable Long id) {
        return this.iUserService.findUserById(id);
    }
 
    @GetMapping("/simple/list")
    public List<User> findUserList() {
        return this.iUserService.findAllUsers();
    }
 
    /**
     * 添加一个student,使用postMapping接收post请求
     *
     * http://localhost:8310/simple/addUser?username=user11&age=11&balance=11
     *
     * @return
     */
    @GetMapping("/simple/addUser")
    public User addUser(@RequestParam(value = "username", required=false) String username, @RequestParam(value = "age", required=false) Integer age, @RequestParam(value = "balance", required=false) String balance){
        User user=new User();
 
        user.setUsername(username);
        user.setName(username);
        user.setAge(age);
        user.setBalance(balance);
 
        int result = iUserService.insertUser(user);
        if(result > 0){
            return user;
        }
 
        user.setId(0L);
        user.setName(null);
        user.setUsername(null);
        user.setBalance(null);
        return user;
    }
}
