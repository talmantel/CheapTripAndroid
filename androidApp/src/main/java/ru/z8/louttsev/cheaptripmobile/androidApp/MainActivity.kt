package ru.z8.louttsev.cheaptripmobile.androidApp

import android.os.Bundle
import android.text.Editable
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.z8.louttsev.cheaptripmobile.androidApp.databinding.ActivityMainBinding
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.viewmodel.AutoCompleteHandler
import ru.z8.louttsev.cheaptripmobile.shared.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model by viewModels<MainViewModel> {
            createWithFactory { MainViewModel(App.sLocationRepository) }
        }

        val binding = ActivityMainBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
            viewModel = model // ignore probably IDE error message "Cannot access class..."
        }

        setContentView(binding.root)

        with(binding) {
            originTextView.setup(model.origins)

            originClearIcon.setOnClickListener {
                model.origins.onItemReset()
                originTextView.clearText()
            }

            destinationTextView.setup(model.destinations)

            destinationClearIcon.setOnClickListener {
                model.destinations.onItemReset()
                destinationTextView.clearText()
            }

            clearButton.setOnClickListener {
                model.origins.onItemReset()
                originTextView.clearText()
                model.destinations.onItemReset()
                destinationTextView.clearText()
            }

            goButton.setOnClickListener {
                Toast.makeText(
                    this@MainActivity,
                    model.getRoute(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createWithFactory(
        create: () -> ViewModel
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return create.invoke() as T
        }
    }

    private fun AutoCompleteTextView.setup(handler: AutoCompleteHandler<Location>) {
        threshold = 1

        setAdapter(
            AutoCompleteLocationsListAdapter(this@MainActivity, handler.data)
        )

        addTextChangedListener { changedEditableText: Editable? ->
            handler.onItemReset()
            handler.isBeingUpdated = true
            handler.onTextChanged(
                changedEditableText.toString(),
                emptyResultHandler = {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.no_data_error_message),
                        Toast.LENGTH_LONG
                    ).show()
                    selectSuitableLocation(handler)
                }
            )
        }

        setOnItemClickListener { parent: AdapterView<*>, _, position: Int, _ ->
            val selectedLocation = parent.getItemAtPosition(position) as Location

            handler.onItemSelected(selectedLocation)
            performCompletion()
        }

        setOnDismissListener {
            if (!handler.isItemSelected()) {
                if (!handler.isBeingUpdated || text.toString().length == 1) {
                    selectSuitableLocation(handler)
                }
            }
            handler.isBeingUpdated = false
        }
    }

    private fun AutoCompleteTextView.clearText() {
        text.clear()
    }

    private fun AutoCompleteTextView.selectSuitableLocation(handler: AutoCompleteHandler<Location>) {
        if (handler.data.value.isNotEmpty()) {
            val suitableLocation = handler.data.value[0]

            setText(suitableLocation.name)
            handler.onItemSelected(suitableLocation)
            performCompletion()
        }
    }
}
