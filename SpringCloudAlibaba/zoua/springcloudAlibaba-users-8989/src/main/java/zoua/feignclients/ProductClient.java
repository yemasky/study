package zoua.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("PRODUCTS")
public interface ProductClient {
    @RequestMapping("/product/{id}")
    String product(@PathVariable("id") Integer id);
}
