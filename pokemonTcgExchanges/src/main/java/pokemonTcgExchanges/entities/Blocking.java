package pokemonTcgExchanges.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import pokemonTcgExchanges.jsonviews.JsonViews;

@Entity
@Table(name = "blocking")
public class Blocking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blocking_id")
	@JsonView(JsonViews.Simple.class)
	private long id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id") 
	@JsonView(JsonViews.Simple.class)
	private User user;
	@Column(name = "reports")
	@JsonView(JsonViews.Simple.class)
	private short reports;
	@Column(name = "is_blocked")
	@JsonView(JsonViews.Simple.class)
	private Boolean isBlocked;
	@Column(name = "asked_unblocking")
	@JsonView(JsonViews.Simple.class)
	private Boolean askedUnblocking;
	
	public Blocking() {
		super();
	}
	public Blocking(long id, User user, short reports, Boolean isBlocked, Boolean askedUnblocking) {
		super();
		this.id = id;
		this.user = user;
		this.reports = reports;
		this.isBlocked = isBlocked;
		this.askedUnblocking = askedUnblocking;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public short getReports() {
		return reports;
	}
	public void setReports(short reports) {
		this.reports = reports;
	}
	public Boolean getIsBlocked() {
		return isBlocked;
	}
	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	public Boolean getAskedUnblocking() {
		return askedUnblocking;
	}
	public void setAskedUnblocking(Boolean askedUnblocking) {
		this.askedUnblocking = askedUnblocking;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Blocking other = (Blocking) obj;
		return id == other.id;
	}
	
	

}
