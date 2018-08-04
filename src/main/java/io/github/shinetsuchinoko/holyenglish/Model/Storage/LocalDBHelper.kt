package io.github.shinetsuchinoko.holyenglish.Model.Storage

import io.realm.Realm
import io.realm.kotlin.createObject

class LocalDBHelper {
    var count = 0
    var mRealm: Realm? = null

    fun storePath(path: String) {
        if(mRealm == null){
            mRealm = Realm.getDefaultInstance()
        }
        mRealm?.let{
            it.executeTransaction{
                it.createObject<CardObject>().apply{
                    this.path = path
                    this.id = count++
                }
            }
        }
    }
}