package pro.jiefzz.ejoker_demo.transfer.applicationMessageHandlers;

import pro.jiefzz.ejoker.infrastructure.varieties.applicationMessage.AbstractApplicationMessage;

/**
 * 账户验证已通过
 * @author kimffy
 *
 */
public class AccountValidatePassedMessage extends AbstractApplicationMessage {

	private String accountId;
	
	private String transactionId;

    public AccountValidatePassedMessage() {
    }
    
    public AccountValidatePassedMessage(String accountId, String transactionId) {
        this.accountId = accountId;
        this.transactionId = transactionId;
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
	
}
