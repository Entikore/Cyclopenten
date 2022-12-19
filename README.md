<h1>
<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/preview/cyclopenten_darkmode.gif" width="320">
  <source media="(prefers-color-scheme: light)" srcset="/preview/cyclopenten_lightmode.gif" width="320">
  <img alt="Shows Cyclopenten Logo." src="/preview/cyclopenten_lightmode.gif" width="320">
</picture>
</h1>

<p>
  <img src="https://img.shields.io/github/actions/workflow/status/Entikore/Cyclopenten/android.yml?branch=main"/>
  <img src="https://img.shields.io/badge/API%20level-24%2B-brightgreen"/>
  <img src="https://img.shields.io/github/license/Entikore/Cyclopenten?color=informational"/>
</p>

This is a demo application to try modern Android development using Compose, Hilt, Coroutines, Flow,
Jetpack and Material Design based on MVVM architecture.

## The game

Cyclopenten is a simple quiz about the periodic table.

The player can choose between two difficulties when starting a new round.

If the player wants to pause the game, he can close the app and continue the game next time by
pressing the Continue button.

The game supports simple sound effects and background music, which can be adjusted via settings .

<p align="center">
  <img src="/preview/home_screen.png" width="160"/>
  <img src="/preview/settings_screen.png" width="160"/>
  <img src="/preview/new_game_screen.png" width="160"/>
</p>

If the easy difficulty is selected, the player has 3 lives and must name the element from a
selection of 4 choices. If the hard difficulty is selected, the player has 4 lives and must enter
the correct name of the element. The score increases if the player answers correctly, otherwise one
life is deducted. However, correct answers in hard mode are worth more points.

The game ends when all elements of the periodic table have been named or no lives are left. If the
score is among the 10 highest scores, the player can enter a name to be listed in the scoreboard.

<p align="center">
  <img src="/preview/easy_game_screen.png" width="160"/>
  <img src="/preview/hard_game_screen.png" width="160"/>
  <img src="/preview/game_over_screen.png" width="160"/>
</p>

### Preview

A small preview of the game, captured on an emulator.

<p align="center">
  <img src="/preview/preview.gif" align="center" width="240"/>
</p>

## Dependencies

- [Compose](https://developer.android.com/jetpack/androidx/releases/compose) - Define your UI
  programmatically with composable functions that describe its shape and data dependencies.
- [Activity](https://developer.android.com/jetpack/androidx/releases/activity) - Access composable
  APIs built on top of Activity.
- [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - Build
  lifecycle-aware components that can adjust behavior based on the current lifecycle state of an
  activity or fragment.
- [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation) - Build and
  structure your in-app UI, handle deep links, and navigate between screens.
- [Hilt AndroidX](https://developer.android.com/jetpack/androidx/releases/hilt) - Extend the
  functionality of Dagger Hilt to enable dependency injection of certain classes from the androidx
  libraries.
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - Create, store, and manage
  persistent data backed by a SQLite database.
- [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore) - Store data
  asynchronously, consistently, and transactionally, overcoming some of the drawbacks of
  SharedPreferences.
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html): - Library for
  coroutines that contains a number of high-level coroutine-enabled primitives.
- [Hilt](https://dagger.dev/hilt/): - Hilt provides a standard way to incorporate Dagger dependency
  injection into an Android application.
- [Timber](https://github.com/JakeWharton/timber): - A logger with a small, extensible API which
  provides utility on top of Android's normal Log class.
- [Moshi](https://github.com/square/moshi): - A modern JSON library for Kotlin and Java.
- [Gson](https://github.com/google/gson): - A Java serialization/deserialization library to convert
  Java Objects into JSON and back

## License

All the code available under the MIT License. See [LICENSE](LICENSE).

```
MIT License

Copyright (c) 2022 Entikore

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```