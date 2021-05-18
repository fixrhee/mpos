package com.jpa.mpos.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.jpa.mpos.data.Merchant;

@Component
@Repository
public class MerchantRepository {

	private JdbcTemplate jdbcTemplate;

	public Merchant validateAccess(String username, String secret) {
		try {
			Merchant member = this.jdbcTemplate.queryForObject(
					"SELECT id, parent_username, username, name, email, msisdn, password, uid, active, created_date FROM merchants WHERE username = ? AND password = MD5(?);",
					new Object[] { username, secret }, new RowMapper<Merchant>() {
						public Merchant mapRow(ResultSet rs, int arg1) throws SQLException {
							Merchant merchant = new Merchant();
							merchant.setId(rs.getInt("id"));
							merchant.setName(rs.getString("name"));
							merchant.setEmail(rs.getString("email"));
							merchant.setUsername(rs.getString("username"));
							merchant.setPassword(rs.getString("password"));
							merchant.setActive(rs.getBoolean("active"));
							merchant.setCreatedDate(rs.getTimestamp("created_date"));
							merchant.setParentUsername(rs.getString("parent_username"));
							merchant.setUID(rs.getString("uid"));
							merchant.setMsisdn(rs.getString("msisdn"));
							return merchant;
						}
					});
			return member;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Merchant getMerchantByID(Object id) {
		try {
			Merchant member = this.jdbcTemplate.queryForObject(
					"SELECT id, parent_username, username, name, email, msisdn, password, uid, active, created_date FROM merchants WHERE id = ?;",
					new Object[] { id }, new RowMapper<Merchant>() {
						public Merchant mapRow(ResultSet rs, int arg1) throws SQLException {
							Merchant merchant = new Merchant();
							merchant.setId(rs.getInt("id"));
							merchant.setName(rs.getString("name"));
							merchant.setEmail(rs.getString("email"));
							merchant.setUsername(rs.getString("username"));
							merchant.setPassword(rs.getString("password"));
							merchant.setActive(rs.getBoolean("active"));
							merchant.setCreatedDate(rs.getTimestamp("created_date"));
							merchant.setParentUsername(rs.getString("parent_username"));
							merchant.setUID(rs.getString("uid"));
							merchant.setMsisdn(rs.getString("msisdn"));
							return merchant;
						}
					});
			return member;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Merchant getMerchantByUsername(String username) {
		try {
			Merchant member = this.jdbcTemplate.queryForObject(
					"SELECT id, parent_username, username, name, email, msisdn, password, uid, active, created_date FROM merchants WHERE username = ?;",
					new Object[] { username }, new RowMapper<Merchant>() {
						public Merchant mapRow(ResultSet rs, int arg1) throws SQLException {
							Merchant merchant = new Merchant();
							merchant.setId(rs.getInt("id"));
							merchant.setName(rs.getString("name"));
							merchant.setEmail(rs.getString("email"));
							merchant.setUsername(rs.getString("username"));
							merchant.setPassword(rs.getString("password"));
							merchant.setActive(rs.getBoolean("active"));
							merchant.setCreatedDate(rs.getTimestamp("created_date"));
							merchant.setParentUsername(rs.getString("parent_username"));
							merchant.setUID(rs.getString("uid"));
							merchant.setMsisdn(rs.getString("msisdn"));
							return merchant;
						}
					});
			return member;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void createMerchant(Merchant member) {
		jdbcTemplate.update(
				"insert into merchants (parent_username, username, name, msisdn, email, password) values (?, ?, ?, ?, ?, ?)",
				member.getParentUsername(), member.getUsername(), member.getName(), member.getMsisdn(),
				member.getEmail(), member.getPassword());
	}

	public void updateMerchants(String id, Merchant member) {
		jdbcTemplate.update(
				"update merchants set parent_username = ?, name = ?, email = ?, password = ?, uid = ?, active = ? where id = ?",
				member.getParentUsername(), member.getName(), member.getEmail(), member.getPassword(), member.getUID(),
				member.isActive(), id);
	}

	public void deleteMember(String id) {
		jdbcTemplate.update("delete from merchants  where id = ?", id);
	}

	public boolean isMerchantActive(Object mid) {
		Integer active = jdbcTemplate.queryForObject("SELECT active from merchants where id = ?", Integer.class, mid);
		if (active == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
