/**
 * @formatter:off
 *
 */
package com.lebupay.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OraclePreparedStatement;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.lebupay.dao.TicketDAO;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.Status;
import com.lebupay.model.TicketModel;
import com.lebupay.model.TypeModel;

/**
 * This is TicketDaoImpl extends BaseDao and Implements TicketDAO Interface used to perform operation on Ticket Module.
 * @author Java Team
 *
 */
@Repository
public class TicketDaoImpl extends BaseDao implements TicketDAO{

	private static Logger logger = Logger.getLogger(TicketDaoImpl.class);
	
	/**
	 * This method is used for saving the ticket for both Merchant and Customer.
	 * @param ticketModel
	 * @param userType
	 * @param userId
	 * @return long
	 * @throws Exception
	 */
	public long saveTicket(TicketModel ticketModel, String userType, long userId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("saveTicket -- START");
		}
		
		long result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
				String sql = "insert into TICKET_MASTER (TICKET_ID,"
						+ "MESSAGE,"
						+ "TYPE_ID,"
						+ "STATUS,"
						+ "CREATED_DATE,"
						+ "CREATED_BY,"
						+ "SUBJECT) values(TICKET_MASTER_seq.nextval,:MESSAGE,(select TYPE_ID from type_master where type_name =:userType),:status,localtimestamp(0),:CREATED_BY,:SUBJECT) ";
			String pk[] = {"TICKET_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql,pk);
			pst.setStringAtName("MESSAGE", ticketModel.getTicketMessage()); // MESSAGE
			pst.setStringAtName("userType", userType); // User Type
			pst.setIntAtName("status", Status.ACTIVE.ordinal()); // status
			pst.setLongAtName("CREATED_BY", userId); // User ID
			pst.setStringAtName("SUBJECT", ticketModel.getSubject()); // Ticket Subject
			
			System.out.println("Save Ticket => "+sql);
			
			boolean result1 = pst.execute();
			if(!result1) {
				
				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
				System.out.println("Ticket GENERATED KEY => "+result);
				connection.commit();
				
			}
			
		} finally {
	
			try { // Closing Connection Object
				if (connection != null) {
	
					if (!connection.isClosed())
						connection.close();
	
					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for saveTicket");
					}
				}
			} catch (Exception e) {
	
				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for saveTicket"
							+ e.getMessage());
				}
	
			}
	
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("saveTicket -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to fetch all the ticket details for Customer, Merchant and Admin.
	 * @param userType
	 * @param userId
	 * @return List<TicketModel>
	 * @throws Exception
	 */
	public List<TicketModel> fetchAllTickets(String userType, long userId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTickets -- START");
		}
		
		List<TicketModel> ticketModels = new ArrayList<TicketModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			
			String sql = "";
			if(userType.equals("Customer")) {
				
				sql="select TICKET_ID,MESSAGE,REPLY,TYPE_ID,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,SUBJECT from TICKET_MASTER where TYPE_ID=(select TYPE_ID from type_master where type_name = '"+userType+"') and CREATED_BY=:CREATED_BY order by CREATED_DATE desc";
				
			} else if(userType.equals("Merchant")) {
				
				sql="select TICKET_ID,MESSAGE,REPLY,TYPE_ID,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,SUBJECT from TICKET_MASTER where TYPE_ID=(select TYPE_ID from type_master where type_name = '"+userType+"') and CREATED_BY=:CREATED_BY order by CREATED_DATE desc";
				
			} else {
				
				sql="select TICKET_ID,MESSAGE,REPLY,TYPE_ID,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,SUBJECT from TICKET_MASTER order by CREATED_DATE desc";
			}
			
			System.out.println("Fetch Tickets: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			if(!userType.equals("User")) {
				pst.setLongAtName("CREATED_BY",userId);
			}
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				TicketModel ticketModel = new TicketModel();
				TypeModel typeModel = new TypeModel();
				
				ticketModel.setTicketId(rs.getLong(1));
				ticketModel.setTicketMessage(rs.getString(2));
				ticketModel.setReply(rs.getString(3));
				
				typeModel.setTypeId(rs.getLong(4));
				typeModel.setTypeName(userType);
				ticketModel.setTypeModel(typeModel);
				if(rs.getInt(5) == 0)
					ticketModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(5) == 1)
					ticketModel.setStatus(Status.INACTIVE);
				else
					ticketModel.setStatus(Status.DELETE);
				
				ticketModel.setCreatedDate(rs.getString(6));
				ticketModel.setCreatedBy(rs.getLong(7));
				ticketModel.setModifiedDate(rs.getString(8));
				ticketModel.setModifiedBy(rs.getLong(9));
				ticketModel.setSubject(rs.getString(10));
				
				ticketModels.add(ticketModel);
			}
		}	
		
		finally {
	          try{
	           
	           if(pst != null)
	            if(!pst.isClosed())
	            	pst.close();
	           
	          }catch(Exception e){
	                 e.printStackTrace();
	          }
	
	          try{
	
	           if(connection != null)
	            if(!connection.isClosed())
	             connection.close();
	
	          }catch(Exception e){
	        	  e.printStackTrace();
	          }      
        }
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTickets -- END");
		}
		
		return ticketModels;
		
	}
	
	
	/**
	 * This method is used internally to generate the search query.
	 * @param dataTableModel
	 * @param isCount
	 * @return String
	 */
	private String getSearchQuery(DataTableModel dataTableModel, boolean isCount) {
		String[] searchColumns = {"MESSAGE", "REPLY", "SUBJECT"};
		
		String sql = "from TICKET_MASTER";
		if(searchColumns.length > 0){
			sql += " where (";
			for (int i = 0; i < searchColumns.length; i++) {
				if(i != 0){
					sql += " OR";
				}
				sql += " "+ searchColumns[i] +" like :searchstr";
			}
			sql += ")";
		}
		
		
		if(!isCount){
			sql = "select TICKET_ID,MESSAGE,REPLY,TYPE_ID,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,SUBJECT "+sql;
		}
		else{
			sql = "select COUNT(*)as total "+sql;
		}
		return sql;
	}
	
	/**
	 * This method is used to fetch all the ticket details according to the searching criteria for Customer, Merchant and Admin.
	 * @param userType
	 * @param userId
	 * @param dataTableModel
	 * @return List<TicketModel>
	 * @throws Exception
	 */
	public List<TicketModel> fetchAllTickets(String userType, long userId, DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTickets -- START");
		}
		
		List<TicketModel> ticketModels = new ArrayList<TicketModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQuery(dataTableModel, false);
			if(userType.equals("User")){
				sql += " order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";
			}else{
				sql += " AND CREATED_BY=:CREATED_BY order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";
			}
			
			System.out.println("Fetch Tickets: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			if(!userType.equals("User")){
				pst.setLongAtName("CREATED_BY",userId);
			}
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				TicketModel ticketModel = new TicketModel();
				TypeModel typeModel = new TypeModel();
				
				ticketModel.setTicketId(rs.getLong(1));
				ticketModel.setTicketMessage(rs.getString(2));
				ticketModel.setReply(rs.getString(3));
				
				typeModel.setTypeId(rs.getLong(4));
				typeModel.setTypeName(userType);
				ticketModel.setTypeModel(typeModel);
				if(rs.getInt(5) == 0)
					ticketModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(5) == 1)
					ticketModel.setStatus(Status.INACTIVE);
				else
					ticketModel.setStatus(Status.DELETE);
				
				ticketModel.setCreatedDate(rs.getString(6));
				ticketModel.setCreatedBy(rs.getLong(7));
				ticketModel.setModifiedDate(rs.getString(8));
				ticketModel.setModifiedBy(rs.getLong(9));
				ticketModel.setSubject(rs.getString(10));
				
				ticketModels.add(ticketModel);
			}
		}	
		
		finally {
	          try{
	           
	           if(pst != null)
	            if(!pst.isClosed())
	            	pst.close();
	           
	          }catch(Exception e){
	                 e.printStackTrace();
	          }
	
	          try{
	
	           if(connection != null)
	            if(!connection.isClosed())
	             connection.close();
	
	          }catch(Exception e){
	        	  e.printStackTrace();
	          }      
        }
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTickets -- END");
		}
		
		return ticketModels;
		
	}
	
	/**
	 * This method is used to count the total number of tickets in Database during AJAX Call.
	 * @param userType
	 * @param userId
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getTicketsCount(String userType, long userId, DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("getTicketsCount -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQuery(dataTableModel, true);
			if(!userType.equals("User")){
				sql += " AND CREATED_BY=:CREATED_BY";
			}
			
				
			System.out.println("Tickets Count: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			if(!userType.equals("User")){
				pst.setLongAtName("CREATED_BY",userId);
			}
			
			ResultSet rs =  pst.executeQuery();
			rs.next();
			result = rs.getInt(1);
		}	
		
		finally {
	          try{
	           
	           if(pst != null)
	            if(!pst.isClosed())
	            	pst.close();
	           
	          }catch(Exception e){
	                 e.printStackTrace();
	          }
	
	          try{
	
	           if(connection != null)
	            if(!connection.isClosed())
	             connection.close();
	
	          }catch(Exception e){
	        	  e.printStackTrace();
	          }      
        }
		
		if (logger.isInfoEnabled()) {
			logger.info("getTicketsCount -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is used during the export of the ticket list.
	 * @param userType
	 * @param userId
	 * @param dataTableModel
	 * @return List<TicketModel>
	 * @throws Exception
	 */
	public List<TicketModel> fetchAllTicketsForExport(String userType, long userId, DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTicketsForExport -- START");
		}
		
		List<TicketModel> ticketModels = new ArrayList<TicketModel>();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			String sql= getSearchQuery(dataTableModel, false);
			if(userType.equals("User")){
				sql += " order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy();
			}
			else{
				sql += " AND CREATED_BY=:CREATED_BY order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy();
			}
			
				
			System.out.println("Fetch Tickets For Export: "+sql);
			
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			if(!userType.equals("User")){
				pst.setLongAtName("CREATED_BY",userId);
			}
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				TicketModel ticketModel = new TicketModel();
				TypeModel typeModel = new TypeModel();
				
				ticketModel.setTicketId(rs.getLong(1));
				ticketModel.setTicketMessage(rs.getString(2));
				ticketModel.setReply(rs.getString(3));
				
				typeModel.setTypeId(rs.getLong(4));
				typeModel.setTypeName(userType);
				ticketModel.setTypeModel(typeModel);
				if(rs.getInt(5) == 0)
					ticketModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(5) == 1)
					ticketModel.setStatus(Status.INACTIVE);
				else
					ticketModel.setStatus(Status.DELETE);
				
				ticketModel.setCreatedDate(rs.getString(6));
				ticketModel.setCreatedBy(rs.getLong(7));
				ticketModel.setModifiedDate(rs.getString(8));
				ticketModel.setModifiedBy(rs.getLong(9));
				ticketModel.setSubject(rs.getString(10));
				
				ticketModels.add(ticketModel);
			}
		}	
		
		finally {
	          try{
	           
	           if(pst != null)
	            if(!pst.isClosed())
	            	pst.close();
	           
	          }catch(Exception e){
	                 e.printStackTrace();
	          }
	
	          try{
	
	           if(connection != null)
	            if(!connection.isClosed())
	             connection.close();
	
	          }catch(Exception e){
	        	  e.printStackTrace();
	          }      
        }
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTicketsForExport -- END");
		}
		
		return ticketModels;
		
	}
	
	/**
	 * This method is used for fetching the tickets by there ID.
	 * @param userType
	 * @param userId
	 * @param ticketId
	 * @return TicketModel
	 * @throws Exception
	 */
	public TicketModel fetchTicketById(String userType, int userId, int ticketId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTickets -- START");
		}
		
		TicketModel ticketModel = new TicketModel();
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
			
			String sql = "";
			if(userType.equals("Customer")) {
				
				sql="select TICKET_ID,MESSAGE,REPLY,TYPE_ID,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,SUBJECT from TICKET_MASTER where CREATED_BY=:CREATED_BY and TICKET_ID=:TICKET_ID";
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setIntAtName("CREATED_BY",userId);
				pst.setIntAtName("TICKET_ID",ticketId);
				
			} else if(userType.equals("Merchant")) {
				
				sql="select TICKET_ID,MESSAGE,REPLY,TYPE_ID,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,SUBJECT from TICKET_MASTER where CREATED_BY=:CREATED_BY and TICKET_ID=:TICKET_ID";
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setIntAtName("CREATED_BY",userId);
				pst.setIntAtName("TICKET_ID",ticketId);
				
			} else if((userType.equals("User")) & (userId == 0)) {
				
				sql="select TICKET_ID,MESSAGE,REPLY,TYPE_ID,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY,SUBJECT from TICKET_MASTER where TICKET_ID=:TICKET_ID";
				
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setIntAtName("TICKET_ID",ticketId);
			}
			
			
			System.out.println("Fetch Ticket By ID: "+sql);
			
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){
				
				TypeModel typeModel = new TypeModel();
				
				ticketModel.setTicketId(rs.getLong(1));
				ticketModel.setTicketMessage(rs.getString(2));
				ticketModel.setReply(rs.getString(3));
				
				typeModel.setTypeId(rs.getLong(4));
				typeModel.setTypeName(userType);
				ticketModel.setTypeModel(typeModel);
				if(rs.getInt(5) == 0)
					ticketModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(5) == 1)
					ticketModel.setStatus(Status.INACTIVE);
				else
					ticketModel.setStatus(Status.DELETE);
				
				ticketModel.setCreatedDate(rs.getString(6));
				ticketModel.setCreatedBy(rs.getLong(7));
				ticketModel.setModifiedDate(rs.getString(8));
				ticketModel.setModifiedBy(rs.getLong(9));
				ticketModel.setSubject(rs.getString(10));
				
				
			}
		}	
		
		finally {
	          try{
	           
	           if(pst != null)
	            if(!pst.isClosed())
	            	pst.close();
	           
	          }catch(Exception e){
	                 e.printStackTrace();
	          }
	
	          try{
	
	           if(connection != null)
	            if(!connection.isClosed())
	             connection.close();
	
	          }catch(Exception e){
	        	  e.printStackTrace();
	          }      
        }
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTickets -- END");
		}
		
		return ticketModel;
		
	}
	
	/**
	 * This method is used for the admin to reply any ticket.
	 * @param ticketModel
	 * @return int
	 * @throws Exception
	 */
	public int replyTicket(TicketModel ticketModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("replyTicket -- START");
		}
		
		int result = 0;
		
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		
		try {
				String sql = "update TICKET_MASTER set REPLY=:REPLY,MODIFIED_DATE=localtimestamp(0),MODIFIED_BY=:MODIFIED_BY where TICKET_ID=:TICKET_ID";
				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("REPLY", ticketModel.getReply()); // Ticket Reply
				pst.setLongAtName("MODIFIED_BY", ticketModel.getaModifiedBy()); // MODIFIED_BY ID
				pst.setLongAtName("TICKET_ID", ticketModel.getTicketId()); // Ticket ID
				
				System.out.println("Reply Ticket => "+sql);
				result = pst.executeUpdate();
				if (result > 0)
					result = 1; 
				connection.commit();
			
		} finally {
	
			try{
		           
		           if(pst != null)
		            if(!pst.isClosed())
		            	pst.close();
		           
		          } catch(Exception e){
		                 e.printStackTrace();
		          }
			
			try { // Closing Connection Object
				if (connection != null) {
	
					if (!connection.isClosed())
						connection.close();
	
					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for replyTicket");
					}
				}
			} catch (Exception e) {
	
				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for replyTicket"
							+ e.getMessage());
				}
	
			}
	
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("replyTicket -- END");
		}
		
		return result;
	}

}
