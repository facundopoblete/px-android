package com.mercadopago.testlib.pages;


import com.mercadopago.testlib.assertions.Validator;

public abstract class PageObject<T extends Validator> {

    protected final T validator;

    protected PageObject() {
        this(null);
    }

    protected PageObject(T validator) {
        this.validator = validator;
        if (validator != null) validate(validator);
    }

    public abstract PageObject validate(T validator);
}
