package data.tasks;

import com.avaje.ebean.Ebean;

import models.Indicator;

import data.api.WorldBank;

public class IndicatorPopulateTask implements Runnable {
    @Override
    public void run() {
        if (Indicator.objects.findRowCount() > 0) {
            return;
        }

        WorldBank worldBank = new WorldBank().fetchIndicatorList();

        Ebean.save(worldBank.getTopicMap().values());
        Ebean.save(worldBank.getSourceMap().values());
        Ebean.save(worldBank.getIndicatorMap().values());
    }
}
