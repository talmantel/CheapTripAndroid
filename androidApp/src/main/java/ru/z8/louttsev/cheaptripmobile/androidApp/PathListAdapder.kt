package ru.z8.louttsev.cheaptripmobile.androidApp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import ru.z8.louttsev.cheaptripmobile.androidApp.databinding.ItemPathBinding
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Path

class PathListAdapder(
    private val mPaths: List<Path>
) : RecyclerView.Adapter<PathListAdapder.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPathBinding.inflate(LayoutInflater.from(parent.context))

        binding.root.apply {
            layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPath = mPaths[position]

        with(holder.binding) {
            model = currentPath // ignore probably IDE error message "Cannot access class..."
            executePendingBindings()
        }
    }

    override fun getItemCount(): Int = mPaths.size

    class ViewHolder(val binding: ItemPathBinding) : RecyclerView.ViewHolder(binding.root)
}