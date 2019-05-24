package com.iSales.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.iSales.database.entry.AgendaEntry;

import java.util.List;

@Dao
public interface AgendaDao {
    @Query("SELECT * FROM agenda")
    LiveData<List<AgendaEntry>> loadAllEvents();
/*
    @Query("SELECT id, event, time, date, month, year FROM agenda")
    List<AgendaEntry> getAllEvents();

    @Query("DELETE FROM agenda")
    void deleteAllCategorie();

    @Insert
    void insertCategorie(AgendaEntry agendaEntry);

    @Query("SELECT id, event, time, date, month, year FROM agenda WHERE date = :date")
    List<AgendaEntry> getAllEvents(String date);

    @Query("SELECT id, event, time, date, month, year FROM agenda WHERE month = :month and year = :year")
    List<AgendaEntry> getAllEventsPerMonth(String month, String year);
*/
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCategorie(AgendaEntry agendaEntry);

    @Delete
    void deleteCategorie(AgendaEntry agendaEntry);

    @Query("SELECT * FROM agenda WHERE id = :id")
    LiveData<AgendaEntry> loadAgendaById(long id);
}
