package com.lebupay.common;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.lebupay.model.TransactionModel;
/**
 * This Interface is used for Security.
 * @author Java-Team
 *
 */
public interface SaltTracker {

	ConcurrentHashMap<String, List<String>> SALT_TRACKER = new ConcurrentHashMap<String, List<String>>();
	TransactionModel transactionModelSaltTracker = new TransactionModel();

}
