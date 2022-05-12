package org.stibits.rnft.controllers.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.converters.ProfileDetailsConverter;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.repositories.AccountDAO;
import org.stibits.rnft.repositories.TransactionDAO;

@RestController
@RequestMapping("/api/${api.version}/user/populare")
public class GetPopulareSellers {
    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private ProfileDetailsConverter profileDetailsConverter;

    @GetMapping
    public Map<String, Object> handleGetRequest () {
        Map<String, Object> response = new HashMap<>();
        List<Account> popularSellers = this.accountDAO.getBestSellers(5); 
        List<Map<String, Object>> data = profileDetailsConverter.convertAccountList(popularSellers);

        data.forEach(profile -> {
            String id = (String)profile.get("id");

            profile.put("totalTransactions", transactionDAO.getTransactionsSumOfAccount(id));
        });

        response.put("data", data);

        return response;
    }
}
