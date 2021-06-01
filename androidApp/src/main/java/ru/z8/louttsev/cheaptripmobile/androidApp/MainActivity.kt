package ru.z8.louttsev.cheaptripmobile.androidApp

import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.z8.louttsev.cheaptripmobile.androidApp.databinding.ActivityMainBinding
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model by viewModels<MainViewModel> {
            createWithFactory { MainViewModel(App.sLocationRepository) }
        }

        val binding = ActivityMainBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
            viewModel = model // TODO can suppress this?
        }

        setContentView(binding.root)

        with(binding) {
            with(originTextView) {
                threshold = 0
                setAdapter(
                    AutoCompleteLocationsListAdapter(this@MainActivity, model.oroginLocations)
                )
                addTextChangedListener { changedEditableText: Editable? ->
                    model.onOriginInputFieldTextChanged(changedEditableText.toString())
                }
                setOnItemClickListener { parent, _, position, _ ->
                    val selectedLocation = parent.getItemAtPosition(position) as Location
                    model.onOriginSelected(selectedLocation)
                    this.performCompletion()
                }
                // TODO Clear model and text (?) in item not selected
                // TODO Extract expression function AutocompleteTextView.setup(...)
            }

            originClearIcon.setOnClickListener {
                model.onOriginReset()
                binding.originTextView.setText("")
            }

            with(destinationTextView) {
                threshold = 0
                setAdapter(
                    AutoCompleteLocationsListAdapter(this@MainActivity, model.destinationLocations)
                )
                addTextChangedListener { changedEditableText: Editable? ->
                    model.onDestinationInputFieldTextChanged(changedEditableText.toString())
                }
                setOnItemClickListener { parent, _, position, _ ->
                    val selectedLocation = parent.getItemAtPosition(position) as Location
                    model.onDestinationSelected(selectedLocation)
                    this.performCompletion()
                }
            }

            destinationClearIcon.setOnClickListener {
                model.onDestinationReset()
                binding.destinationTextView.setText("")
            }

            clearButton.setOnClickListener {
                model.onOriginReset()
                binding.originTextView.setText("")
                model.onDestinationReset()
                binding.destinationTextView.setText("")
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
}
