package com.jpa.mpos.processor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.jpa.mpos.data.Category;
import com.jpa.mpos.data.Merchant;
import com.jpa.mpos.data.Product;

@Component
@Repository
public class ProductRepository {

	private JdbcTemplate jdbcTemplate;

	public List<Product> getAllProducts(int mid, int pageSize, int rowNum) {
		try {
			List<Product> prod = this.jdbcTemplate.query(
					"SELECT id, merchant_id, category_id, name, sku, description, price, strike_price, publish_price, image, publish, created_date, modified_date FROM products WHERE merchant_id = ? ORDER BY id DESC LIMIT ?,?;",
					new Object[] { mid, pageSize, rowNum }, new RowMapper<Product>() {
						public Product mapRow(ResultSet rs, int arg1) throws SQLException {
							Product prod = new Product();
							Category cat = new Category();
							cat.setId(rs.getInt("category_id"));
							Merchant m = new Merchant();
							m.setId(rs.getInt("merchant_id"));
							prod.setCategory(cat);
							prod.setCreatedDate(rs.getTimestamp("created_date"));
							prod.setDescription(rs.getString("description"));
							prod.setId(rs.getInt("id"));
							prod.setImage(rs.getString("image"));
							prod.setMerchant(m);
							prod.setModifiedDate(rs.getTimestamp("modified_date"));
							prod.setName(rs.getString("name"));
							prod.setPrice(rs.getBigDecimal("price"));
							prod.setPublish(rs.getBoolean("publish"));
							prod.setPublishPrice(rs.getBoolean("publish_price"));
							prod.setSku(rs.getString("sku"));
							prod.setStrikePrice(rs.getBigDecimal("strike_price"));
							return prod;
						}
					});
			return prod;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Product getProductByID(int id, int mid) {
		try {
			Product prod = this.jdbcTemplate.queryForObject(
					"SELECT id, merchant_id, category_id, name, sku, description, price, strike_price, publish_price, image, publish, created_date, modified_date FROM products WHERE merchant_id = ? AND id = ?;",
					new Object[] { mid, id }, new RowMapper<Product>() {
						public Product mapRow(ResultSet rs, int arg1) throws SQLException {
							Product prod = new Product();
							Category cat = new Category();
							cat.setId(rs.getInt("category_id"));
							Merchant m = new Merchant();
							m.setId(rs.getInt("merchant_id"));
							prod.setCategory(cat);
							prod.setCreatedDate(rs.getTimestamp("created_date"));
							prod.setDescription(rs.getString("description"));
							prod.setId(rs.getInt("id"));
							prod.setImage(rs.getString("image"));
							prod.setMerchant(m);
							prod.setModifiedDate(rs.getTimestamp("modified_date"));
							prod.setName(rs.getString("name"));
							prod.setPrice(rs.getBigDecimal("price"));
							prod.setPublish(rs.getBoolean("publish"));
							prod.setPublishPrice(rs.getBoolean("publish_price"));
							prod.setSku(rs.getString("sku"));
							prod.setStrikePrice(rs.getBigDecimal("strike_price"));
							return prod;
						}
					});
			return prod;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Product getProductBySKU(String sku, int mid) {
		try {
			Product prod = this.jdbcTemplate.queryForObject(
					"SELECT id, merchant_id, category_id, name, sku, description, price, strike_price, publish_price, image, publish, created_date, modified_date FROM products WHERE merchant_id = ? AND sku = ?;",
					new Object[] { mid, sku }, new RowMapper<Product>() {
						public Product mapRow(ResultSet rs, int arg1) throws SQLException {
							Product prod = new Product();
							Category cat = new Category();
							cat.setId(rs.getInt("category_id"));
							Merchant m = new Merchant();
							m.setId(rs.getInt("merchant_id"));
							prod.setCategory(cat);
							prod.setCreatedDate(rs.getTimestamp("created_date"));
							prod.setDescription(rs.getString("description"));
							prod.setId(rs.getInt("id"));
							prod.setImage(rs.getString("image"));
							prod.setMerchant(m);
							prod.setModifiedDate(rs.getTimestamp("modified_date"));
							prod.setName(rs.getString("name"));
							prod.setPrice(rs.getBigDecimal("price"));
							prod.setPublish(rs.getBoolean("publish"));
							prod.setPublishPrice(rs.getBoolean("publish_price"));
							prod.setSku(rs.getString("sku"));
							prod.setStrikePrice(rs.getBigDecimal("strike_price"));
							return prod;
						}
					});
			return prod;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Product> getProductByCategory(int mid, int catID, int pageSize, int rowNum) {
		try {
			List<Product> prod = this.jdbcTemplate.query(
					"SELECT id, merchant_id, category_id, name, sku, description, price, strike_price, publish_price, image, publish, created_date, modified_date FROM products WHERE merchant_id = ? AND category_id = ? ORDER BY id DESC LIMIT ?,?;",
					new Object[] { mid, catID, pageSize, rowNum }, new RowMapper<Product>() {
						public Product mapRow(ResultSet rs, int arg1) throws SQLException {
							Product prod = new Product();
							Category cat = new Category();
							cat.setId(rs.getInt("category_id"));
							Merchant m = new Merchant();
							m.setId(rs.getInt("merchant_id"));
							prod.setCategory(cat);
							prod.setCreatedDate(rs.getTimestamp("created_date"));
							prod.setDescription(rs.getString("description"));
							prod.setId(rs.getInt("id"));
							prod.setImage(rs.getString("image"));
							prod.setMerchant(m);
							prod.setModifiedDate(rs.getTimestamp("modified_date"));
							prod.setName(rs.getString("name"));
							prod.setPrice(rs.getBigDecimal("price"));
							prod.setPublish(rs.getBoolean("publish"));
							prod.setPublishPrice(rs.getBoolean("publish_price"));
							prod.setSku(rs.getString("sku"));
							prod.setStrikePrice(rs.getBigDecimal("strike_price"));
							return prod;
						}
					});
			return prod;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Map<Integer, Product> getProductInMap(List<Integer> ids) {
		String sql = "select * from products where id in (:productID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("productID", ids);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		Map<Integer, Product> mapRet = template.query(sql, paramMap, new ResultSetExtractor<Map<Integer, Product>>() {
			@Override
			public Map<Integer, Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
				HashMap<Integer, Product> mapRet = new HashMap<Integer, Product>();
				while (rs.next()) {
					Product prod = new Product();
					Category cat = new Category();
					cat.setId(rs.getInt("category_id"));
					Merchant m = new Merchant();
					m.setId(rs.getInt("merchant_id"));
					prod.setCategory(cat);
					prod.setCreatedDate(rs.getTimestamp("created_date"));
					prod.setDescription(rs.getString("description"));
					prod.setId(rs.getInt("id"));
					prod.setImage(rs.getString("image"));
					prod.setMerchant(m);
					prod.setModifiedDate(rs.getTimestamp("modified_date"));
					prod.setName(rs.getString("name"));
					prod.setPrice(rs.getBigDecimal("price"));
					prod.setPublish(rs.getBoolean("publish"));
					prod.setPublishPrice(rs.getBoolean("publish_price"));
					prod.setSku(rs.getString("sku"));
					prod.setStrikePrice(rs.getBigDecimal("strike_price"));
					mapRet.put(rs.getInt("id"), prod);
				}
				return mapRet;
			}
		});
		return mapRet;
	}

	/**
	 * public void createProduct(Product prod) { jdbcTemplate.update( "insert into
	 * products (merchant_id, category_id, name, sku, description, price,
	 * strike_price, publish_price, image) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
	 * prod.getMerchant().getId(), prod.getCategory().getId(), prod.getName(),
	 * prod.getSku(), prod.getDescription(), prod.getPrice(), prod.getStrikePrice(),
	 * prod.getPublishPrice(), prod.getImage()); }
	 **/

	public long createProduct(Product prod) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(
					"insert into products (merchant_id, category_id, name, sku, description, price, strike_price, publish_price, image) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, prod.getMerchant().getId());
			ps.setInt(2, prod.getCategory().getId());
			ps.setString(3, prod.getName());
			ps.setString(4, prod.getSku());
			ps.setString(5, prod.getDescription());
			ps.setBigDecimal(6, prod.getPrice());
			ps.setBigDecimal(7, prod.getStrikePrice());
			ps.setBoolean(8, prod.getPublishPrice());
			ps.setString(9, prod.getImage());
			return ps;
		}, keyHolder);

		return (long) keyHolder.getKey();
	}

	public void updateProduct(Object id, Product prod) {
		jdbcTemplate.update(
				"update products set category_id = ?, name = ?, sku = ?, description = ?, price = ?, strike_price = ?, publish_price = ?, image = ? where id = ?",
				prod.getCategory().getId(), prod.getName(), prod.getSku(), prod.getDescription(), prod.getPrice(),
				prod.getStrikePrice(), prod.getPublishPrice(), prod.getImage(), id);
	}

	public void deleteProduct(int mid, int id) {
		jdbcTemplate.update("delete from products  where merchant_id = ? AND id = ?", mid, id);
	}

	public Integer count() {
		Integer res = jdbcTemplate.queryForObject("select count(id) from products", Integer.class);
		return res;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
