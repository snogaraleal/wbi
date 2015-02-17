package data.tasks;

import com.avaje.ebean.Ebean;

import models.Country;

import data.api.WorldBank;

public class CountryPopulateTask implements Runnable {
    @Override
    public void run() {
        if (Country.objects.findRowCount() > 0) {
            return;
        }

        WorldBank worldBank = new WorldBank().fetchCountryList();

        Ebean.save(worldBank.getRegionMap().values());
        Ebean.save(worldBank.getCountryMap().values());
    }
}
