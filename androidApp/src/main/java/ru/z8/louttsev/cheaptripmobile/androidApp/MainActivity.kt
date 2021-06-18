package ru.z8.louttsev.cheaptripmobile.androidApp

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.icerock.moko.mvvm.livedata.LiveData
import ru.z8.louttsev.cheaptripmobile.androidApp.adapters.AutoCompleteLocationsListAdapter
import ru.z8.louttsev.cheaptripmobile.androidApp.adapters.RouteListAdapter
import ru.z8.louttsev.cheaptripmobile.androidApp.databinding.ActivityMainBinding
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.viewmodel.AutoCompleteHandler
import ru.z8.louttsev.cheaptripmobile.shared.viewmodel.MainViewModel

/**
 * Declares main UI controller.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        val model by viewModels<MainViewModel> {
            createWithFactory { MainViewModel(App.sLocationRepository, App.sRouteRepository) }
        }

        val binding = ActivityMainBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
            viewModel = model // ignore probably IDE error message "Cannot access class..."
        }

        setContentView(binding.root)

        supportActionBar?.apply {
            customView = layoutInflater.inflate(R.layout.action_bar, null)
            customView.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            displayOptions = DISPLAY_SHOW_CUSTOM
        }

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

            goButton.setup(
                isReady = model.routes.isReadyToBuild,
                listener = {
                    model.routes.build(emptyResultHandler = { showNoResultsMessage() })
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                }
            )

            with(routeList) {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = RouteListAdapter(model.routes.data)
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        itemPosition: Int,
                        parent: RecyclerView
                    ) {
                        outRect.bottom = resources.getDimension(R.dimen.route_item_margin).toInt()
                    }
                })
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
            AutoCompleteLocationsListAdapter(handler.data)
        )

        addTextChangedListener { changedEditableText: Editable? ->
            handler.onItemReset()
            handler.isBeingUpdated = true
            handler.onTextChanged(
                changedEditableText.toString(),
                emptyResultHandler = {
                    showNoResultsMessage()
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

    private fun showNoResultsMessage() {
        Toast.makeText(
            this@MainActivity,
            getString(R.string.no_data_error_message),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun AutoCompleteTextView.clearText() {
        text.clear()
    }

    private fun AutoCompleteTextView.selectSuitableLocation(handler: AutoCompleteHandler<Location>) {
        if (handler.data.value.isNotEmpty()) {
            val suitableLocation = handler.data.value.first()

            setText(suitableLocation.name)
            handler.onItemSelected(suitableLocation)
            performCompletion()
        }
    }

    private fun Button.setup(isReady: LiveData<Boolean>, listener: (View) -> Unit) {
        isReady.addObserver {
            if (it) {
                setOnClickListener(listener)
                setBackgroundColor(getColor(R.color.colorAccent))
            } else {
                setOnClickListener(null)
                setBackgroundColor(getColor(R.color.colorInactiveViewBackground))
            }
        }
    }
}
