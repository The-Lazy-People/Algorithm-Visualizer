package com.thelazypeople.algorithmvisualizer.markov

class TrainingDataKeeper {
    val dictFutureWord = mutableMapOf<String, MutableMap<String,Int>>()
    val dictCompleteWord = mutableMapOf<String, MutableMap<String,Int>>()
}