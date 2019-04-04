package springCloudDemo.CloudServiceFeign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * HelloWorldService
 *
 */
@FeignClient(value = "SERVICE-HELLOWORLD")
public interface HelloWorldService
{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String sayHello();
}
