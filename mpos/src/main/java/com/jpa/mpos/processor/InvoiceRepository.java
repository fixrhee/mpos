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
import com.jpa.mpos.data.Channel;
import com.jpa.mpos.data.Invoice;
import com.jpa.mpos.data.Merchant;
import com.jpa.mpos.data.Terminal;

@Component
@Repository
public class InvoiceRepository {

	private JdbcTemplate jdbcTemplate;

	public Invoice getInvoiceByNo(String invoiceNo) {
		try {
			Invoice inv = this.jdbcTemplate.queryForObject(
					"SELECT id, merchant_id, terminal_id, channel_id, invoice_no, order_no, subtotal, tax, convenience_fee, total, payment_code, name, email, msisdn, address, status,  invoice_date, modified_date FROM invoices WHERE invoice_no = ?;",
					new Object[] { invoiceNo }, new RowMapper<Invoice>() {
						public Invoice mapRow(ResultSet rs, int arg1) throws SQLException {
							Invoice inv = new Invoice();
							Channel ch = new Channel();
							Merchant mr = new Merchant();
							Terminal t = new Terminal();
							t.setId(rs.getInt("terminal_id"));
							mr.setId(rs.getInt("merchant_id"));
							ch.setId(rs.getInt("channel_id"));
							inv.setAddress(rs.getString("address"));
							inv.setChannel(ch);
							inv.setConvenienceFee(rs.getBigDecimal("convenience_fee"));
							inv.setEmail(rs.getString("email"));
							inv.setId(rs.getInt("id"));
							inv.setInvoiceDate(rs.getTimestamp("invoice_date"));
							inv.setMerchant(mr);
							inv.setModifiedDate(rs.getTimestamp("modified_date"));
							inv.setMsisdn(rs.getString("msisdn"));
							inv.setName(rs.getString("name"));
							inv.setOrderNumber(rs.getString("order_no"));
							inv.setPaymentCode(rs.getString("payment_code"));
							inv.setStatus(rs.getString("status"));
							inv.setSubtotal(rs.getBigDecimal("subtotal"));
							inv.setTax(rs.getBigDecimal("tax"));
							inv.setTerminal(t);
							inv.setTotal(rs.getBigDecimal("total"));
							return inv;
						}
					});
			return inv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void createInvoice(Invoice inv) {
		jdbcTemplate.update(
				"insert into invoices (merchant_id, terminal_id, channel_id, order_no, invoice_no, subtotal, tax, convenience_fee, total, payment_code, name, email, msisdn, address) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				inv.getMerchant().getId(), inv.getTerminal().getId(), inv.getChannel().getId(), inv.getOrderNumber(),
				inv.getInvoiceNumber(), inv.getSubtotal(), inv.getTax(), inv.getConvenienceFee(), inv.getTotal(),
				inv.getPaymentCode(), inv.getName(), inv.getEmail(), inv.getMsisdn(), inv.getAddress());
	}

	public void updateInvoiceStatus(String invoiceNo, String status) {
		jdbcTemplate.update("update invoices set status = ? where invoice_no = ?", status, invoiceNo);
	}

	public Integer count() {
		Integer res = jdbcTemplate.queryForObject("select count(id) from invoices", Integer.class);
		return res;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
