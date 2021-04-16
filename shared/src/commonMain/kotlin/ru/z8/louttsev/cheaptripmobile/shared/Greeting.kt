package ru.z8.louttsev.cheaptripmobile.shared


class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}
