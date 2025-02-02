package pokemonTcgExchanges.entities;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import pokemonTcgExchanges.jsonviews.JsonViews;

@Entity
@Table(name = "exchanges")
public class Exchange {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "exchange_id")
	@JsonView(JsonViews.Simple.class)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "id_card1_exchange", foreignKey = @ForeignKey(name = "id_card1_exchange_fk"))
	@JsonView(JsonViews.Exchange.class)
	private Card card1;
	@ManyToOne
	@JoinColumn(name = "id_card2_exchange", foreignKey = @ForeignKey(name = "id_card2_exchange_fk"))
	@JsonView(JsonViews.Exchange.class)
	private Card card2;
	@ManyToOne
	@JoinColumn(name = "id_user1_exchange", foreignKey = @ForeignKey(name = "id_user1_exchange_fk"))
	@JsonView(JsonViews.Exchange.class)
	private User user1;
	@ManyToOne
	@JoinColumn(name = "id_user2_exchange", foreignKey = @ForeignKey(name = "id_user2_exchange_fk"))
	@JsonView(JsonViews.Exchange.class)
	private User user2;
	@Column(name = "date", nullable = false)
	@JsonView(JsonViews.Simple.class)
	private LocalDate date;

	public Exchange() {
		super();
	}

	public Exchange(Long id, Card card1, Card card2, User user1, User user2, LocalDate date) {
		super();
		this.id = id;
		this.card1 = card1;
		this.card2 = card2;
		this.user1 = user1;
		this.user2 = user2;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Card getCard1() {
		return card1;
	}

	public void setCard1(Card card1) {
		this.card1 = card1;
	}

	public Card getCard2() {
		return card2;
	}

	public void setCard2(Card card2) {
		this.card2 = card2;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
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
		Exchange other = (Exchange) obj;
		return Objects.equals(id, other.id);
	}

}
