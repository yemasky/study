package zoua.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Stock {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long stock_id;
	
	@Column
	private int product_id;
	
	@Column
	private int stock_number;

	public Long getStock_id() {
		return stock_id;
	}

	public void setStock_id(Long stock_id) {
		this.stock_id = stock_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getStock_number() {
		return stock_number;
	}

	public void setStock_number(int stock_number) {
		this.stock_number = stock_number;
	}
	

}
