package pokemonTcgExchanges.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "canceld_exchange")
public class CanceldExchange {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "canceld_exchange_id")
	@JsonView(JsonViews.Simple.class)
	private Long id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exchange_id") 
	@JsonView(JsonViews.Simple.class)
	private Exchange exchange;
	@Column(name = "cause")
	@Enumerated(EnumType.ORDINAL)
	@JsonView(JsonViews.Simple.class)
	private Cause cause;
	
	public CanceldExchange() {
		super();
	}
	public CanceldExchange(Long id, Exchange exchange, Cause cause) {
		super();
		this.id = id;
		this.exchange = exchange;
		this.cause = cause;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Exchange getExchange() {
		return exchange;
	}
	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}
	public Cause getCause() {
		return cause;
	}
	public void setCause(Cause cause) {
		this.cause = cause;
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
		CanceldExchange other = (CanceldExchange) obj;
		return Objects.equals(id, other.id);
	}
	
	

}
