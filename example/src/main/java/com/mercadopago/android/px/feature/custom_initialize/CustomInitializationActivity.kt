package com.mercadopago.android.px.feature.custom_initialize

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.mercadopago.android.px.di.Dependencies
import com.mercadopago.example.R

class CustomInitializationActivity : AppCompatActivity() {

    private lateinit var viewModel: CustomInitializationViewModel
    private lateinit var localeInput: AutoCompleteTextView
    private lateinit var publicKeyInput: AutoCompleteTextView
    private lateinit var preferenceIdInput: AutoCompleteTextView
    private lateinit var accessTokenInput: AutoCompleteTextView
    private lateinit var clearButton: Button
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_initialization)
        viewModel = Dependencies.instance.viewModelModule!!.get(this, CustomInitializationViewModel::class.java)
        localeInput = findViewById(R.id.localeInput)
        publicKeyInput = findViewById(R.id.publicKeyInput)
        preferenceIdInput = findViewById(R.id.preferenceIdInput)
        accessTokenInput = findViewById(R.id.accessTokenInput)
        clearButton = findViewById(R.id.clearButton)
        startButton = findViewById(R.id.startButton)
        configureViews()
        bindViewModel(savedInstanceState)
        viewModel.initialize()
    }

    private fun bindViewModel(savedInstanceState: Bundle?) {
        savedInstanceState?.let { viewModel.recoverFromBundle(savedInstanceState) }
        viewModel.stateUILiveData.observe(this, Observer { state ->
            when (state) {
                is CustomInitializeState.LoadData -> {
                    loadData(state.initializationData)
                }
                is CustomInitializeState.InitCheckout -> {
                    state.builder.build().startPayment(this, REQUEST_CODE)
                }
                else -> {
                }
            }
        })
    }

    private fun loadData(initializationData: InitializationData) {
        localeInput.setText(initializationData.locale.value)
        publicKeyInput.setText(initializationData.publicKey.value)
        preferenceIdInput.setText(initializationData.preferenceId.value)
        accessTokenInput.setText(initializationData.accessToken.value)
    }

    private fun configureViews() {
        configInputDropDownView(localeInput, resources.getStringArray(R.array.locales))
        { value -> value?.let { viewModel.onInputChanged(InitializationDataType.Locale(it)) } }
        configInputDropDownView(publicKeyInput, resources.getStringArray(R.array.public_key))
        { value -> value?.let { viewModel.onInputChanged(InitializationDataType.PublicKey(it)) } }
        configInputDropDownView(preferenceIdInput, resources.getStringArray(R.array.preference_ids))
        { value -> value?.let { viewModel.onInputChanged(InitializationDataType.PreferenceId(it)) } }
        configInputDropDownView(accessTokenInput, resources.getStringArray(R.array.access_tokens))
        { value -> value?.let { viewModel.onInputChanged(InitializationDataType.AccessToken(it)) } }

        clearButton.setOnClickListener { viewModel.onClear() }
        startButton.setOnClickListener { viewModel.onStartButtonClicked() }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configInputDropDownView(view: AutoCompleteTextView, items: Array<String>, updateValue: ((String?) -> Unit)) {
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items).also { adapter ->
            view.setAdapter(adapter)
        }
        view.setOnTouchListener { _, _ ->
            view.showDropDown()
            false
        }
        view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateValue(s?.toString())
            }
        })
    }

    companion object {
        private const val REQUEST_CODE = 0x01
    }
}