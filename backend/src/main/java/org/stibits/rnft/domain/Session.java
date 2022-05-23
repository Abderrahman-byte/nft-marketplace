package org.stibits.rnft.domain;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.vladmihalcea.hibernate.type.json.JsonType;

@Entity
@Table(name="session")
@TypeDefs({
	@TypeDef(name="json", typeClass = JsonType.class)
})
@Setter
@NoArgsConstructor
@Getter
public class Session {
	@Id
	@Column(name = "id")
	private String sid;
	
	@Type(type="json")
	@Column(columnDefinition="json")
	private Map<String, Object> payload = new HashMap<>();
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Calendar expires = Calendar.getInstance();
	
	public long getMaxAge () {
		if (this.expires == null) return 0;

		return (this.expires.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000;
	}
}
