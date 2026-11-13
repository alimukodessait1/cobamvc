package com.mycompany.cobamvc.dao;

import com.mycompany.cobamvc.model.Mobil;
import com.mycompany.cobamvc.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

/**
 * Implementasi Spring DAO untuk entitas Mobil menggunakan Hibernate
 */
@Repository
public class MobilDaoImpl implements MobilDAO {

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<Mobil> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Mobil> list = session.createQuery("FROM Mobil ORDER BY id ASC", Mobil.class).list();

            // Jika armada mobil kurang dari 5, otomatis tambahkan beberapa mobil populer
            if (list == null || list.size() < 5) {
                tambahMobilOtomatis(session, list);
                list = session.createQuery("FROM Mobil ORDER BY id ASC", Mobil.class).list();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void tambahMobilOtomatis(Session session, List<Mobil> existingList) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // Cek kode mobil yang sudah ada agar tidak duplikat
            List<String> existingKode = new java.util.ArrayList<>();
            if (existingList != null) {
                for (Mobil m : existingList) {
                    existingKode.add(m.getKodeMobil());
                }
            }

            if (!existingKode.contains("M003")) {
                session.persist(new Mobil("M003", "Honda Brio RS", "Honda", 300000, "Tersedia"));
            }
            if (!existingKode.contains("M004")) {
                session.persist(new Mobil("M004", "Mitsubishi Xpander", "Mitsubishi", 400000, "Tersedia"));
            }
            if (!existingKode.contains("M005")) {
                session.persist(new Mobil("M005", "Toyota Fortuner GR", "Toyota", 850000, "Tersedia"));
            }
            if (!existingKode.contains("M006")) {
                session.persist(new Mobil("M006", "Mitsubishi Pajero Sport", "Mitsubishi", 850000, "Disewa"));
            }
            if (!existingKode.contains("M007")) {
                session.persist(new Mobil("M007", "Toyota Alphard Luxury", "Toyota", 1800000, "Tersedia"));
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
