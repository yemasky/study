package zoua.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StockMapper {
    @Update("UPDATE stock SET stock_number=stock_number-#{stock_number} WHERE product_id=#{product_id}")
    void updateStock(int stock_number, int product_id);
}
