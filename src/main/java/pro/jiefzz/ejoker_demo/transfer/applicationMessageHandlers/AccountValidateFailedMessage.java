package pro.jiefzz.ejoker_demo.transfer.applicationMessageHandlers;

import pro.jiefzz.ejoker.infrastructure.varieties.applicationMessage.AbstractApplicationMessage;

/**
 * 账户验证未通过
 * @author kimffy
 *
 */
public class AccountValidateFailedMessage extends AbstractApplicationMessage {

	private String accountId;
	
	private String transactionId;
	
	private String reason;

    public AccountValidateFailedMessage() {
    }
    
    public AccountValidateFailedMessage(String accountId, String transactionId, String reason) {
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.reason = reason;
    }

    @Override
    public String getRoutingKey()
    {
        return accountId;
    }

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
