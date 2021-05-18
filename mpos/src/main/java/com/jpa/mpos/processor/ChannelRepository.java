package com.jpa.mpos.processor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.jpa.mpos.data.Channel;
import com.jpa.mpos.data.Merchant;

@Component
@Repository
public class ChannelRepository {

	private JdbcTemplate jdbcTemplate;

	public Channel getChannelByID(int id, int mid) {
		try {
			Channel channel = this.jdbcTemplate.queryForObject(
					"SELECT id, merchant_id, name, description, fixed_fee, percentage_fee, created_date FROM channels WHERE merchant_id = ? AND id = ?;",
					new Object[] { mid, id }, new RowMapper<Channel>() {
						public Channel mapRow(ResultSet rs, int arg1) throws SQLException {
							Channel channel = new Channel();
							Merchant merchant = new Merchant();
							merchant.setId(rs.getInt("merchant_id"));
							channel.setCreatedDate(rs.getTimestamp("created_date"));
							channel.setDescription(rs.getString("description"));
							channel.setFixedFee(rs.getBigDecimal("fixed_fee"));
							channel.setId(rs.getInt("id"));
							channel.setMerchant(merchant);
							channel.setName(rs.getString("name"));
							channel.setPercentageFee(rs.getDouble("percentage_fee"));
							return channel;
						}
					});
			return channel;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void createCategory(int mid, String name, String description, BigDecimal fixed, BigDecimal percentage) {
		jdbcTemplate.update(
				"insert into channels (merchant_id, name, description, fixed_fee, percentage_fee) values (?, ?, ?, ?, ?)",
				mid, name, description, fixed, percentage);
	}

	public void updateChannel(int id) {
		jdbcTemplate.update(
				"update channels set name = ?, description = ?, fixed_fee = ?, percentage_fee = ? where id = ?", id);
	}

	public void deleteChannel(int id) {
		jdbcTemplate.update("delete from channels  where id = ?", id);
	}

	public Integer count() {
		Integer res = jdbcTemplate.queryForObject("select count(id) from channels", Integer.class);
		return res;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
