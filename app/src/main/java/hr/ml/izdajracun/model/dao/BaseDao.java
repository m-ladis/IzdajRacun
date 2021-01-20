package hr.ml.izdajracun.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface BaseDao<T> {
    @Insert
    void insert(T data);

    @Update
    void update(T data);

    @Delete
    void delete(T data);
}
