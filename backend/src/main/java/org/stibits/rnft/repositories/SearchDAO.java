package org.stibits.rnft.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.stibits.rnft.domain.Account;
import org.stibits.rnft.domain.NftCollection;
import org.stibits.rnft.domain.Token;

@Repository
public class SearchDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Token> searchForToken (String query, int limit, int offset) {
        String qlString = "SELECT * FROM token WHERE title REGEXP :query LIMIT :limit OFFSET :offset";
        Query sqlQuery = this.entityManager.createNativeQuery(qlString, Token.class);

        sqlQuery.setParameter("query", query);
        sqlQuery.setParameter("limit", limit);
        sqlQuery.setParameter("offset", offset);

        return (List<Token>)sqlQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<NftCollection> searchForCollections (String query, int limit, int offset) {
        String qlString = "SELECT * FROM nft_collection WHERE name REGEXP :query LIMIT :limit OFFSET :offset";
        Query sqlQuery = this.entityManager.createNativeQuery(qlString, NftCollection.class);

        sqlQuery.setParameter("query", query);
        sqlQuery.setParameter("limit", limit);
        sqlQuery.setParameter("offset", offset);

        return (List<NftCollection>)sqlQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Account> searchForAccount (String query, int limit, int offset) {
        String qlString = "select a.id, a.username, a.email, a.is_verified, a.created_date, a.updated_date, a.is_admin, a.password " +
        "from account a join account_profile p on a.id = p.account_id " +
        "where (a.username REGEXP :query OR p.display_name REGEXP :query) AND a.is_verified = true " +
        "limit :limit offset :offset";
        Query sqlQuery = this.entityManager.createNativeQuery(qlString, Account.class);

        sqlQuery.setParameter("query", query);
        sqlQuery.setParameter("limit", limit);
        sqlQuery.setParameter("offset", offset);

        return (List<Account>)sqlQuery.getResultList();
    }
}
