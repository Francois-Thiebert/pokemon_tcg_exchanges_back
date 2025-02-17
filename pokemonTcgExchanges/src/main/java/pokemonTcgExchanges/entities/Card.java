package pokemonTcgExchanges.entities;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import pokemonTcgExchanges.jsonviews.JsonViews;

@Entity
@Table(name = "card")
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "card_id")
	@JsonView(JsonViews.Simple.class)
	private Long id;
	@Column(name = "name")
	@JsonView(JsonViews.Simple.class)
	private String name;
	@Column(name = "rarity")
	@JsonView(JsonViews.Simple.class)
	private Integer rarity;
	@Column(name = "collection")
	@JsonView(JsonViews.Simple.class)
	private int collection;
	@Column(name = "serial_number")
	@JsonView(JsonViews.Simple.class)
	private String serialNumber;
	@Column(name = "type")
	@Enumerated(EnumType.ORDINAL)
	@JsonView(JsonViews.Simple.class)
	private Type type;
	@Column(name = "picture")
	@JsonView(JsonViews.Card.class)
	private String picture;
	@ManyToMany(mappedBy = "wishList")
	@JsonView(JsonViews.Card.class)
	private Set<User> wisher;
	@ManyToMany(mappedBy = "toGiveList")
	@JsonView(JsonViews.Card.class)
	private Set<User> giver;

	public Card() {
		super();
	}

	public Card(Long id, String name, Integer rarity, int collection, String serialNumber, Type type, String picture,
			Set<User> wisher, Set<User> giver) {
		super();
		this.id = id;
		this.name = name;
		this.rarity = rarity;
		this.collection = collection;
		this.serialNumber = serialNumber;
		this.type = type;
		this.picture = picture;
		this.wisher = wisher;
		this.giver = giver;
	}

	public Set<User> getGiver() {
		return giver;
	}

	public void setGiver(Set<User> giver) {
		this.giver = giver;
	}

	public Set<User> getWisher() {
		return wisher;
	}

	public void setWisher(Set<User> wisher) {
		this.wisher = wisher;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(Integer rarity) {
		this.rarity = rarity;
	}

	public Integer getCollection() {
		return collection;
	}

	public void setCollection(int collection) {
		this.collection = collection;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
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
		Card other = (Card) obj;
		return Objects.equals(id, other.id);
	}

}
