package hr.ml.izdajracun.model.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import hr.ml.izdajracun.model.dao.BusinessInvoiceDao;
import hr.ml.izdajracun.model.dao.InvoiceDao;
import hr.ml.izdajracun.model.dao.RentalPropertyInfoDao;
import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.model.entity.typeconverter.CalendarTypeConverter;
import hr.ml.izdajracun.model.entity.typeconverter.RentalPropertyInfoTypeConverter;

@Database(entities = {RentalPropertyInfo.class, Invoice.class, BusinessInvoice.class}, version = 4)
@TypeConverters(value = {CalendarTypeConverter.class, RentalPropertyInfoTypeConverter.class})
public abstract class IzdajRacunDatabase extends RoomDatabase {

    private static IzdajRacunDatabase database;

    public abstract RentalPropertyInfoDao rentalPropertyInfoDao();

    public abstract InvoiceDao invoiceDao();

    public abstract BusinessInvoiceDao businessInvoiceDao();

    public static synchronized IzdajRacunDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context, IzdajRacunDatabase.class,
                    "izdaj_racun_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);

                            Log.d("izdaj_racun_database", "opened");
                        }
                    })
                    .build();
        }
        return database;
    }
}
