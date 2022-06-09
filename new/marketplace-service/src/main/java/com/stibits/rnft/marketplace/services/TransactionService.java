package com.stibits.rnft.marketplace.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.common.clients.GatewayClient;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.marketplace.api.TransactionDetails;
import com.stibits.rnft.marketplace.domain.Token;
import com.stibits.rnft.marketplace.domain.Transaction;
import com.stibits.rnft.marketplace.errors.TokenNotFoundError;
import com.stibits.rnft.marketplace.repositories.TokenRepository;
import com.stibits.rnft.marketplace.repositories.TransactionRespository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRespository transactionRespository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private GatewayClient gatewayClient;

    public Transaction createTransaction (String tokenId, String accountFrom, String accountTo, double price) throws ApiError {
        Token token = tokenRepository.selectById(tokenId);

        if (token == null) throw new TokenNotFoundError();

        return this.transactionRespository.insertTransaction(token, accountFrom, accountTo, price);
    }

    public List<TransactionDetails> getTokenTransactions (String id, int limit, int offset) {
        return transactionRespository.getTransactionsOfToken(id, limit, offset)
            .stream()
            .map(transaction -> this.getTransactionDetails(transaction))
            .toList();
    }

    public int getAccountTokenBalance (Token token, String accountId) {
        int balance = accountId.equals(token.getCreatorId()) ? token.getQuantity() : 0;

        List<Transaction> transactions = token.getTransactions().stream().sorted((a, b) -> {
                return a.getCreatedDate().compareTo(b.getCreatedDate());
            }).toList();

        for (Transaction transaction : transactions) {
                if (transaction.getFromId().equals(accountId)) balance--;
                if (transaction.getToId().equals(accountId)) balance++;
        }

        return balance;
    }

    // FIXME : this is only for single token
    public String getTokenOwner (Token token) {
        List<Transaction> transactions = token.getTransactions().stream().sorted(
                (a, b) -> a.getCreatedDate().compareTo(b.getCreatedDate())
        ).toList();

        if (transactions.isEmpty()) return token.getCreatorId();

        return transactions.get(transactions.size() - 1).getToId();
    }

    public TransactionDetails getTransactionDetails (Transaction transaction) {
        TransactionDetails transactionDetails = new TransactionDetails();
        ApiSuccessResponse<ProfileDetails> fromAccount = gatewayClient.getUserDetails(transaction.getFromId());
        ApiSuccessResponse<ProfileDetails> toAccount = gatewayClient.getUserDetails(transaction.getToId());

        transactionDetails.setId(transaction.getId());
        transactionDetails.setPrice(transaction.getPrice());
        transactionDetails.setTokenId(transaction.getToken().getId());
        transactionDetails.setTo(toAccount.getData());
        transactionDetails.setFrom(fromAccount.getData());
        transactionDetails.setCreatedDate(transaction.getCreatedDate());

        return transactionDetails;
    }
}
