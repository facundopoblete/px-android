package com.mercadopago.plugins.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mercadopago.R;
import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Renderer;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.paymentresult.components.Header;
import com.mercadopago.paymentresult.props.HeaderProps;
import com.mercadopago.plugins.model.ButtonAction;

public class BusinessPaymentRenderer extends Renderer<BusinessPaymentContainer> {
    @Override
    protected View render(@NonNull final BusinessPaymentContainer component,
                          @NonNull final Context context,
                          @Nullable final ViewGroup parent) {

        LinearLayout linearLayout = createMainContainer(context);
        ScrollView scrollView = createScrollContainer(context, linearLayout);
        View header = addHeader(component, context, linearLayout);

        if (component.props.hasHelp()) {
            addHelp(component.props.getHelp(), linearLayout);
        }

        renderButtons(component, linearLayout, scrollView, header);

        return scrollView;
    }

    private void renderButtons(final @NonNull BusinessPaymentContainer component, final LinearLayout linearLayout,
                               final ScrollView scrollView, final View header) {
        if (component.props.hasPrimaryButton() || component.props.hasSecondaryButton()) {
            final Context context = linearLayout.getContext();
            final View divider = new View(context);
            divider.setBackgroundColor(context.getResources().getColor(R.color.mpsdk_very_light_gray));
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    context.getResources().getDimensionPixelSize(R.dimen.mpsdk_xxxs_height)));
            linearLayout.addView(divider);

            configureBottomCorrection(linearLayout, scrollView, divider, component, header);

            if (component.props.hasPrimaryButton()) {
                addPrimaryButton(component.getDispatcher(),
                        component.props.getPrimaryAction(),
                        linearLayout);
            }

            if (component.props.hasSecondaryButton()) {
                addSecondaryButton(component.getDispatcher(),
                        component.props.getSecondaryAction(), linearLayout);
            }
        }

    }

    private void configureBottomCorrection(final LinearLayout linearLayout, final ScrollView scrollView,
                                           final View divider,
                                           final BusinessPaymentContainer component,
                                           final View header) {
        final ViewTreeObserver vto = scrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int linearHeight = linearLayout.getMeasuredHeight();
                int scrollHeight = scrollView.getMeasuredHeight();
                if (scrollHeight > linearHeight) {
                    int diffHeight = scrollHeight - linearHeight;
                    if (component.props.hasHelp()) { //Alias has body
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) divider.getLayoutParams();
                        layoutParams.setMargins(0, diffHeight, 0, 0);
                        divider.setLayoutParams(layoutParams);
                    } else {
                        header.setPadding(header.getPaddingLeft(), header.getPaddingTop(), header.getPaddingRight(), (header.getPaddingBottom() + diffHeight));
                    }
                }
            }
        });
    }

    private void addPrimaryButton(final ActionDispatcher dispatcher,
                                  final ButtonAction primaryAction,
                                  final ViewGroup parent) {
        View parentView = inflate(R.layout.mpsdk_view_text_button_blue, parent);
        TextView blueButton = parentView.findViewById(R.id.text_button_blue);
        blueButton.setText(primaryAction.getName());
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dispatcher.dispatch(primaryAction);
            }
        });
    }

    private void addSecondaryButton(final ActionDispatcher dispatcher,
                                    final ButtonAction secondaryAction,
                                    final ViewGroup parent) {

        View parentView = inflate(R.layout.mpsdk_view_text_button, parent);
        TextView whiteButton = parentView.findViewById(R.id.text_button_white);
        whiteButton.setText(secondaryAction.getName());
        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dispatcher.dispatch(secondaryAction);
            }
        });
    }

    private void addHelp(final String help, final ViewGroup parent) {
        final View bodyErrorView = inflate(R.layout.mpsdk_payment_result_body_error, parent);
        TextView errorTitle = bodyErrorView.findViewById(R.id.paymentResultBodyErrorTitle);
        TextView errorDescription = bodyErrorView.findViewById(R.id.paymentResultBodyErrorDescription);
        bodyErrorView.findViewById(R.id.paymentResultBodyErrorSecondDescription).setVisibility(View.GONE);
        errorTitle.setText(parent.getContext().getString(R.string.mpsdk_what_can_do));
        errorDescription.setText(help);
    }

    private View addHeader(final @NonNull BusinessPaymentContainer component, final @NonNull Context context,
                           final LinearLayout linearLayout) {
        Header header = new Header(HeaderProps.from(component.props, context), component.getDispatcher());
        View render = RendererFactory.create(context, header).render(linearLayout);
        return render.findViewById(R.id.mpsdkPaymentResultContainerHeader);
    }

    @NonNull
    private ScrollView createScrollContainer(final @NonNull Context context, final LinearLayout linearLayout) {
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.addView(linearLayout);
        scrollView.setBackgroundColor(scrollView
                .getContext()
                .getResources()
                .getColor(R.color.mpsdk_white_background));
        return scrollView;
    }

    @NonNull
    private LinearLayout createMainContainer(final @NonNull Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.mpsdk_white));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setPadding(0, 0, 0, context.getResources().getDimensionPixelSize(R.dimen.mpsdk_s_margin));
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }
}