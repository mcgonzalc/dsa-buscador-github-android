package cat.mcgonzalc.buscadorandroidgithub

data class Repository(
    val id: Long,
    val name: String,
    val description: String?,
    val owner: Owner,
    val stargazers_count: Int
)


data class Owner(
    val login: String,
    val avatar_url: String
)