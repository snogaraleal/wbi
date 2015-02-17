package data.tasks;

import com.avaje.ebean.Ebean;

import models.Country;
import models.Indicator;

import data.api.WorldBank;

public class IndicatorLoadTask implements Runnable {
    protected Indicator indicator;

    public IndicatorLoadTask(Indicator indicator) {
        this.indicator = indicator;
    }

    @Override
    public void run() {
        WorldBank worldBank = new WorldBank().fetchSeries(
            this.indicator, Country.objects.all());

        Ebean.save(worldBank.getSeriesMap().values());
        Ebean.save(worldBank.getPointList());
    }
}
