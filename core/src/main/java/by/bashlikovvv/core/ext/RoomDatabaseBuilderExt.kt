package by.bashlikovvv.core.ext

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

fun <T : RoomDatabase> RoomDatabase.Builder<T>.preLoadData(sql: String): RoomDatabase.Builder<T> {
    return addCallback(
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL(sql)
            }
        }
    )
}