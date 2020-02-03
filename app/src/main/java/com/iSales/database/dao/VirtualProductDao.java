package com.iSales.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.iSales.database.entry.UserEntry;
import com.iSales.database.entry.VirtualProductEntry;

import java.util.List;

/**
 * Created by JDevs on 16/01/2020.
 */

@Dao
public interface VirtualProductDao {
    @Query("SELECT * FROM virtual_product")
    List<UserEntry> getAllVirtualProduct();

    @Query("SELECT * FROM virtual_product WHERE _0 = :id")
    List<UserEntry> getVirtualProductById(String id);

    @Insert
    void insertVirtualProduct(VirtualProductEntry mVirtualProductEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateVirtualProduct(VirtualProductEntry mVirtualProductEntry);

    @Delete
    void deleteVirtualProduct(VirtualProductEntry mVirtualProductEntry);

    @Query("DELETE FROM virtual_product")
    void deleteAllVirtualProduct();
}
