package client.history;

import java.util.List;

import com.google.gwt.core.client.Callback;

import models.Indicator;

import client.managers.history.HistoryManager;
import client.managers.history.HistoryState;
import client.managers.history.HistoryStateData;
import client.managers.models.IndicatorManager;

/**
 * {@link HistoryManager.Listener} in charge of updating an
 * {@link IndicatorManager} depending on the current {@link HistoryState}.
 */
public class IndicatorHistory extends HistoryManager.BaseHistory
    implements IndicatorManager.Listener {

    /**
     * Currently attached {@code IndicatorManager}.
     */
    private IndicatorManager manager;

    public IndicatorHistory() {}

    /**
     * Attach an {@link IndicatorManager}.
     *
     * @param manager Manager to attach.
     */
    public void connect(IndicatorManager manager) {
        assert this.manager == null;

        this.manager = manager;
        this.manager.addListener(this);
    }

    /**
     * Detach the currently attached {@link IndicatorManager}.
     */
    public void disconnect() {
        assert this.manager != null;

        this.manager.removeListener(this);
        this.manager = null;
    }

    /**
     * Handle a {@code HistoryState} change.
     */
    @Override
    public void onChange(HistoryState state) {
        String indicatorIdent = state.getIndicatorIdent();

        if (indicatorIdent != null) {
            /*
             * Request a {@code HistoryStateData} when the current
             * {@code HistoryState} specifies an indicator identifier.
             */
            state.getData(new Callback<HistoryStateData, Void>() {
                @Override
                public void onSuccess(HistoryStateData data) {
                    /*
                     * Get the {@code Indicator} object and update the
                     * manager accordingly.
                     */
                    Indicator indicator = data.getIndicator();

                    if (indicator != null) {
                        manager.select(indicator);
                    }
                }

                @Override
                public void onFailure(Void reason) {
                }
            });
        }
    }

    @Override
    public void onSearch(
            List<Indicator> indicators,
            Indicator selectedIndicator) {
    }

    @Override
    public void onChange(Indicator indicator) {
    }

    /**
     * Handle selected {@code Indicator}.
     */
    @Override
    public void onSelect(Indicator indicator) {
        HistoryState state = historyManager.getCurrentState();

        /*
         * Update the current {@code HistoryState} with the identifier of the
         * selected {@code Indicator}.
         */
        state.setIndicatorIdent(indicator.getIdent());
        historyManager.setState(state);
    }
}
