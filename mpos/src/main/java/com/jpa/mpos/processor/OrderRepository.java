package com.jpa.mpos.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.jpa.mpos.data.Order;
import com.jpa.mpos.data.OrderProduct;
import com.jpa.mpos.data.Product;

@Component
@Repository
public class OrderRepository {

	private JdbcTemplate jdbcTemplate;

	public void createOrder(Order order) {
		jdbcTemplate.update(
				"insert into orders (order_no, merchant_id, terminal_id, product_id, quantity, unit_price, notes) values (?, ?, ?, ?, ?, ?, ?)",
				order.getOrderNo(), order.getMerchant().getId(), order.getTerminal().getId(),
				order.getProduct().getId(), order.getQuantity(), order.getUnitPrice(), order.getNotes());
	}

	public void updateOrder(Order order) {
		jdbcTemplate.update(
				"update orders set quantity = ?, notes = ? where order_no = ? and merchant_id = ? and terminal_id = ?",
				order.getQuantity(), order.getNotes(), order.getOrderNo(), order.getMerchant().getId(),
				order.getProduct().getId());
	}

	public void updateOrder(int orderID, int quantity, String notes) {
		jdbcTemplate.update("update orders set quantity = ?, notes = ? where id = ?", quantity, notes, orderID);
	}

	public void updateOrderQuantity(int orderID, int quantity) {
		jdbcTemplate.update("update orders set quantity = ? where id = ?", quantity, orderID);
	}

	public void deleteProductFromOrder(int orderID) {
		jdbcTemplate.update("delete from orders where id = ?", orderID);
	}

	public void deleteOrder(String orderNo, int mid, int tid) {
		jdbcTemplate.update("delete from orders where order_no = ? and merchant_id = ? and  terminal_id = ?;", orderNo,
				mid, tid);
	}

	public List<String> getAllOrderNo(int mid, int tid) {
		try {
			List<String> orderno = this.jdbcTemplate.query(
					"SELECT order_no FROM orders WHERE merchant_id = ? AND terminal_id = ? GROUP BY order_no;",
					new Object[] { mid, tid }, new RowMapper<String>() {
						public String mapRow(ResultSet rs, int arg1) throws SQLException {
							String orderno = rs.getString("order_no");
							return orderno;
						}
					});
			return orderno;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<OrderProduct> getOrderProduct(String orderno, int mid, int tid) {
		try {
			List<OrderProduct> orderprod = this.jdbcTemplate.query(
					"SELECT id, product_id, quantity, unit_price, notes, order_date FROM orders WHERE order_no = ? AND merchant_id = ? AND terminal_id = ? ",
					new Object[] { orderno, mid, tid }, new RowMapper<OrderProduct>() {
						public OrderProduct mapRow(ResultSet rs, int arg1) throws SQLException {
							OrderProduct orderprod = new OrderProduct();
							Product prod = new Product();
							prod.setId(rs.getInt("product_id"));
							orderprod.setOrderID(rs.getInt("id"));
							orderprod.setProduct(prod);
							orderprod.setNotes(rs.getString("notes"));
							orderprod.setOrderDate(rs.getTimestamp("order_date"));
							orderprod.setQuantity(rs.getInt("quantity"));
							orderprod.setUnitPrice(rs.getBigDecimal("unit_price"));
							return orderprod;
						}
					});
			return orderprod;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public OrderProduct getOrderProduct(int id) {
		try {
			OrderProduct orderprod = this.jdbcTemplate.queryForObject(
					"SELECT id, product_id, quantity, unit_price, notes, order_date FROM orders WHERE id= ? ",
					new Object[] { id }, new RowMapper<OrderProduct>() {
						public OrderProduct mapRow(ResultSet rs, int arg1) throws SQLException {
							OrderProduct orderprod = new OrderProduct();
							Product prod = new Product();
							prod.setId(rs.getInt("product_id"));
							orderprod.setOrderID(rs.getInt("id"));
							orderprod.setProduct(prod);
							orderprod.setNotes(rs.getString("notes"));
							orderprod.setOrderDate(rs.getTimestamp("order_date"));
							orderprod.setQuantity(rs.getInt("quantity"));
							orderprod.setUnitPrice(rs.getBigDecimal("unit_price"));
							return orderprod;
						}
					});
			return orderprod;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer getOrderProduct(int mid, int tid, int pid, String orderno) {
		try {
			Integer q = this.jdbcTemplate.queryForObject(
					"SELECT id FROM orders WHERE merchant_id = ? AND terminal_id = ? AND product_id = ? AND order_no = ?;",
					new Object[] { mid, tid, pid, orderno }, Integer.class);
			return q;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
