package org.stibits.rnft.entities;

import java.io.Serializable;

public class LikeId implements Serializable {
    protected String accountId;
    protected String tokenId;
    
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj == this) return true;

        if (!obj.getClass().equals(this.getClass())) return false;

        LikeId target = (LikeId)obj;

        return target.accountId.equals(this.accountId) && target.tokenId.equals(this.tokenId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.accountId == null) ? 0 : this.accountId.hashCode());
        result = prime * result + ((this.tokenId == null) ? 0 : this.tokenId.hashCode());
        return result;
    }
}
