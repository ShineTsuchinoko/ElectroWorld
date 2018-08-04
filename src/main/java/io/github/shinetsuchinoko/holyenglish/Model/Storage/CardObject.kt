package io.github.shinetsuchinoko.holyenglish.Model.Storage

import io.realm.RealmObject

private val DEFAULT_PATH = ""

open class CardObject: RealmObject() {
    var id : Int = -1
    var name :String = "Default Name"
    var path :String = DEFAULT_PATH
    var deck :String = "Default Deck"
    var flavorText : String = "Default Flavor"
}