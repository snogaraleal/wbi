package services;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

import rpc.server.Service;

import models.Indicator;

import data.tasks.IndicatorLoadTask;
import data.tasks.IndicatorUnloadTask;
import data.tasks.TaskUtils;

public class WBIManagementService implements Service {
    private static void updateIndicatorStatus(
            Indicator indicator, Indicator.Status status) {

        SqlUpdate updateIndicator = Ebean.createSqlUpdate(
            "UPDATE indicator " +
            "SET status = :status " +
            "WHERE id = :id");

        updateIndicator.setParameter("status", status);
        updateIndicator.setParameter("id", indicator.getId());
        updateIndicator.execute();

        indicator.setStatus(status);
    }

    private static class IndicatorLoadAndUpdateTask
        extends IndicatorLoadTask {

        public IndicatorLoadAndUpdateTask(Indicator indicator) {
            super(indicator);
        }

        @Override
        public void run() {
            super.run();

            updateIndicatorStatus(indicator, Indicator.Status.READY);
        }
    }

    private static class IndicatorUnloadAndUpdateTask 
        extends IndicatorUnloadTask {

        public IndicatorUnloadAndUpdateTask(Indicator indicator) {
            super(indicator);
        }

        @Override
        public void run() {
            super.run();

            updateIndicatorStatus(indicator, Indicator.Status.AVAILABLE);
        }
    }

    public static List<Indicator> poll(List<Long> indicatorIds) {
        if (indicatorIds.isEmpty()) {
            return null;
        }

        return Indicator.objects
            .where()
                .idIn(indicatorIds)
            .query()
            .fetch("source")
            .fetch("topics")
            .findList();
    }

    public static Indicator load(Long indicatorId) {
        Indicator indicator = Indicator.objects.byId(indicatorId);

        if (indicator.isAvailable()) {
            updateIndicatorStatus(indicator, Indicator.Status.LOADING);

            TaskUtils.runTask(new IndicatorLoadAndUpdateTask(indicator));
        }

        return indicator;
    }

    public static Indicator unload(Long indicatorId) {
        Indicator indicator = Indicator.objects.byId(indicatorId);

        if (indicator.isReady()) {
            updateIndicatorStatus(indicator, Indicator.Status.LOADING);

            TaskUtils.runTask(new IndicatorUnloadAndUpdateTask(indicator));
        }

        return indicator;
    }
}
