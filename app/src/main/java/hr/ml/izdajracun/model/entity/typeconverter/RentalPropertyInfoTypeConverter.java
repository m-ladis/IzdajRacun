package hr.ml.izdajracun.model.entity.typeconverter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import hr.ml.izdajracun.model.entity.RentalPropertyInfo;

public class RentalPropertyInfoTypeConverter {
    @TypeConverter
    public static String rentalPropertyInfoToJSON(RentalPropertyInfo propertyInfo){
        Gson gson = new Gson();

        return gson.toJson(propertyInfo);
    }

    @TypeConverter
    public static RentalPropertyInfo rentalPropertyInfoFromJSON(String json){
        Gson gson = new Gson();

        return gson.fromJson(json, RentalPropertyInfo.class);
    }
}
