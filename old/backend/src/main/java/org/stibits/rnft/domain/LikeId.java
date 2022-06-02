package org.stibits.rnft.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class LikeId implements Serializable {
    protected String accountId;
    protected String tokenId;
}
