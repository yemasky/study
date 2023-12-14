package consumer.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import consumer.entity.Order;

@Mapper
public interface OrderMapper {

	@Insert("INSERT INTO order(order_name, order_number) VALUES(#{order_name}, #{order_number})")
	int insertOrder(Order order);
}
