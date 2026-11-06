package com.mycompany.cobamvc.dao;

import com.mycompany.cobamvc.model.User;
import com.mycompany.cobamvc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserDAO {

    public User login(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Jika tabel users masih kosong, otomatis daftarkan akun admin default
            Long count = session.createQuery("SELECT COUNT(u) FROM User u", Long.class).uniqueResult();
            if (count == null || count == 0) {
                Transaction tx = session.beginTransaction();
                session.persist(new User("admin", "123"));
                tx.commit();
            }

            // Cari user dengan username dan password yang cocok
            String hql = "FROM User WHERE username = :uname AND password = :pwd";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("uname", username.trim());
            query.setParameter("pwd", password.trim());

            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Koneksi Database / Hibernate Gagal: " + e.getMessage(), e);
        }
    }
}
