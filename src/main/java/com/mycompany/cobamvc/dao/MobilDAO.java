package com.mycompany.cobamvc.dao;

import com.mycompany.cobamvc.model.Mobil;
import com.mycompany.cobamvc.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MobilDAO {

    // Simpan Data Mobil Baru (Insert)
    public boolean insert(Mobil mobil) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(mobil);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Ubah Data Mobil (Update)
    public boolean update(Mobil mobil) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(mobil);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Hapus Data Mobil (Delete)
    public boolean delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Mobil mobil = session.get(Mobil.class, id);
            if (mobil != null) {
                session.remove(mobil);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Ambil Semua Data Mobil untuk Ditampilkan di JTable
    public List<Mobil> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Mobil ORDER BY id DESC", Mobil.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
