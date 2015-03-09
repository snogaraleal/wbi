import play.Application;
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.gzip.GzipFilter;

import data.tasks.CountryPopulateTask;
import data.tasks.IndicatorPopulateTask;
import data.tasks.TaskUtils;

import controllers.ServerConf;

/**
 * Play application {@code GlobalSettings} object.
 */
public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        super.onStart(app);

        // Configure RPC mechanism
        ServerConf.configureRPC();

        // Start tasks for populating the database when the application starts
        TaskUtils.runTask(new CountryPopulateTask());
        TaskUtils.runTask(new IndicatorPopulateTask());
    }

    @Override
    public void onStop(Application app) {
        super.onStop(app);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[] {
            GzipFilter.class
        };
    }
}
