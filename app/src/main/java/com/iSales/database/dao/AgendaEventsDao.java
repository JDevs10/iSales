package com.iSales.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.iSales.database.entry.AgendaEventEntry;

import java.util.List;

@Dao
public interface AgendaEventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNewEvent(AgendaEventEntry events);

    @Query("SELECT * FROM agenda_events")
    List<AgendaEventEntry> getAllEvents();

    @Query("SELECT * FROM agenda_events WHERE id = :id")
    List<AgendaEventEntry> getEventsById(Long id);

    @Query("SELECT * FROM agenda_events WHERE DATE = :date")
    List<AgendaEventEntry> getEventsByDate(String date);

    @Query("SELECT * FROM agenda_events WHERE MONTH = :month and YEAR = :year")
    List<AgendaEventEntry> getEventsByMonth(String month, String year);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEvent(AgendaEventEntry events);

    @Delete
    void deleteEvent(AgendaEventEntry events);

    @Query("DELETE FROM agenda_events WHERE id = :id")
    void deleteEvent(long id);
}
