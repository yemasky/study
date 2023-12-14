package zoua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zoua.mapper.StockMapper;
import zoua.service.StockService;

@Service
public class StockServiceImpl implements StockService {
	@Autowired
	StockMapper stockMapper;

	@Override
	public void updateStock(int stock_number, int product_id) {
		// TODO Auto-generated method stub
		stockMapper.updateStock(stock_number, product_id);
	}
	
}
