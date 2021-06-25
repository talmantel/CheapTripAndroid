/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.androidApp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.icerock.moko.mvvm.livedata.LiveData
import ru.z8.louttsev.cheaptripmobile.androidApp.R
import ru.z8.louttsev.cheaptripmobile.androidApp.databinding.ItemRouteBinding
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route

/**
 * Declares adapter for route list as result of searching.
 *
 * @param liveData Observable source of routes data
 */
class RouteListAdapter(
    liveData: LiveData<List<Route>>
) : RecyclerView.Adapter<RouteListAdapter.ViewHolder>() {
    private var mRoutes: List<Route>

    init {
        mRoutes = liveData.value
        liveData.addObserver {
            mRoutes = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val binding = ItemRouteBinding.inflate(LayoutInflater.from(context))

        with(binding) {
            root.layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            pathList.layoutManager = LinearLayoutManager(context)
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRoute = mRoutes[position]

        with(holder.binding) {
            val context = root.context

            model = currentRoute // ignore probably IDE error message "Cannot access class..."
            pathList.adapter = PathListAdapter(currentRoute.directPaths)
            executePendingBindings()

            root.setOnClickListener { openIndicator.isChecked = !openIndicator.isChecked }
            openIndicator.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    pathList.visibility = View.VISIBLE
                } else {
                    pathList.visibility = View.GONE
                }
            }
            openIndicator.isChecked = false

            transportIconContainer.removeAllViews()
            currentRoute.directPaths.forEach {
                val imageview = LayoutInflater.from(context).inflate(
                    R.layout.transport_icon_imageview,
                    transportIconContainer,
                    false
                ) as ImageView
                imageview.apply {
                    setImageResource(it.transportationType.imageResource.drawableResId)
                    layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                        setMargins(0, 8, 0, 8)
                    }
                }
                transportIconContainer.addView(imageview)
            }
        }
    }

    override fun getItemCount(): Int = mRoutes.size

    class ViewHolder(val binding: ItemRouteBinding) : RecyclerView.ViewHolder(binding.root)
}