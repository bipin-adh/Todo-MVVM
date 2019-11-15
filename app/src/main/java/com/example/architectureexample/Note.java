package com.example.architectureexample;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 *  Entity - hides boiler plate code that we write in SQlite helper class.
 *  Entity is a room annotation. At compile time it will create codes to create a sqlite table for this object.
 *
 *  By annotating this Java class with @Entity, we can
 *  let room generate all the necessary code to create an SQLite table for this object,
 *  as well as columns for all it's fields.
 */
@Entity(tableName = "note_table")
public class Note {

    /**
     * With @PrimaryKey and autoGenerate = true,
     * we can turn an integer member variable into an auto-incrementing primary key,
     * which we can use to uniquely identify each row in the table.
     * In the past we would have to do all of this in an SQLiteOpenHelper with plain SQL statements.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private int priority;

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
