package com.win_vkey.startai.winvkey.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.win_vkey.startai.winvkey.data_class.Key
import com.win_vkey.startai.winvkey.data_class.Settings
import org.jetbrains.anko.db.*
import java.net.URL
import java.sql.Types.BOOLEAN

/**
 * @Author: AiMin
 * @Description:Database that save key and Settings
 * @Date: Created in 22:55 2018/10/9
 * @Modified By:
 */

class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, ctx.getDatabasePath("set.db").absolutePath) {
    companion object {
        private var instance: MySqlHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MySqlHelper {
            if (instance == null) {
                instance = MySqlHelper(ctx.applicationContext)
            }
            println(ctx.getDatabasePath("set.db"))
            return instance!!
        }
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Settings", true,
                "host" to TEXT,
                "port" to INTEGER)

        db.createTable("Keys", true,
                "code" to INTEGER + PRIMARY_KEY + UNIQUE,
                "name" to TEXT,
                "is_favor" to INTEGER)

        db.insert("Settings",
                "host" to "127.0.0.1", "port" to 8889)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }


    public fun saveKey(key:Key) {
        this.use {
            insert("Keys",
                    "name" to key.name, "code" to key.code,"is_favor" to key.isFavor)
        }
    }

    public fun getAllKey(): List<Key> {
        return this.getKey(null)
    }


    public fun getKey(isFavor:Boolean?=null):List<Key>{
        return this.use {
            var res = select("Keys")
            return@use when(isFavor){
                null -> res;
                true -> res.whereArgs("is_favor > 0")
                false -> res.whereArgs("is_favor = 0")
            }.parseList(classParser<Key>())
        }
    }


    public fun updateKey(oldKey:Key,newKey:Key) {
        this.use {
            update("Keys",
                    "name" to newKey.name, "code" to newKey.code,"is_favor" to newKey.isFavor)
                    .whereArgs("code = {code}", "code" to oldKey.code)
                    .exec()
        }
    }

    public fun deleteKey(key:Key){
        this.use{
            delete("Keys","code={code}","code" to key.code)

        }
    }

    /*
        saving setting to database using text,like:
            http://192.168.1.1:5555
            but we only need
        @throws URL
     */
    public fun saveSettings(s: Settings) {
        this.use {
            update("Settings",
                    "host" to s.host,
                    "port" to s.port)
                    .exec()
        }
    }

    public fun getSettings(): Settings {
        return this.use {
            val parser = rowParser { host: String, port: Int ->
                return@rowParser Settings(host, port)
            }
            select("Settings").parseSingle(parser)
        }
    }
}

val Context.database: MySqlHelper
    get() = MySqlHelper.getInstance(applicationContext)