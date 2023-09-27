package org.movieBooking.services;


import org.movieBooking.entities.User;
import org.movieBooking.exceptions.UsernameAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface UserService {

	List<User> getUsersList();

	User encryptAndSave(User user) throws UsernameAlreadyExistsException;

	User encryptAndUpdate(User user);

	User saveOrUpdate(User user);

	void deleteEntry(User user);


	Optional<User> getByUsername(String userName);

	Optional<User> findById(int id);



}