package org.stibits.rnft.repositories;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.stibits.rnft.entities.Bid;
import org.stibits.rnft.entities.Notification;
import org.stibits.rnft.entities.NotificationEvent;
import org.stibits.rnft.entities.Transaction;
import org.stibits.rnft.notifications.converters.BidCreatedNotification;
import org.stibits.rnft.notifications.converters.BidResponseNotification;
import org.stibits.rnft.notifications.converters.SoldNotification;
import org.stibits.rnft.utils.RandomGenerator;

@Repository
public class NotificationDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SoldNotification soldNotificationConverter;

    @Autowired
    private BidCreatedNotification bidCreatedNotification;

    @Autowired
    private BidResponseNotification bidResponseNotification;

    @Autowired
    private RandomGenerator randomGenerator;

    @SuppressWarnings("unchecked")
    public List<Notification> selectLatestNotifications (String id) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        Root<Notification> root = cq.from(Notification.class);

        cq.select(root)
        .where(
            cb.and(
                cb.equal(root.get("to").get("id"), id), 
                cb.equal(root.get("vued"), false))
        ).orderBy(
            cb.desc(root.get("createdDate"))
        );

        Query query = this.entityManager.createQuery(cq);

        return (List<Notification>)query.getResultList();
    }

    @Transactional
    public boolean updateVuedNotifications (String id, Calendar lastSeen) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaUpdate<Notification> cu = cb.createCriteriaUpdate(Notification.class);
        Root<Notification> root = cu.from(Notification.class);

        cu.set(root.get("vued"), true).where(
            cb.and(
                cb.equal(root.get("to").get("id"), id), 
                cb.lessThanOrEqualTo(root.get("createdDate"), lastSeen)
            )
        );

        Query query = this.entityManager.createQuery(cu);

        return query.executeUpdate() > 0;
    }

    @Transactional
    public Notification insertNotification (Transaction transaction) {
        Notification notification = new Notification();

        notification.setId(randomGenerator.generateRandomStr(25));
        notification.setEvent(NotificationEvent.SOLD);
        notification.setTo(transaction.getFrom());
        notification.setMetadata(soldNotificationConverter.convert(transaction));

        this.entityManager.persist(notification);
        
        return notification;
    }
    
    @Transactional
    public Notification insertNotification (Bid bid) {
        Notification notification = new Notification();
        
        notification.setId(randomGenerator.generateRandomStr(25));
        notification.setEvent(NotificationEvent.BID_CREATED);
        notification.setTo(bid.getTo());
        notification.setMetadata(bidCreatedNotification.convert(bid));

        this.entityManager.persist(notification);

        return notification;
    }

    @Transactional
    public Notification insertNotification (Bid bid, boolean accepted) {
        Notification notification = new Notification();

        notification.setId(randomGenerator.generateRandomStr(25));
        notification.setEvent(accepted ? NotificationEvent.BID_ACCEPTED : NotificationEvent.BID_REJECT);
        notification.setTo(bid.getFrom());
        notification.setMetadata(bidResponseNotification.convert(bid));

        this.entityManager.persist(notification);

        return notification;
    }
}
