package connection;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

public class IfpTransObj  {
	
	public PlatformTransactionManager  txManager;    
    public TransactionStatus status;

	public IfpTransObj(PlatformTransactionManager txManager,TransactionStatus status) {		 
		this.txManager = txManager;
		this.status = status;
	} 

}
