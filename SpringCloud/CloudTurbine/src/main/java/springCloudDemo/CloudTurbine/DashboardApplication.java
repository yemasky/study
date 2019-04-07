package springCloudDemo.CloudTurbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * DashboardApplication
 *
 */

@SpringBootApplication
@EnableHystrixDashboard
@EnableTurbine
public class DashboardApplication {

	//断路器指标数据监控Hystrix Dashboard 和 Turbine
    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class, args);
    }
}
