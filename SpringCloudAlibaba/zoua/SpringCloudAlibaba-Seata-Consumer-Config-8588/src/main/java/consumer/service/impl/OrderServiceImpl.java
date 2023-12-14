package consumer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import consumer.entity.Order;
import consumer.mapper.OrderMapper;
import consumer.service.OrderService;

public class OrderServiceImpl implements OrderService {
	@Autowired
    OrderMapper orderMapper;
	
	@Override
	public int insertOrder(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.insertOrder(order);
	}

}
