package com.instablog.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.instablog.model.User;

public class UserDao {
	public List<User> getAllUsers() {

		List<User> userList = null;
		User user = new User(1, "Mahesh", "Nom1", "pseudo1", "password1");
		User user2 = new User(2, "Mahesh2", "Nom2", "pseudo2", "password2");
		userList = new ArrayList<User>();
		userList.add(user);
		userList.add(user2);
		return userList;
	}

	public User getUserById(int id) {
		// Recherche User correspondant dans la BD et le renvoie
		User user = new User(1, "Mahesh", "Nom1", "pseudo1", "password1");
		return user;
	}

	public boolean deleteUserById(int parseInt) {
		boolean deleted = false;
		return deleted;
	}

	public boolean updateUserById(int parseInt) {
		boolean updated = false;
		return updated;
	}

	public User createUser(String user) {
		//Cree le user à partir de la string puis renvoie l'objet creer dans la base de donnée
		return null;
	}

	public void changeUserUUID(int userID, String randomUUIDString) {
		// TODO Auto-generated method stub
		
	}
}