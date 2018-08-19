package com.lebupay.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lebupay.connection.OracleConnection;

/**
 * This interface is used to autowired database connection with oracle Database.
 * @author Java Team
 *
 */
@Repository
public class BaseDao {

	@Autowired
	protected OracleConnection oracleConnection;
}
