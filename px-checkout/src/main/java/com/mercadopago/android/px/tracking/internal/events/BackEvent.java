package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.tracking.internal.views.ViewTracker;

public class BackEvent extends EventTracker {

    private static final String BACK = "/back";

    @NonNull private final ViewTracker viewTracker;

    public BackEvent(@NonNull final ViewTracker viewTracker) {
        this.viewTracker = viewTracker;
    }

    @NonNull
    @Override
    public String getEventPath() {
        return viewTracker.getViewPath() + BACK;
    }
}
