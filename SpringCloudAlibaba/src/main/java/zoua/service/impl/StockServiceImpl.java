package zoua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zoua.mapper.IStockMapper;
import zoua.service.StockService;

@Service
public class StockServiceImpl implements StockService  {
	@Autowired
	IStockMapper iStockMapper;

	@Override
	public void updateStock(int stock_number, int product_id) {
		// TODO Auto-generated method stub
		//iStockMapper.updateStock(stock_number, product_id);
	}

	

	

	
	

	
}
