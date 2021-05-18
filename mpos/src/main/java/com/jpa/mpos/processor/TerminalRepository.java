package com.jpa.mpos.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.jpa.mpos.data.Terminal;

@Component
@Repository
public class TerminalRepository {

	private JdbcTemplate jdbcTemplate;

	public Terminal validateAccess(String username, String secret) {
		try {
			Terminal terminal = this.jdbcTemplate.queryForObject(
					"SELECT id, terminal_ref_id, merchant_id, nns, uid, username, password, name, address, msisdn, active, created_date, modified_date FROM terminals WHERE username = ? AND password = MD5(?);",
					new Object[] { username, secret }, new RowMapper<Terminal>() {
						public Terminal mapRow(ResultSet rs, int arg1) throws SQLException {
							Terminal terminal = new Terminal();
							terminal.setId(rs.getInt("id"));
							terminal.setTerminalRefID(rs.getInt("terminal_ref_id"));
							terminal.setMerchantID(rs.getInt("merchant_id"));
							terminal.setNnsID(rs.getString("nns"));
							terminal.setUID(rs.getString("uid"));
							terminal.setUsername(rs.getString("username"));
							terminal.setPassword(rs.getString("password"));
							terminal.setName(rs.getString("name"));
							terminal.setAddress(rs.getString("address"));
							terminal.setMsisdn(rs.getString("msisdn"));
							terminal.setActive(rs.getBoolean("active"));
							terminal.setCreatedDate(rs.getTimestamp("created_date"));
							terminal.setModifiedDate(rs.getTimestamp("modified_date"));
							return terminal;
						}
					});
			return terminal;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Terminal getTerminalByID(int id, int mid) {
		try {
			Terminal terminal = this.jdbcTemplate.queryForObject(
					"SELECT id, terminal_ref_id, merchant_id, nns, uid, username, password, name, address, msisdn, active, created_date, modified_date FROM terminals WHERE merchant_id = ? AND id = ?;",
					new Object[] { mid, id }, new RowMapper<Terminal>() {
						public Terminal mapRow(ResultSet rs, int arg1) throws SQLException {
							Terminal terminal = new Terminal();
							terminal.setId(rs.getInt("id"));
							terminal.setTerminalRefID(rs.getInt("terminal_ref_id"));
							terminal.setMerchantID(rs.getInt("merchant_id"));
							terminal.setNnsID(rs.getString("nns"));
							terminal.setUID(rs.getString("uid"));
							terminal.setUsername(rs.getString("username"));
							terminal.setPassword(rs.getString("password"));
							terminal.setName(rs.getString("name"));
							terminal.setAddress(rs.getString("address"));
							terminal.setMsisdn(rs.getString("msisdn"));
							terminal.setActive(rs.getBoolean("active"));
							terminal.setCreatedDate(rs.getTimestamp("created_date"));
							terminal.setModifiedDate(rs.getTimestamp("modified_date"));
							return terminal;
						}
					});
			return terminal;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Terminal getTerminalByUsername(String username) {
		try {
			Terminal terminal = this.jdbcTemplate.queryForObject(
					"SELECT id, terminal_ref_id, merchant_id, nns, uid, username, password, name, address, msisdn, active, created_date, modified_date FROM terminals WHERE username = ?;",
					new Object[] { username }, new RowMapper<Terminal>() {
						public Terminal mapRow(ResultSet rs, int arg1) throws SQLException {
							Terminal terminal = new Terminal();
							terminal.setId(rs.getInt("id"));
							terminal.setTerminalRefID(rs.getInt("terminal_ref_id"));
							terminal.setMerchantID(rs.getInt("merchant_id"));
							terminal.setNnsID(rs.getString("nns"));
							terminal.setUID(rs.getString("uid"));
							terminal.setUsername(rs.getString("username"));
							terminal.setPassword(rs.getString("password"));
							terminal.setName(rs.getString("name"));
							terminal.setAddress(rs.getString("address"));
							terminal.setMsisdn(rs.getString("msisdn"));
							terminal.setActive(rs.getBoolean("active"));
							terminal.setCreatedDate(rs.getTimestamp("created_date"));
							terminal.setModifiedDate(rs.getTimestamp("modified_date"));
							return terminal;
						}
					});
			return terminal;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Terminal> getTerminalByMerchant(Object mid) {
		try {
			List<Terminal> terminal = this.jdbcTemplate.query(
					"SELECT id, terminal_ref_id, merchant_id, nns, uid, username, password, name, address, msisdn, active, created_date, modified_date FROM terminals WHERE merchant_id = ?;",
					new Object[] { mid }, new RowMapper<Terminal>() {
						public Terminal mapRow(ResultSet rs, int arg1) throws SQLException {
							Terminal terminal = new Terminal();
							terminal.setId(rs.getInt("id"));
							terminal.setTerminalRefID(rs.getInt("terminal_ref_id"));
							terminal.setMerchantID(rs.getInt("merchant_id"));
							terminal.setNnsID(rs.getString("nns"));
							terminal.setUID(rs.getString("uid"));
							terminal.setUsername(rs.getString("username"));
							terminal.setPassword(rs.getString("password"));
							terminal.setName(rs.getString("name"));
							terminal.setAddress(rs.getString("address"));
							terminal.setMsisdn(rs.getString("msisdn"));
							terminal.setActive(rs.getBoolean("active"));
							terminal.setCreatedDate(rs.getTimestamp("created_date"));
							terminal.setModifiedDate(rs.getTimestamp("modified_date"));
							return terminal;
						}
					});
			return terminal;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void createTerminal(Terminal terminal) {
		jdbcTemplate.update(
				"insert into terminals (terminal_ref_id, merchant_id, nns, username, password, name, address, msisdn) values (?, ?, ?, ?, ?, ?, ?, ?)",
				terminal.getTerminalRefID(), terminal.getMerchantID(), terminal.getNnsID(), terminal.getUsername(),
				DigestUtils.md5Hex(terminal.getPassword()), terminal.getName(), terminal.getAddress(),
				terminal.getMsisdn());
	}

	public void updateTerminal(Object id, Terminal terminal) {
		jdbcTemplate.update(
				"update terminals set terminal_ref_id = ?, merchant_id = ?, nns = ?, username = ?, password = ?, name = ?, address = ?, msisdn = ? where id = ?",
				terminal.getTerminalRefID(), terminal.getMerchantID(), terminal.getNnsID(), terminal.getUsername(),
				DigestUtils.md5Hex(terminal.getPassword()), terminal.getName(), terminal.getAddress(),
				terminal.getMsisdn(), id);
	}

	public void updateTerminalUID(Object id, String uid) {
		jdbcTemplate.update("update terminals set uid = ? where id = ?", uid, id);
	}

	public void deleteTerminal(Object id, int mid) {
		jdbcTemplate.update("delete from terminals  where merchant_id = ? AND id = ?", mid, id);
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
