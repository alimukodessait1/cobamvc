package com.mycompany.cobamvc.dao;

import com.mycompany.cobamvc.model.User;

/**
 * Interface Spring DAO untuk standarisasi akses data User
 * Menerapkan prinsip Class Interface (Dependency Injection)
 */
public interface UserDAO {
    User login(String username, String password);
}
