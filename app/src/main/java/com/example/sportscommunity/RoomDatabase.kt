package com.example.sportscommunity

import android.content.Context
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.navercorp.nid.oauth.NidOAuthErrorCode

@Entity
class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo
    val email: String,

    @ColumnInfo val name: String
) {
    constructor(email: String, name: String) : this(0, email, name)
}

@Dao
interface UserProfileDao {
    @Insert(onConflict = REPLACE)
    fun insert(userProfile: UserProfile)

    @Query("SELECT * FROM UserProfile")
    fun getAll(): List<UserProfile>

    @Query("DELETE FROM userprofile WHERE id = :email")
    fun delete(email: String)
}

@Database(entities = [UserProfile::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao

}