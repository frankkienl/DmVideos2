package nl.frankkie.dmvideos2.network

class DmUserPlaylists {
    var error: DmUserPlaylistsError? = null
    var total: Int? = null
    var list: List<DmPlaylistInfo>? = null
}

class DmUserPlaylistsError {
    var code: Int? = null
}

class DmPlaylistInfo {
    var id: String? = null
    var name: String? = null
    var owner: String? = null
}

/*
//Example JSON
https://api.dailymotion.com/user/PioRuN_PL/playlists
{
page: 1,
limit: 10,
explicit: false,
has_more: true,
list: [
{
id: "x6l2xd",
name: "Ed, Edd i Eddy - Dubbing PL",
owner: "x1gzy6k"
},
{
id: "x6dbgg",
name: "MLP Season 9",
owner: "x1gzy6k"
},
{
id: "x5uwta",
name: "MLP Fundamentals of Magic",
owner: "x1gzy6k"
},
{
id: "x5qzv1",
name: "MLP Season 6",
owner: "x1gzy6k"
},
{
id: "x5obca",
name: "MLP Season 5 Napisy PL (26)",
owner: "x1gzy6k"
},
{
id: "x5nt3q",
name: "My Little Pony Season 8 (12)",
owner: "x1gzy6k"
},
{
id: "x5nf1x",
name: "My Little Pony: The Movie (complete - smooth cuts)",
owner: "x1gzy6k"
},
{
id: "x5m36g",
name: "Movie Trailers",
owner: "x1gzy6k"
},
{
id: "x5gsa7",
name: "MLP Season 7 English",
owner: "x1gzy6k"
},
{
id: "x5c7g4",
name: "MLP Season 4 (22)",
owner: "x1gzy6k"
}
]
}
 */