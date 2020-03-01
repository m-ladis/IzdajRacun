package hr.ml.izdajracun.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface BaseDao<T> {
    @Insert
    void insert(T data);

    @Update
    void update(T data);

    @Insert
    void delete(T data);
}
