package data.tasks;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

import models.Indicator;

public class IndicatorUnloadTask implements Runnable {
    protected Indicator indicator;

    public IndicatorUnloadTask(Indicator indicator) {
        this.indicator = indicator;
    }

    @Override
    public void run() {
        SqlUpdate deletePoints = Ebean.createSqlUpdate(
            "DELETE FROM point " +
            "WHERE series_id IN (" +
                "SELECT id " +
                "FROM series " +
                "WHERE indicator_id = :indicator_id " +
            ")");
        deletePoints.setParameter("indicator_id", indicator.getId());
        deletePoints.execute();

        SqlUpdate deleteSeries = Ebean.createSqlUpdate(
            "DELETE FROM series " +
            "WHERE indicator_id = :indicator_id");
        deleteSeries.setParameter("indicator_id", indicator.getId());
        deleteSeries.execute();
    }
}
