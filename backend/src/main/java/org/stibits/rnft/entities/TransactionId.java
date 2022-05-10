package org.stibits.rnft.entities;

import java.io.Serializable;

public class TransactionId implements Serializable{
	protected String accountFrom;
	protected String accountTo;
	protected String tokenId;
	public String getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}
	public String getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(String accountTo) {
		this.accountTo = accountTo;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	  @Override
      public boolean equals(Object obj) {
                  if (this == obj)
                              return true;
                  if (obj == null) 
                              return false;
                  if (getClass() != obj.getClass())
                              return false;
                TransactionId other = (TransactionId) obj;
                  if (accountFrom != other.accountFrom)
                              return false; 
                  if (accountTo != other.accountTo)
                              return false;
                  if(tokenId != other.tokenId)
                	  return false;
                  return true;
      }
	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((this.accountFrom == null) ? 0 : this.accountFrom.hashCode());
	        result = prime * result + ((this.tokenId == null) ? 0 : this.tokenId.hashCode());
	        result = prime * result + ((this.accountTo == null) ? 0 : this.accountTo.hashCode());
	        return result;
	    }
	  

}
