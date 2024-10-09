package cat.mcgonzalc.buscadorandroidgithub

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.mcgonzalc.buscadorandroidgithub.ui.theme.BuscadorAndroidGithubTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RepositoryAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var usernameEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.repositoryRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        usernameEditText = findViewById(R.id.usernameEditText)
        searchButton = findViewById(R.id.searchButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)

        searchButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            if (username.isNotEmpty()) {
                fetchRepositories(service, username)
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchRepositories(service: GitHubService, username: String) {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        service.listRepos(username).enqueue(object : Callback<List<Repository>> {
            override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    val repositories = response.body() ?: emptyList()
                    adapter = RepositoryAdapter(repositories)
                    recyclerView.adapter = adapter
                } else {
                    showError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                showError("Network error: ${t.message}")
            }
        })
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}