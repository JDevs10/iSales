package com.rainbow_cl.i_sales.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.rainbow_cl.i_sales.database.entry.ClientEntry;
import com.rainbow_cl.i_sales.database.entry.PanierEntry;

import java.util.List;

/**
 * Created by netserve on 30/10/2018.
 */

@Dao
public interface ClientDao {
    @Query("SELECT * FROM client")
    LiveData<List<ClientEntry>> loadAllClient();

    @Query("SELECT * FROM client")
    List<ClientEntry> getAllClient();

    @Query("SELECT * FROM client WHERE id > :lastId ORDER BY id LIMIT :limit")
    List<ClientEntry> getClientsLimit(long lastId, int limit);

    @Query("SELECT * FROM client WHERE LOWER(address) LIKE '%'||:keyword||'%' OR LOWER(name) LIKE '%'||:keyword||'%' ORDER BY id LIMIT :limit")
    List<ClientEntry> getClientsLikeLimit(int limit, String keyword);

    @Insert
    void insertClient(ClientEntry clientEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateClient(ClientEntry clientEntry);

//    Mise a jour du logo d'un client
    @Query("UPDATE client SET logo_content = :logoBytes WHERE id = :clientId")
    void updateLogo_content(String logoBytes, long clientId);

//    Mise a jour du logo d'un client
    @Query("UPDATE client SET logo = :logoPath WHERE id = :clientId")
    void updateLogo(String logoPath, long clientId);

    @Query("SELECT * FROM client WHERE id = :id")
    LiveData<ClientEntry> loadClientById(long id);

    @Query("SELECT * FROM client WHERE id = :id")
    ClientEntry getClientById(long id);

    @Delete
    void deleteClient(ClientEntry clientEntry);

    @Query("DELETE FROM client")
    void deleteAllClient();
}
