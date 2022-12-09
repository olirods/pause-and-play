<p align="center"><img src="https://user-images.githubusercontent.com/49884623/206701504-e6955a60-1112-49c2-988d-54cb4370abb2.png"></p>

Pause and Play is a location based song guessing game designed for the Android platform. In the game, users will have to guess the title and artist of a song, collecting the song lyrics from locations based around the Bay Campus, while they are playing other cultural music mini-games in order to be able to get those lyric lines.

There are two masters of the game: ’Play’ and ’Pause’. Users will have to choose one of them to be their guides along all the game. Depending on which master they choose, users will play one of these game modes:

* *’Current’ mode* (choosing ’Play’): based on songs taken from weekly sales data on the week beginning on the 30th September.
* *’Classic’ mode* (choosing ’Pause’): based on songs that have been popular over the last 50 years.

## Requirements

The application will run on any mobile phone with a higher version than Android 8.0 (API level 26). The device will have to be connected to the Internet (WiFi or 4G) in every moment (if the user loses the data connectivity while he is playing the game, it will be counted as if he had given up).

## Functionality

In the following sections, the functionality of the game will be explained, focusing on the most important aspects of the game.

### Collecting lyric lines
When the user selects the desired game mode, the timer is activated (this will be important for the final point count) and the game starts. The player will have to walk to the locations of the lyric lines, which are shown on the map. Once the user has approached that position, the master will challenge him to a mini-game:

* If the player wins the game, he will get the line and will be able to run to another one.
* On the contrary, if he loses it, that lyric line in that location will get blocked and the user should
go to another different one in the map.

The application will work giving the user firstly the most difficult and less clarifying lyric lines, and then giving him easier ones. This is so that the game is not too simple.

### Playing mini-games

The idea is to make the game more entertaining, so it does not only consist of walking to one place to another and guessing a song, and to add more musical features to it. They are cultural music mini-games. Users will have to demonstrate their musical knowledge.

Players will have the option at all the times to leave the mini-game and return to the map. If they do that, it will be counted as if they had lost the mini-game.

### Giving up the game

The user will be able to leave the game at any time, in the event that he can’t determine the song or he simply doesn’t want to play anymore, pressing the corresponding button. However, if the player does it he will lose lots of points from his account (all about points is explained in below section __Adding points__).

It will work in the same way if the user leaves the app to another one on his mobile phone. The game will detect this and will consider that he has given up. This procedure is used in order to prevent users from trying to look for the answer on the Internet.

### Attempting to guess the song

Since the first lyric line collected, the user will be able to try to guess the song, pressing the ’Solve!’ button. The master of the game will ask the player to introduce the artist and the title of the song:

* If the artist and title given is correct, without any misspelled mistakes, the player will have won the game and the application will show him the final point count.
* If the artist’s name or the title is incorrect, the player will be able to continue trying to guess it more times. Nevertheless, these mistakes will be count in the score.

### Adding points
Although the main objective of the game is to guess the song, players can get it better or worse. This will be measured in the final point count that is displayed to the user once he has guessed the title and artist.

The points will be added or subtracted as follows:

```
+ 200 points for guessing the artist and title of the song
- 1 point for each minute spent in the game 
- 10 points for each lyric line collected
- 30 points for each mini-game lost
- 40 points for each guessing mistake
- 500 points for giving up the game
```

### Playing different language modes
As well as the two game modes, the application will bring another two different modes basing on the language and the music culture of each country:

* English mode. The basic one. Users will play in English and with the most popular songs listened in the UK.
* Spanish mode. Players will have the option of playing in Spanish and with a selection of songs and artists listened in Spain.
The language mode will be selected automatically since the beginning according to the mobile phone default language, but the user will be able to change this in the application settings.

## Application Theme used

The App Theme selected is a Material Light one, characterized by a light steel blue as the primary color and a crimson red as the secondary color. The navigation of the game is done through an App Bar

<p align="center"><img src="https://user-images.githubusercontent.com/49884623/206703244-89215063-6199-42c1-abb6-9d4664230916.png"></p>

## Screenshots (video)


