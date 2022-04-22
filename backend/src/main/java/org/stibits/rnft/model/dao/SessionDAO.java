package org.stibits.rnft.model.dao;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.stibits.rnft.model.bo.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// TODO : Delete Expired sessions

@Repository
public class SessionDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
    public void insertSession (Session s) {
        entityManager.persist(s);
    }
    
    public Session getSessionById (String sid) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Session> cq = cb.createQuery(Session.class);
        Root<Session> root = cq.from(Session.class);

        cq.select(root).where(
            cb.and(
                cb.equal(root.get("id"), sid),
                cb.greaterThan(root.get("expires"), Calendar.getInstance())
            )
        );

        try {
            return (Session)entityManager.createQuery(cq).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Transactional
    public boolean deleteSession (String sid) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Session> cd = cb.createCriteriaDelete(Session.class);
        Root<Session> root = cd.from(Session.class);

        cd.where(
            cb.equal(root.get("id"), sid)
        );

        return entityManager.createQuery(cd).executeUpdate() > 0;
    }
}
