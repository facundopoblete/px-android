package com.mercadopago.android.px.internal.features.review_and_confirm;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.addons.model.SecurityValidationData;
import com.mercadopago.android.px.core.DynamicDialogCreator;
import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.internal.callbacks.PaymentServiceHandler;
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator;
import com.mercadopago.android.px.internal.features.pay_button.PayButton;
import com.mercadopago.android.px.internal.viewmodel.BusinessPaymentModel;
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel;
import com.mercadopago.android.px.internal.viewmodel.PaymentModel;
import com.mercadopago.android.px.internal.viewmodel.PostPaymentAction;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;

public interface ReviewAndConfirm {

    interface View extends MvpView {

        void showCardCVVRequired(@NonNull final Card card);

        void showPaymentProcessor();

        void showResult(@NonNull final BusinessPaymentModel businessPaymentModel);

        void showResult(@NonNull final PaymentModel paymentModel);

        void startPaymentRecoveryFlow(PaymentRecovery recovery);

        void showErrorScreen(@NonNull final MercadoPagoError error);

        void showErrorSnackBar(@NonNull final MercadoPagoError error);

        void showDynamicDialog(@NonNull final DynamicDialogCreator creator,
            @NonNull final DynamicDialogCreator.CheckoutData checkoutData);

        void finishAndChangePaymentMethod();
    }

    interface Action {
        void onPrePayment(@NonNull final PayButton.OnReadyForPaymentCallback callback);

        void onPaymentFinished(@NonNull final PaymentModel paymentModel);

        void changePaymentMethod();
    }
}