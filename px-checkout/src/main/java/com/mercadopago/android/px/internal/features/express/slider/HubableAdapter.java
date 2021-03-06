package com.mercadopago.android.px.internal.features.express.slider;

import android.view.View;
import java.util.List;

public abstract class HubableAdapter<T, V extends View> extends ViewAdapter<T, V> {
    /* default */ HubableAdapter(final V view) {
        super(view);
    }

    public abstract T getNewModels(HubAdapter.Model model);
}