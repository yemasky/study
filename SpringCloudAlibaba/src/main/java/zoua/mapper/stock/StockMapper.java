package zoua.mapper.stock;

import org.springframework.beans.factory.annotation.Autowired;

import zoua.mapper.IStockMapper;

public class StockMapper implements IStockMapper {
	@Autowired
	IStockMapper iStockMapper;
	
	//@Override
	//public void updateStock(int stock_number, int product_id) {
		// TODO Auto-generated method stub
		//iStockMapper.updateStock(stock_number, product_id);
	//}

}
