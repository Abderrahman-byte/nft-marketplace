package org.stibits.rnft.domain;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vladmihalcea.hibernate.type.json.JsonType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@TypeDefs({
	@TypeDef(name="json", typeClass = JsonType.class)
})
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    private String id;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "metadata", nullable = false)
    private Map<String, Object> metadata = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "event", nullable = false)
    private NotificationEvent event;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Calendar createdDate = Calendar.getInstance();

    @ManyToOne(targetEntity = Account.class, optional = false)
    @JoinColumn(name = "to_account")
    private Account to;

    @Column(name = "vued", nullable = false)
    private boolean vued = false;
}
