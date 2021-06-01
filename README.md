# MemoryGame

<img src="ui_design/MemoryGameUI.png" />

Colour Memory is an Android app consisting of a board which displays eight pairs of cards where each pair is of different colour. 
A player has to touch the cards to view the colours and match all the pairs. 

If the player matches the pair correctly, then:
1. he scores 2 points and the matched pair of cards is removed from the board
2. otherwise, he loses 1 point and the card is flipped to face down.

The player can also view his scores and rankings against other players.

# Project Setup
The remote repository of the app is available on the GitHub.
Clone the repo, open the project in Android Studio, and Run.
`https://github.com/adilkdev/MemoryGame.git`

# Architecture
Colour Memory app follows MVVM architecture to provide a clear flow of data and separation of components.

The app uses only local data and there’s no dependency on the internet. For persistent data storage the app uses Room Database for Android.

In MVVM the activity acts as View. All the view components are present in the activity. The state and data of the app is maintained in the view model. 
Also, all the logics reside in the view model.

In the activity, for observing the data residing in the view model, we are using live data, which enables to sync the user interface with the data
available in the view model instantly.

The app uses multithreading with Kotlin Coroutines, to take any possible load away from the UI thread to ensure smooth and
fluid user experience.

The app uses dependency injection so that the classes don’t
have to generate hard dependencies and also it helps to avoid
boilerplate.

# Dependencies

• Default Android : <br /> 
`implementation "org.jetbrains.kotlin:kotlin-stdlib: 1.5.10"` <br />
`implementation "androidx.core:core-ktx:1.5.0"` <br />
`implementation "androidx.appcompat:appcompat:1.3.0"` <br />
`implementation "com.google.android.material:material:1.3.0"` <br />
`implementation "androidx.constraintlayout:constraintlayout:2.0.4"` <br /> <br />

• Room Database <br />
`// Persistent data storage` <br />
`implementation("androidx.room:room-runtime: 2.3.0")` <br />
`kapt("androidx.room:room-compiler: 2.3.0")` <br />
`implementation("androidx.room:room-ktx: 2.3.0")` <br />

• Dagger-Hilt <br />
`Dependency injection` <br />
`implementation("com.google.dagger:hilt-android:2.35")` <br />
`kapt("com.google.dagger:hilt-android-compiler: 2.35")` <br />

• Lottie <br />
`// Animated content` <br />
` implementation "com.airbnb.android:lottie: 3.7.0"` <br />

• Truth <br />
`// Fluent assertions` <br />
`testImplementation "com.google.truth:truth:1.1.3"` <br />

• Unit Tests <br />
`testImplementation "junit:junit:4.13.2"` <br />

• Instrumented Tests <br />
`androidTestImplementation "org.jetbrains.kotlinx:kotlinxcoroutines-test:1.5.0"` <br />
`androidTestImplementation "com.google.truth:truth:1.1.3"` <br />
`androidTestImplementation "androidx.arch.core:core-testing:2.1.0"` <br />

`// Espresso` <br />
`def androidx_test_espresso = "3.3.0"` <br />
`androidTestImplementation "androidx.test.espresso:espressocore:$androidx_test_espresso"` <br />
`androidTestImplementation "androidx.test.espresso:espresso-contrib:$androidx_test_espresso"` <br />

`// androidx.test` <br />
`def androidx_test = "1.1.2"` <br />
`androidTestImplementation "androidx.test:runner:$androidx_test"` <br />
`androidTestImplementation "androidx.test:core:$androidx_test"` <br />
`androidTestImplementation "androidx.test.ext:junitktx:$androidx_test"` <br />

• RecyclerView <br />
`implementation("androidx.recyclerview:recyclerview:1.2.0")` <br />
 
# Unit Test 
The test class is present in the project which tests the methods of game activity’s view model. <br />
Unit test resides in this directory: <br />
`app/src/test/java/adil/app/memorygame/ui/game_screen/GameViewModelTest` <br />
[Go To Unit Test](https://github.com/adilkdev/MemoryGame/blob/master/app/src/test/java/adil/app/memorygame/ui/game_screen/GameViewModelTest.kt)

# Instrumentation Test
Instrumentation tests run on a device or an emulator and they can take advantage of the Android framework APIs. <br />
Instrumented test resides in this directory: <br />
`app/src/androidTest/java/adil/app/memorygame/data/local/db/dao/PlayerDaoTest` <br />
[Go To Instrumentation Test](https://github.com/adilkdev/MemoryGame/blob/master/app/src/androidTest/java/adil/app/memorygame/data/local/db/dao/PlayerDaoTest.kt)

# UI Test
Testing user interactions within a single app helps to ensure that users do not encounter unexpected results or have a poor
experience when interacting with your app. Espresso is used for UI Testing. <br />
UI test resides in this directory: <br />
`app/src/androidTest/java/adil/app/memorygame/ui/game_screen/GameActivityTest` <br />
[Go To UI Test](https://github.com/adilkdev/MemoryGame/blob/master/app/src/androidTest/java/adil/app/memorygame/ui/game_screen/GameActivityTest.kt)
