package com.jpa.mpos.processor;

import com.jpa.mpos.data.ServiceResponse;
import com.jpa.mpos.data.Status;

public abstract class ResponseBuilder {

	public static ServiceResponse getStatus(String arg, Object payload) {
		return ResponseBuilder.getStatus(Status.valueOf(arg), payload);
	}

	public static ServiceResponse getStatus(Status arg, Object payload) {
		switch (arg) {
		case PROCESSED:
			return new ServiceResponse(arg.toString(), "200", "Transaction Succesfull", payload);
		case INVALID_PARAMETER:
			return new ServiceResponse(arg.toString(), "404", "Incomplete Request Parameter", payload);
		case PRODUCT_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "The Specified Product Not Found", payload);
		case MEMBER_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "The Specified Member Not Found On System", payload);
		case CATEGORY_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "Category Not Found For Specified Merchant ID", payload);
		case TERMINAL_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "Terminal Not Found", payload);
		case ORDER_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "Order Not Found", payload);
		case CHANNEL_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "Channel Not Found", payload);
		case UNAUTHORIZED_ACCESS:
			return new ServiceResponse(arg.toString(), "403", "Invalid Token Signature", payload);
		case BLOCKED:
			return new ServiceResponse(arg.toString(), "403",
					"Your Username has been Blocked, Please Contact Administrator", payload);
		case ACCESS_DENIED:
			return new ServiceResponse(arg.toString(), "403", "You don't have Permission to Access this WebService",
					payload);
		case DUPLICATE_TRACENO:
			return new ServiceResponse(arg.toString(), "409", "The specified Trace Number has Already Being Used",
					payload);
		case QUOTA_EXCEEDED:
			return new ServiceResponse(arg.toString(), "419", "Voucher Registration Quota Exceeded", payload);
		case INVALID_DATE:
			return new ServiceResponse(arg.toString(), "403", "Invalid Date Format", payload);
		default:
			return new ServiceResponse("UNKNOWN_ERROR", "500", "Transaction Failed, Please Contact Administrator",
					payload);
		}
	}
}
