package com.mycompany.cobamvc.dao;

import com.mycompany.cobamvc.model.Mobil;
import java.util.List;

/**
 * Interface Spring DAO untuk standarisasi akses data Mobil
 * Menerapkan prinsip Class Interface (Dependency Injection)
 */
public interface MobilDAO {
    boolean insert(Mobil mobil);
    boolean update(Mobil mobil);
    boolean delete(int id);
    List<Mobil> getAll();
}
