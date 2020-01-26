package pro.jiefzz.demo.ejoker.transfer.domain.bankAccount;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.demo.ejoker.transfer.domain.TransactionType;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.AccountCreatedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.TransactionPreparationAddedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.TransactionPreparationCanceledEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.TransactionPreparationCommittedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.exceptions.InsufficientBalanceException;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.exceptions.TransactionPreparationNotExistException;
import pro.jiefzz.ejoker.common.context.annotation.assemblies.AggregateRoot;
import pro.jiefzz.ejoker.domain.AbstractAggregateRoot;

/**
 * 银行账户聚合根，封装银行账户余额变动的数据一致性
 * @author kimffy
 *
 */
@AggregateRoot
public class BankAccount extends AbstractAggregateRoot<String> {

	private final static Logger logger = LoggerFactory.getLogger(BankAccount.class);

	@SuppressWarnings("unused")
	private String owner;
	
	private double balance = 0d;
	
	private Map<String, TransactionPreparation> transactionPreparations;

	public BankAccount() {
	}
	
	public BankAccount(String accountId, String owner) {
		super(accountId);
		applyEvent(new AccountCreatedEvent(owner));
	}
	
	/**
	 * 添加一笔预操作
	 * @param transactionId
	 * @param transactionType
	 * @param preparationType
	 * @param amount
	 */
    public void addTransactionPreparation(String transactionId, TransactionType transactionType, PreparationType preparationType, double amount) 
    {
    	double availableBalance = getAvailableBalance();
        if (PreparationType.DebitPreparation.equals(preparationType) && availableBalance < amount)
        {
            throw new InsufficientBalanceException(getId(), transactionId, transactionType, amount, balance, availableBalance);
        }

        applyEvent(new TransactionPreparationAddedEvent(new TransactionPreparation(getId(), transactionId, transactionType, preparationType, amount)));
    }
    
    /**
     * 提交一笔预操作
     * @param transactionId
     */
    public void commitTransactionPreparation(String transactionId) 
    {
        TransactionPreparation transactionPreparation = getTransactionPreparation(transactionId);
        double currentBalance = balance;
        if (PreparationType.DebitPreparation.equals(transactionPreparation.getPreparationType())) {
            currentBalance -= transactionPreparation.getAmount();
        }
        else if (PreparationType.CreditPreparation.equals(transactionPreparation.getPreparationType())) {
            currentBalance += transactionPreparation.getAmount();
        }
        applyEvent(new TransactionPreparationCommittedEvent(currentBalance, transactionPreparation));
    }
    
    /**
     * 取消一笔预操作
     * @param transactionId
     */
    public void cancelTransactionPreparation(String transactionId) 
    {
        applyEvent(new TransactionPreparationCanceledEvent(getTransactionPreparation(transactionId)));
    }
    
	/**
	 * 获取当前账户内的一笔预操作，如果预操作不存在，则抛出异常
	 * @param transactionId
	 * @return
	 */
    private TransactionPreparation getTransactionPreparation(String transactionId) {
        if (null == transactionPreparations || 0 == transactionPreparations.size())
        {
        	logger.error("current info: {}",
        				null == transactionPreparations?
        						"null == transactionPreparations":"0 == transactionPreparations.size()"
        	);
            throw new TransactionPreparationNotExistException(getId(), transactionId);
        }
        TransactionPreparation transactionPreparation;
        if (null == (transactionPreparation = transactionPreparations.get(transactionId)))
        {
            throw new TransactionPreparationNotExistException(getId(), transactionId);
        }
        return transactionPreparation;
    }
    
    /**
     * 获取当前账户的可用余额，需要将已冻结的余额计算在内
     * @return
     */
    private double getAvailableBalance()
    {
    	if (null == transactionPreparations || 0 == transactionPreparations.size())
        {
            return balance;
        }

//        foreach (double debitTransactionPreparation in _transactionPreparations.Values.Where(x => x.PreparationType == PreparationType.DebitPreparation))
//        {
//            totalDebitTransactionPreparationAmount += debitTransactionPreparation.Amount;
//        }
    	
//    	ForEachUtil.processForEach(transactionPreparations, (transactionId, transaction) -> {
//		if(PreparationType.DebitPreparation.equals(transaction.getPreparationType()))
//			totalDebitTransactionPreparationAmount += transaction.getAmount();
//	});
    	
    	double totalDebitTransactionPreparationAmount = 0D;
    	Set<Entry<String, TransactionPreparation>> entrySet = transactionPreparations.entrySet();
    	for(Entry<String, TransactionPreparation> entry : entrySet) {
    		TransactionPreparation transaction = entry.getValue();
    		if(PreparationType.DebitPreparation.equals(transaction.getPreparationType()))
    			totalDebitTransactionPreparationAmount += transaction.getAmount();
    	}

        return balance - totalDebitTransactionPreparationAmount;
    }
    
	// =========== handler ===========

    @SuppressWarnings("unused")
	private void handle(AccountCreatedEvent evt) {
		this.owner = evt.getOwner();
		// 当snapshotES的时候 要注意 类型可能要丢哦~！
		transactionPreparations = new ConcurrentHashMap<>();
	}

    @SuppressWarnings("unused")
    private void handle(TransactionPreparationAddedEvent evnt) {
        transactionPreparations.put(evnt.getTransactionPreparation().getTransactionId(), evnt.getTransactionPreparation());
    }

    @SuppressWarnings("unused")
    private void handle(TransactionPreparationCommittedEvent evnt) {
        transactionPreparations.remove(evnt.getTransactionPreparation().getTransactionId());
        balance = evnt.getCurrentBalance();
    }

    @SuppressWarnings("unused")
    private void Handle(TransactionPreparationCanceledEvent evnt) {
        transactionPreparations.remove(evnt.getTransactionPreparation().getTransactionId());
    }

}
