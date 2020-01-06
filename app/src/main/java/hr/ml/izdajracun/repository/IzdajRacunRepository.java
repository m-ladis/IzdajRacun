package hr.ml.izdajracun.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import hr.ml.izdajracun.model.dao.RentalPropertyInfoDao;
import hr.ml.izdajracun.model.database.IzdajRacunDatabase;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;

public class IzdajRacunRepository {

    private RentalPropertyInfoDao rentalPropertyInfoDao;
    private LiveData<List<RentalPropertyInfo>> propertiesInfo;

    public IzdajRacunRepository(Application application) {
        IzdajRacunDatabase database = IzdajRacunDatabase.getInstance(application);
        rentalPropertyInfoDao = database.rentalPropertyInfoDao();
        propertiesInfo = rentalPropertyInfoDao.getAllRentalPropertiesInfo();
    }

    public void insert(RentalPropertyInfo rentalPropertyInfo){
        new InsertRentalPropertyInfoAsyncTask(rentalPropertyInfoDao).execute(rentalPropertyInfo);
    }

    public void update(RentalPropertyInfo rentalPropertyInfo){
        new UpdateRentalPropertyInfoAsyncTask(rentalPropertyInfoDao).execute(rentalPropertyInfo);
    }

    public void delete(RentalPropertyInfo rentalPropertyInfo){
        new DeleteRentalPropertyInfoAsyncTask(rentalPropertyInfoDao).execute(rentalPropertyInfo);
    }

    public LiveData<List<RentalPropertyInfo>> getAllPropertiesInfo(){
        return propertiesInfo;
    }

    private static class InsertRentalPropertyInfoAsyncTask
            extends AsyncTask<RentalPropertyInfo, Void, Void> {

        private final RentalPropertyInfoDao dao;

        public InsertRentalPropertyInfoAsyncTask(RentalPropertyInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RentalPropertyInfo... rentalPropertyInfos) {
            RentalPropertyInfo propertyInfo = rentalPropertyInfos[0];

            dao.insert(propertyInfo);

            return null;
        }
    }

    private static class UpdateRentalPropertyInfoAsyncTask
            extends AsyncTask<RentalPropertyInfo, Void, Void> {

        private final RentalPropertyInfoDao dao;

        public UpdateRentalPropertyInfoAsyncTask(RentalPropertyInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RentalPropertyInfo... rentalPropertyInfos) {
            RentalPropertyInfo propertyInfo = rentalPropertyInfos[0];

            dao.update(propertyInfo);

            return null;
        }
    }

    private static class DeleteRentalPropertyInfoAsyncTask
            extends AsyncTask<RentalPropertyInfo, Void, Void> {

        private final RentalPropertyInfoDao dao;

        public DeleteRentalPropertyInfoAsyncTask(RentalPropertyInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RentalPropertyInfo... rentalPropertyInfos) {
            RentalPropertyInfo propertyInfo = rentalPropertyInfos[0];

            dao.delete(propertyInfo);

            return null;
        }
    }
}
