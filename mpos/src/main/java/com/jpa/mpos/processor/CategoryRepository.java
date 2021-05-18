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
import com.jpa.mpos.data.Category;
import com.jpa.mpos.data.Merchant;

@Component
@Repository
public class CategoryRepository {

	private JdbcTemplate jdbcTemplate;

	public Category getCategoryByID(int id, int mid) {
		try {
			Category member = this.jdbcTemplate.queryForObject(
					"SELECT id, merchant_id, name, description, created_date FROM categories WHERE mid = ? AND id = ?;",
					new Object[] { mid, id }, new RowMapper<Category>() {
						public Category mapRow(ResultSet rs, int arg1) throws SQLException {
							Category category = new Category();
							Merchant m = new Merchant();
							m.setId(rs.getInt("merchant_id"));
							category.setId(rs.getInt("id"));
							category.setMerchant(m);
							category.setName(rs.getString("name"));
							category.setDescription(rs.getString("description"));
							category.setCreatedDate(rs.getTimestamp("created_date"));
							return category;
						}
					});
			return member;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Category> getAllCategories(int mid, int pageSize, int rowNum) {
		try {
			List<Category> member = this.jdbcTemplate.query(
					"SELECT id, merchant_id, name, description, created_date FROM categories WHERE merchant_id = ? ORDER BY id DESC LIMIT ?,?;",
					new Object[] { mid, pageSize, rowNum }, new RowMapper<Category>() {
						public Category mapRow(ResultSet rs, int arg1) throws SQLException {
							Category category = new Category();
							Merchant m = new Merchant();
							m.setId(rs.getInt("merchant_id"));
							category.setId(rs.getInt("id"));
							category.setMerchant(m);
							category.setName(rs.getString("name"));
							category.setDescription(rs.getString("description"));
							category.setCreatedDate(rs.getTimestamp("created_date"));
							return category;
						}
					});
			return member;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Map<Integer, Category> getCategoryInMap(List<Integer> ids) {
		String sql = "select * from categories where id in (:categoryID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("categoryID", ids);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		Map<Integer, Category> mapRet = template.query(sql, paramMap, new ResultSetExtractor<Map<Integer, Category>>() {
			@Override
			public Map<Integer, Category> extractData(ResultSet rs) throws SQLException, DataAccessException {
				HashMap<Integer, Category> mapRet = new HashMap<Integer, Category>();
				while (rs.next()) {
					Category category = new Category();
					Merchant m = new Merchant();
					m.setId(rs.getInt("merchant_id"));
					category.setId(rs.getInt("id"));
					category.setMerchant(m);
					category.setName(rs.getString("name"));
					category.setDescription(rs.getString("description"));
					category.setCreatedDate(rs.getTimestamp("created_date"));
					mapRet.put(rs.getInt("id"), category);
				}
				return mapRet;
			}
		});
		return mapRet;
	}

	public void createCategory(Category cat) {
		jdbcTemplate.update("insert into categories (merchant_id, name, description) values (?, ?, ?)",
				cat.getMerchant().getId(), cat.getName(), cat.getDescription());
	}

	public void updateCategory(Category cat) {
		jdbcTemplate.update("update categories set name = ?, description = ? where id = ?", cat.getName(),
				cat.getDescription(), cat.getId());
	}

	public void deleteCategory(int id, int mid) {
		jdbcTemplate.update("delete from categories  where merchant_id = ? AND id = ?", mid, id);
	}

	public Integer count() {
		Integer res = jdbcTemplate.queryForObject("select count(id) from categories", Integer.class);
		return res;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
