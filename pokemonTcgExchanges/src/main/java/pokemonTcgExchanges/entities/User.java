package pokemonTcgExchanges.entities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonView;

import pokemonTcgExchanges.jsonviews.JsonViews;

@Entity
@Table(name = "user")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	@JsonView(JsonViews.Simple.class)
	private Long id;
	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@JsonView(JsonViews.Simple.class)
	private Role role;
	@Column(name = "login", nullable = false, unique = true)
	@JsonView(JsonViews.Simple.class)
	private String login;
	@Column(name = "password", length = 255, nullable = false)
	private String password;
	@Column(name = "friendCode", nullable = true, unique = true)
	@JsonView(JsonViews.Simple.class)
	private String friendCode;
	@OneToMany(mappedBy = "user1")
	@JsonView(JsonViews.UserWithAll.class)
	private Set<Exchange> exchanges1;
	@OneToMany(mappedBy = "user2")
	@JsonView(JsonViews.UserWithAll.class)
	private Set<Exchange> exchanges2;
	@ManyToMany
	@JoinTable(
	    name = "user_wish_list", 
	    joinColumns = @JoinColumn(name = "user_id"), 
	    inverseJoinColumns = @JoinColumn(name = "wish_list_card_id")
	)
	@JsonView(JsonViews.UserWithAll.class)
	private Set<Card> wishList;
	@ManyToMany
	@JoinTable(
	    name = "user_give_list", 
	    joinColumns = @JoinColumn(name = "user_id"), 
	    inverseJoinColumns = @JoinColumn(name = "give_list_card_id")
	)
	@JsonView(JsonViews.UserWithAll.class)
	private Set<Card> toGiveList;

	public User() {
		super();
	}

	public User(Long id, Role role, String login, String password, String friendCode, Set<Exchange> exchanges1,
			Set<Exchange> exchanges2, Set<Card> wishList, Set<Card> toGiveList) {
		super();
		this.id = id;
		this.role = role;
		this.login = login;
		this.password = password;
		this.friendCode = friendCode;
		this.exchanges1 = exchanges1;
		this.exchanges2 = exchanges2;
		this.wishList = wishList;
		this.toGiveList = toGiveList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFriendCode() {
		return friendCode;
	}

	public void setFriendCode(String friendCode) {
		this.friendCode = friendCode;
	}

	public Set<Exchange> getExchanges1() {
		return exchanges1;
	}

	public void setExchanges1(Set<Exchange> exchanges1) {
		this.exchanges1 = exchanges1;
	}

	public Set<Exchange> getExchanges2() {
		return exchanges2;
	}

	public void setExchanges2(Set<Exchange> exchanges2) {
		this.exchanges2 = exchanges2;
	}

	public Set<Card> getWishList() {
		return wishList;
	}

	public void setWishList(Set<Card> wishList) {
		this.wishList = wishList;
	}

	public Set<Card> getToGiveList() {
		return toGiveList;
	}

	public void setToGiveList(Set<Card> toGiveList) {
		this.toGiveList = toGiveList;
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
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// new SimpleGrantedAuthority("ROLE_"+this.getClass().getSimpleName());
		return Arrays.asList(new SimpleGrantedAuthority(role.toString()));
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return login;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
