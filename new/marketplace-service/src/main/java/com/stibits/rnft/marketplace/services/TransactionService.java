package com.stibits.rnft.marketplace.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stibits.rnft.marketplace.api.TransactionDetails;
import com.stibits.rnft.marketplace.domain.Transaction;
import com.stibits.rnft.marketplace.repositories.TransactionRespository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRespository transactionRespository;

    public List<TransactionDetails> getTokenTransactions (String id, int limit, int offset) {
        return transactionRespository.getTransactionsOfToken(id, limit, offset)
            .stream()
            .map(transaction -> this.getTransactionDetails(transaction))
            .toList();
    }

    public TransactionDetails getTransactionDetails (Transaction transaction) {
        TransactionDetails transactionDetails = new TransactionDetails();

        transactionDetails.setId(transaction.getId());
        transactionDetails.setPrice(transaction.getPrice());
        transactionDetails.setTokenId(transaction.getToken().getId());
        transactionDetails.setToId(transaction.getToId());
        transactionDetails.setFromId(transaction.getFromId());
        transactionDetails.setCreatedDate(transaction.getCreatedDate());

        return transactionDetails;
    }
}
