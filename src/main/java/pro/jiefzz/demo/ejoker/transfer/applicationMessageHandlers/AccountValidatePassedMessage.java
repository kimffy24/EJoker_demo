package pro.jiefzz.demo.ejoker.transfer.applicationMessageHandlers;

import pro.jiefzz.ejoker.messaging.AbstractApplicationMessage;

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
