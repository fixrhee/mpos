package com.jpa.mpos.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.jpa.mpos.data.Inventory;
import com.jpa.mpos.data.Merchant;
import com.jpa.mpos.data.Product;

@Component
@Repository
public class InventoryRepository {

	private JdbcTemplate jdbcTemplate;

	public Inventory getStockByProductID(int id, int mid) {
		try {
			Inventory inv = this.jdbcTemplate.queryForObject(
					"SELECT id, product_id, merchant_id, stock, low_stock_threshold, last_update FROM inventories WHERE merchant_id = ? AND product_id = ?;",
					new Object[] { mid, id }, new RowMapper<Inventory>() {
						public Inventory mapRow(ResultSet rs, int arg1) throws SQLException {
							Inventory inv = new Inventory();
							Product p = new Product();
							p.setId(rs.getInt("product_id"));
							Merchant m = new Merchant();
							m.setId(rs.getInt("merchant_id"));
							inv.setId(rs.getInt("id"));
							inv.setLastUpdate(rs.getTimestamp("last_update"));
							inv.setLowStockThreshold(rs.getInt("low_stock_threshold"));
							inv.setMerchant(m);
							inv.setProduct(p);
							inv.setStock(rs.getInt("stock"));
							return inv;
						}
					});
			return inv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Map<Integer, Inventory> getInventoryInMap(List<Integer> ids) {
		String sql = "select * from inventories where product_id in (:productID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("productID", ids);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		Map<Integer, Inventory> mapRet = template.query(sql, paramMap,
				new ResultSetExtractor<Map<Integer, Inventory>>() {
					@Override
					public Map<Integer, Inventory> extractData(ResultSet rs) throws SQLException, DataAccessException {
						HashMap<Integer, Inventory> mapRet = new HashMap<Integer, Inventory>();
						while (rs.next()) {
							Inventory inv = new Inventory();
							Merchant m = new Merchant();
							m.setId(rs.getInt("merchant_id"));
							Product p = new Product();
							p.setId(rs.getInt("product_id"));
							inv.setId(rs.getInt("id"));
							inv.setLastUpdate(rs.getTimestamp("last_update"));
							inv.setLowStockThreshold(rs.getInt("low_stock_threshold"));
							inv.setMerchant(m);
							inv.setProduct(p);
							inv.setStock(rs.getInt("stock"));
							mapRet.put(rs.getInt("product_id"), inv);
						}
						return mapRet;
					}
				});
		return mapRet;
	}

	public void createStock(long pid, int mid, Integer stock, Integer lowStockThreshold) {
		jdbcTemplate.update(
				"insert into inventories (product_id, merchant_id, stock, low_stock_threshold) values (?, ?, ?, ?)",
				pid, mid, stock, lowStockThreshold);
	}

	public void updateStock(Integer mid, Integer pid, Integer stock, Integer lowStockThreshold) {
		jdbcTemplate.update(
				"update inventories set stock = ?, low_stock_threshold = ? where merchant_id = ? and product_id = ?",
				stock, lowStockThreshold, mid, pid);
	}

	public void deleteStock(int mid, int pid) {
		jdbcTemplate.update("delete from inventories  where merchant_id = ? AND product_id = ?", mid, pid);
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
