package com.rainbow_cl.i_sales.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.rainbow_cl.i_sales.database.entry.ServerEntry;
import com.rainbow_cl.i_sales.database.entry.TokenEntry;

import java.util.List;

/**
 * Created by netserve on 11/01/2019.
 */

@Dao
public interface ServerDao {
    @Query("SELECT * FROM server")
    List<ServerEntry> getAllServers();

    @Query("SELECT * FROM server WHERE is_active=:active")
    ServerEntry getActiveServer(boolean active);

    @Query("SELECT * FROM server WHERE hostname=:hostname")
    ServerEntry getServerByHostname(String hostname);

    @Insert
    void insertServer(ServerEntry serverEntry);

    @Insert
    void insertAllServer(List<ServerEntry> serverEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateServer(ServerEntry serverEntry);

    @Query("UPDATE server SET is_active=:active WHERE id = :id")
    void updateActiveServer(long id, boolean active);

    @Query("UPDATE server SET is_active = :active")
    void updateActiveAllserver(boolean active);

    @Delete
    void deleteServer(ServerEntry serverEntry);

    @Query("DELETE FROM server")
    void deleteAllServers();
}
