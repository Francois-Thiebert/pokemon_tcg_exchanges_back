package pokemonTcgExchanges.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pokemonTcgExchanges.entities.CanceldExchange;
import pokemonTcgExchanges.entities.Card;
import pokemonTcgExchanges.entities.Cause;
import pokemonTcgExchanges.entities.Exchange;
import pokemonTcgExchanges.entities.State;
import pokemonTcgExchanges.entities.User;
import pokemonTcgExchanges.exceptions.ExchangeException;
import pokemonTcgExchanges.repositories.CanceldExchangeRepository;
import pokemonTcgExchanges.repositories.ExchangeRepository;

@Service
public class ExchangeService {

	@Autowired
	private Validator validator;
	@Autowired
	private ExchangeRepository exchangeRepo;
	@Autowired
	private CanceldExchangeRepository cancelRepo;
	@Autowired
	private UserService userSrv;
	@Autowired
	private CardService cardSrv;

	public List<Exchange> getAll() {
		return exchangeRepo.findAll();
	}

	public Exchange create(Exchange exchange) {
		Card card1 = cardSrv.getById(exchange.getCard1().getId());
		Card card2 = cardSrv.getById(exchange.getCard2().getId());
		User user1 = userSrv.getById(exchange.getUser1().getId());
		User user2 = userSrv.getById(exchange.getUser2().getId());
		Set<Card> wishUser1 = user1.getWishList();
		Set<Card> wishUser2 = user2.getWishList();
		Set<Card> giveUser1 = user1.getToGiveList();
		Set<Card> giveUser2 = user2.getToGiveList();
		Long numberExchangesUser2 = exchangeRepo.countCurrentExchangeByUser(user2.getId());
		Long numberExchangesUser1 = exchangeRepo.countCurrentExchangeByUser(user1.getId());
		wishUser1.remove(card1);
		user1.setWishList(wishUser1);
		giveUser1.remove(card2);
		user1.setToGiveList(giveUser1);
		if(numberExchangesUser1 > 5) {
			user1.setIsVisible(false);
		}
	    userSrv.update(user1);
	    wishUser2.remove(card2);
		user2.setWishList(wishUser2);
		giveUser2.remove(card1);
		user2.setToGiveList(giveUser2);
		if(numberExchangesUser2 > 5) {
			user2.setIsVisible(false);
		}
	    userSrv.update(user2);
	    exchange.setState(State.ASKED);
	    exchange.setDate(LocalDateTime.now());
		Set<ConstraintViolation<Exchange>> violations = validator.validate(exchange);
		if (violations.isEmpty()) {
			exchangeRepo.save(exchange);
			return exchange;
		} else {
			throw new ExchangeException();
		}
	}

	public Exchange getById(Long id) {
		return exchangeRepo.findById(id).orElseThrow(ExchangeException::new);
	}

	public List<Exchange> getByCard(Long id) {
		Card card = new Card();
		card.setId(id);
		List<Exchange> exchanges = new ArrayList<>();
		exchanges = exchangeRepo.findExchangesByCard(card.getId());
		return exchanges;
	}

	public List<Exchange> getByUserId(Long id) {
		User user = new User();
		user.setId(id);
		List<Exchange> exchanges = new ArrayList<>();
		List<Exchange> exchangesUser1 = new ArrayList<>();
		List<Exchange> exchangesUser2 = new ArrayList<>();
		exchangesUser1 = exchangeRepo.findByUser1(user);
		exchangesUser2 = exchangeRepo.findByUser2(user);
		exchangesUser1.forEach((m) -> exchanges.add(m));
		exchangesUser2.forEach((m) -> exchanges.add(m));
		return exchanges;
	}

	public List<Exchange> getAllExchangeUser1(Long id) {
		User user = new User();
		user.setId(id);
		List<Exchange> exchangesUser1 = new ArrayList<>();
		exchangesUser1 = exchangeRepo.findByUser1(user);
		return exchangesUser1;
	}

	public List<Exchange> getAllExchangeUser2(Long id) {
		User user = new User();
		user.setId(id);
		List<Exchange> exchangesUser2 = new ArrayList<>();
		exchangesUser2 = exchangeRepo.findByUser2(user);
		return exchangesUser2;
	}

	public List<Exchange> getExchangeByUsersId(Long idUser1, Long idUser2) {
		List<Exchange> exchanges = new ArrayList<>();
		exchanges = exchangeRepo.findExchangesByUserIds(idUser1, idUser2);
		return exchanges;
	}
	
	public Long getCurrentExchangeNumberByUser(User user) {
		return exchangeRepo.countCurrentExchangeByUser(user.getId());
	}

	public Exchange update(Exchange exchange) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Exchange>> violations = validator.validate(exchange);
		if (violations.isEmpty()) {
			Exchange exchangeEnBase = getById(exchange.getId());
			exchangeEnBase.setUser1(exchange.getUser1());
			exchangeEnBase.setUser2(exchange.getUser2());
			exchangeEnBase.setCard1(exchange.getCard1());
			exchangeEnBase.setCard2(exchange.getCard2());
			exchangeEnBase.setDate(exchange.getDate());
			exchangeEnBase.setState(exchange.getState());
			return exchangeRepo.save(exchangeEnBase);
		} else {
			throw new ExchangeException();
		}
	}

	public void delete(Exchange exchange) {
		exchangeRepo.delete(exchange);
	}

	public void deleteById(Long Id) {
		Exchange exchangeEnBase = exchangeRepo.findById(Id).orElseThrow(ExchangeException::new);
		exchangeRepo.delete(exchangeEnBase);
	}
	
	public CanceldExchange getCancelByExchangeId(Long exchangeID) {
		Exchange exchange = getById(exchangeID);
		return cancelRepo.findCancelByExchange(exchange);
	}
	
	public void cancelExchange (Exchange exchange, Long userId, Cause cause) {
		CanceldExchange cancel = new CanceldExchange();
		cancel.setExchange(exchange);
		cancel.setCause(cause);
		User user1 = new User();
		user1 = exchange.getUser1();
		User user2 = new User();
		user2 = exchange.getUser2();
		Card card1 = new Card();
		card1 = exchange.getCard1();
		Card card2 = new Card();
		card2 = exchange.getCard2();
		Set<Card> wish_cards_user1 = exchange.getUser1().getWishList();
		Set<Card> wish_cards_user2 = exchange.getUser2().getWishList();
		Set<Card> give_cards_user1 = exchange.getUser1().getToGiveList();
		Set<Card> give_cards_user2 = exchange.getUser2().getToGiveList();
		wish_cards_user1.add(card1);
		wish_cards_user2.add(card2);
		give_cards_user1.add(card2);
		give_cards_user2.add(card1);
		exchange.setState(State.CANCELED);
		user1.setWishList(wish_cards_user1);
		user2.setWishList(wish_cards_user2);
		user1.setToGiveList(give_cards_user1);
		user2.setToGiveList(give_cards_user2);
		if(userId == user1.getId()) {
			if(cancel.getCause() == Cause.NotSearched) {
				wish_cards_user1.remove(card1);
			}
			if(cancel.getCause() == Cause.NotToGive) {
				give_cards_user1.remove(card2);
			}
			if(cancel.getCause() == Cause.UserNotTrust) {
				userSrv.blockingManagment(user2);
			}
		}
		if(userId == user2.getId()) {
			if(cancel.getCause() == Cause.NotSearched) {
				wish_cards_user2.remove(card2);
			}
			if(cancel.getCause() == Cause.NotToGive) {
				give_cards_user2.remove(card1);
			}
			if(cancel.getCause() == Cause.UserNotTrust) {
				userSrv.blockingManagment(user1);
			}
		}
		userSrv.update(user1);
		userSrv.update(user2);
		exchangeRepo.save(exchange);
		cancelRepo.save(cancel);
	}
	
	public List<Exchange> getNewExchanges(Long userID) {
		User user = new User();
		user = userSrv.getById(userID);
		Set<Card> wishedCards = user.getWishList();
		Set<Card> toGiveCards = user.getToGiveList();
		List<User> givers = new ArrayList<>();
		List<Card> wishedCardsFind = new ArrayList<>();
		List<Exchange> exchangesProposal = new ArrayList<>();
		Boolean isThereRarity0 = false;
		User giverRarity0 = new User();
		Card cardWishedRarity0 = new Card();
		Card cardGivedRarity0 = new Card();
		Exchange exchangeRarity0 = new Exchange();
		Boolean isThereRarity1 = false;
		User giverRarity1 = new User();
		Card cardWishedRarity1 = new Card();
		Card cardGivedRarity1 = new Card();
		Exchange exchangeRarity1 = new Exchange();
		Boolean isThereRarity2 = false;
		User giverRarity2 = new User();
		Card cardWishedRarity2 = new Card();
		Card cardGivedRarity2 = new Card();
		Exchange exchangeRarity2 = new Exchange();
		Boolean isThereRarity3 = false;
		User giverRarity3 = new User();
		Card cardWishedRarity3 = new Card();
		Card cardGivedRarity3 = new Card();
		Exchange exchangeRarity3 = new Exchange();
		Boolean isThereRarity4 = false;
		User giverRarity4 = new User();
		Card cardWishedRarity4 = new Card();
		Card cardGivedRarity4 = new Card();
		Exchange exchangeRarity4 = new Exchange();
		for (Card c : wishedCards) {
			if (c.getRarity() == 0 && isThereRarity0 == false) {
				List<Long> giversId = cardSrv.getGiversIdsByCard(c.getId());
				if (!giversId.isEmpty()) {
					for (Long g : giversId) {
						if (g != userID) {
							User giver = userSrv.getById(g);
							Set<Card> wishedCardsGiver = giver.getWishList();
							if(!wishedCardsGiver.isEmpty() && giver.getIsVisible()) {
								Boolean isCompatible = false;
								for(Card wishedGiver: wishedCardsGiver) {
									if(wishedGiver.getRarity()==0 && !isCompatible) {
										for(Card givedWisher: toGiveCards) {
											if(wishedGiver.getSerialNumber()==givedWisher.getSerialNumber() && !isCompatible) {
												if(!isCancelHistory(c.getId(), wishedGiver.getId(), userID, g)) {
													isCompatible=true;
													cardGivedRarity0 = wishedGiver;
												}
											}
										}
									}
								}
								if(isCompatible == true && isThereRarity0 == false) {
									giverRarity0 = giver;
									cardWishedRarity0 = c;
									isThereRarity0 = true;
								}
							}
						}

					}
				}
			}
			if (c.getRarity() == 1 && isThereRarity1 == false) {
				List<Long> giversId = cardSrv.getGiversIdsByCard(c.getId());
				if (!giversId.isEmpty()) {
					for (Long g : giversId) {
						if (g != userID) {
							User giver = userSrv.getById(g);
							Set<Card> wishedCardsGiver = giver.getWishList();
							if(!wishedCardsGiver.isEmpty() && giver.getIsVisible()) {
								Boolean isCompatible = false;
								for(Card wishedGiver: wishedCardsGiver) {
									if(wishedGiver.getRarity()==1 && !isCompatible) {
										for(Card givedWisher: toGiveCards) {
											if(wishedGiver.getSerialNumber()==givedWisher.getSerialNumber() && !isCompatible) {
												if(!isCancelHistory(c.getId(), wishedGiver.getId(), userID, g)) {
													isCompatible=true;
													cardGivedRarity1 = wishedGiver;
												}
											}
										}
									}
								}
								if(isCompatible == true && isThereRarity1 == false) {
									giverRarity1 = giver;
									cardWishedRarity1 = c;
									isThereRarity1 = true;
								}
							}
						}

					}
				}
			}
			if (c.getRarity() == 2 && isThereRarity2 == false) {
				List<Long> giversId = cardSrv.getGiversIdsByCard(c.getId());
				if (!giversId.isEmpty()) {
					for (Long g : giversId) {
						if (g != userID) {
							User giver = userSrv.getById(g);
							Set<Card> wishedCardsGiver = giver.getWishList();
							if(!wishedCardsGiver.isEmpty() && giver.getIsVisible()) {
								Boolean isCompatible = false;
								for(Card wishedGiver: wishedCardsGiver) {
									if(wishedGiver.getRarity()==2 && !isCompatible) {
										for(Card givedWisher: toGiveCards) {
											if(wishedGiver.getSerialNumber()==givedWisher.getSerialNumber() && !isCompatible) {
												if(!isCancelHistory(c.getId(), wishedGiver.getId(), userID, g)) {
													isCompatible=true;
													cardGivedRarity2 = wishedGiver;
												}
											}
										}
									}
								}
								if(isCompatible == true && isThereRarity2 == false) {
									giverRarity2 = giver;
									cardWishedRarity2 = c;
									isThereRarity2 = true;
								}
							}
						}

					}
				}
			}
			if (c.getRarity() == 3 && isThereRarity3 == false) {
				List<Long> giversId = cardSrv.getGiversIdsByCard(c.getId());
				if (!giversId.isEmpty()) {
					for (Long g : giversId) {
						if (g != userID) {
							User giver = userSrv.getById(g);
							Set<Card> wishedCardsGiver = giver.getWishList();
							if(!wishedCardsGiver.isEmpty() && giver.getIsVisible()) {
								Boolean isCompatible = false;
								for(Card wishedGiver: wishedCardsGiver) {
									if(wishedGiver.getRarity()==3 && !isCompatible) {
										for(Card givedWisher: toGiveCards) {
											if(wishedGiver.getSerialNumber()==givedWisher.getSerialNumber() && !isCompatible) {
												if(!isCancelHistory(c.getId(), wishedGiver.getId(), userID, g)) {
													isCompatible=true;
													cardGivedRarity3 = wishedGiver;
												}
											}
										}
									}
								}
								if(isCompatible == true && isThereRarity3 == false) {
									giverRarity3 = giver;
									cardWishedRarity3 = c;
									isThereRarity3 = true;
								}
							}
						}

					}
				}
			}
			if (c.getRarity() == 4 && isThereRarity4 == false) {
				List<Long> giversId = cardSrv.getGiversIdsByCard(c.getId());
				if (!giversId.isEmpty()) {
					for (Long g : giversId) {
						if (g != userID) {
							User giver = userSrv.getById(g);
							Set<Card> wishedCardsGiver = giver.getWishList();
							if(!wishedCardsGiver.isEmpty() && giver.getIsVisible()) {
								Boolean isCompatible = false;
								for(Card wishedGiver: wishedCardsGiver) {
									if(wishedGiver.getRarity()==4 && !isCompatible) {
										for(Card givedWisher: toGiveCards) {
											if(wishedGiver.getSerialNumber()==givedWisher.getSerialNumber() && !isCompatible) {
												if(!isCancelHistory(c.getId(), wishedGiver.getId(), userID, g)) {
													isCompatible=true;
													cardGivedRarity4 = wishedGiver;
												}
											}
										}
									}
								}
								if(isCompatible == true && isThereRarity4 == false) {
									giverRarity4 = giver;
									cardWishedRarity4 = c;
									isThereRarity4 = true;
								}
							}
						}

					}
				}
			}
		}
		wishedCardsFind.add(cardWishedRarity0);
		wishedCardsFind.add(cardWishedRarity1);
		wishedCardsFind.add(cardWishedRarity2);
		wishedCardsFind.add(cardWishedRarity3);
		wishedCardsFind.add(cardWishedRarity4);
		givers.add(giverRarity0);
		givers.add(giverRarity1);
		givers.add(giverRarity2);
		givers.add(giverRarity3);
		givers.add(giverRarity4);
		if(cardWishedRarity0.getId() != null) {
			exchangeRarity0.setCard1(cardWishedRarity0);
			exchangeRarity0.setCard2(cardGivedRarity0);
			exchangeRarity0.setUser1(user);
			exchangeRarity0.setUser2(giverRarity0);
			exchangesProposal.add(exchangeRarity0);
		}
		if(cardWishedRarity1.getId() != null) {
			exchangeRarity1.setCard1(cardWishedRarity1);
			exchangeRarity1.setCard2(cardGivedRarity1);
			exchangeRarity1.setUser1(user);
			exchangeRarity1.setUser2(giverRarity1);
			exchangesProposal.add(exchangeRarity1);
		}
		if(cardWishedRarity2.getId() != null) {
			exchangeRarity2.setCard1(cardWishedRarity2);
			exchangeRarity2.setCard2(cardGivedRarity2);
			exchangeRarity2.setUser1(user);
			exchangeRarity2.setUser2(giverRarity2);
			exchangesProposal.add(exchangeRarity2);
		}
		if(cardWishedRarity3.getId() != null) {
			exchangeRarity3.setCard1(cardWishedRarity3);
			exchangeRarity3.setCard2(cardGivedRarity3);
			exchangeRarity3.setUser1(user);
			exchangeRarity3.setUser2(giverRarity3);
			exchangesProposal.add(exchangeRarity3);
		}
		if(cardWishedRarity4.getId() != null) {
			exchangeRarity4.setCard1(cardWishedRarity4);
			exchangeRarity4.setCard2(cardGivedRarity4);
			exchangeRarity4.setUser1(user);
			exchangeRarity4.setUser2(giverRarity4);
			exchangesProposal.add(exchangeRarity4);
		}
		return exchangesProposal;
	}

	
	public Boolean isCancelHistory(Long cardAId, Long cardBId, Long userAId, Long userBId) {
	    CanceldExchange cancelHistory = cancelRepo.findCancelHistory(userAId, userBId, cardAId, cardBId);
	    if (cancelHistory != null) {
		       return true; 
		}else {
		      return false; 
		}
	}
	
	public Long countCurrentExchange() {
		return exchangeRepo.countCurrentExchange();
	}
	public Long countFinishedExchange() {
		return exchangeRepo.countFinishedExchange();
	}

}
