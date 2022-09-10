package com.sky.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sky.domain.Category;
import com.sky.domain.Product;

@Repository("ProductRepositoryDBimpl")
public class ProductRepositoryDBimpl implements ProductRepository {
	private final static String INSERT_PRODUCT_SQL = "insert into products(productId, productName, productPrice,catId) values(?,?,?,?)";
//	private final static String ALL_PRODUCT_SQL= "Select productId,productName,productprice from products";
	private static String PRODUCT_BY_ID_SQL="Select productID,productName,productprice from products where productId=?";
	private final static String UPDATE_PRODUCT_SQL ="update products set  productName=?,productPrice=? where productId=?";	
	private final static String DELETE_PRODUCT_SQL = "delete from products where productId=?";
	private final static String ALL_PRODUCT_SQL = 
			"Select productId, productName,productprice , b.categoryID as categoryID, categoryName from products a , category  b where a.catID = b.categoryID";
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void addProduct(Product product) {
		jdbcTemplate.update(INSERT_PRODUCT_SQL, product.getProductId(), product.getProductName(), product.getPrice(), product.getCategory().getCatId());

	}

	@Override
	public List<Product> getProduct() {
		return this.jdbcTemplate.query(ALL_PRODUCT_SQL,new ProductRowMapper());
	}

	@Override
	public void deleteProduct(Product product) {
	   jdbcTemplate.update(DELETE_PRODUCT_SQL,product.getProductId());

	}

	@Override
	public Product getProductById(String productId) {
		return this.jdbcTemplate.queryForObject(PRODUCT_BY_ID_SQL, new Object[] {productId},new ProductRowMapper());
	}

	@Override
	public Product updateProduct(Product product) {
		// TODO Auto-generated method stub
		jdbcTemplate.update(UPDATE_PRODUCT_SQL,product.getProductName(),product.getPrice(),product.getProductId());
		return product;
	}
	 private static final class ProductRowMapper implements org.springframework.jdbc.core.RowMapper<Product>{
		 public Product mapRow(ResultSet rs,int rownum) throws SQLException{
			 
			 Product product= new Product();
			         	product.setProductId(rs.getString("productId"));
			         	product.setProductName(rs.getString("productName"));
			         	product.setPrice(rs.getInt("productprice"));
			 			
			 			Category category = new Category();
						category.setCatId(rs.getString("categoryID"));
						category.setCatName(rs.getString("categoryName"));
						product.setCategory(category);
			 			return product;
			  
		 }
	   }

}
