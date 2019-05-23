package com.javahelps.helloaxis;

import java.util.List;

import javax.persistence.EntityManager;

import com.instablog.connexion.InstaException;
//import com.instablog.connexion.EMFManager;
import com.instablog.dao.UserDao;
import com.instablog.model.User;

public class HelloService {

	UserDao userDao = new UserDao();
	
	public String sayHello(String name) {
		//EntityManager em = EMFManager.getEntityManager(); 
        return "Hello " + name;
    }
	
	public List<User> users(String name) throws InstaException {
		//EntityManager em = EMFManager.getEntityManager(); 
        return userDao.getAllUsers();
    }
	
}
