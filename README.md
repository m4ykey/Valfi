Valfi is an app that allows you to search and save your favorite albums, discover new releases, and stay updated on the latest music news.

## Screenshots
| Home | Search | Detail | News |
|:-:|:-:|:-:|:-:|


## Helpful dependencies
- [Vico](https://github.com/patrykandpatrick/vico) - Generating charts
- [Firebase](https://firebase.google.com/) - Basic analytics and application monitoring itself
- [Coil](https://coil-kt.github.io/coil/) - Loading images
- [Moshi](https://github.com/square/moshi) - JSON serialization/deserialization library
- [Retrofit](https://github.com/square/retrofit) - HTTP client to communicate with the API.

## Project Setup
1. Clone repository and open project in the latest version of Android Studio.
2. Generate and import your `google-services.json` file and put it in the `/app`
3. Create `local.properties` and import it to `/app`
4. Add your [Spotify](https://developer.spotify.com/) SPOTIFY_CLIENT_ID, SPOTIFY_CLIENT_SECRET and [NewsApi](https://newsapi.org/) NEWS_API_KEY key in `local.properties`
```
SPOTIFY_CLIENT_ID = "YOUR_SPOTIFY_CLIENT_ID"
SPOTIFY_CLIENT_SECRET = "YOUR_SPOTIFY_CLIENT_SECRET"
NEWS_API_KEY = "YOUR_NEWS_API_KEY"
```

## Music news providers
- [RollingStone](https://www.rollingstone.com/)
- [Billboard](https://www.billboard.com/)
- [Pitchfork](https://pitchfork.com/)
- [NME](https://www.nme.com/)
- [Consequence](https://consequence.net/)
- [Stereogum](https://www.stereogum.com/)
- [The FADER](https://www.thefader.com/)

## Icons
The icons used in the application are from the website [svgrepo.com](https://www.svgrepo.com/) and [Google Icons](https://fonts.google.com/icons)