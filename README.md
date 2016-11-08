# HackerNews
[![Build Status](https://travis-ci.org/Schinizer/HackerNews.svg?branch=development)](https://travis-ci.org/Schinizer/HackerNews)

A simple read only HackerNews client built using Dagger2, RxJava, Retrofit2 and AutoValue.

## Features
* API 9 -24
* MVP
* Repository Design Pattern
* Dependency Injection
* Reactive Streams
* Data Binding
* Pagination
* Orientation Change Support
* Presenter Unit Tests
* Proguard
* Travis CI

## Overview

* [News Feed](https://github.com/Schinizer/HackerNews/blob/development/app/src/main/java/com/schinizer/hackernews/features/newsfeed/README.md)
* [Comments](https://github.com/Schinizer/HackerNews/tree/development/app/src/main/java/com/schinizer/hackernews/features/comments)

## To Build
Clone the repo using your preferred client or run the following command
```
git clone https://github.com/Schinizer/HackerNews.git
```

Open and run the project using Android Studio or run the following command
```
./gradlew assembleDebug installDebug
```
to build and install the project to connected devices.

## License
```
Copyright 2016 Schinizer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
