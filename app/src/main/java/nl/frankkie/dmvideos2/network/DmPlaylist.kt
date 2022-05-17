package nl.frankkie.dmvideos2.network

class DmPlaylist {
    var total: Int? = null
    var list: List<DmPlaylistVideo>? = null
}

class DmPlaylistVideo {
    var id: String? = null
    var title: String? = null
    var channel: String? = null
    var owner: String? = null
}

/*
//Example JSON
{
page: 1,
limit: 100,
explicit: false,
total: 1,
has_more: false,
list: [
{
id: "x77zjps",
title: "MLP s09e01",
channel: "shortfilms",
owner: "x1gzy6k"
}
]
}
 */