package cat.mcgonzalc.buscadorandroidgithub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RepositoryAdapter(private val repositories: List<Repository>) :
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.repositoryName)
        val descriptionTextView: TextView = view.findViewById(R.id.repositoryDescription)
        val starsTextView: TextView = view.findViewById(R.id.repositoryStars)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repository_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = repositories[position]
        holder.nameTextView.text = repository.name
        holder.descriptionTextView.text = repository.description ?: "No description"
        holder.starsTextView.text = "${repository.stargazers_count} ★"
    }

    override fun getItemCount() = repositories.size
}