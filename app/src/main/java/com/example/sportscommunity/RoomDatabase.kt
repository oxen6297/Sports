package com.example.sportscommunity

import android.content.Context
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity
class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo
    val email: String,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val birthday: String?,
    @ColumnInfo
    val nickname: String?,
    @ColumnInfo
    val phoneNumber: String?,
    @ColumnInfo
    val gender: String?
) {
    constructor(
        email: String,
        name: String,
        birthday: String?,
        nickname: String?,
        phoneNumber: String?,
        gender: String?
    ) : this(0, email, name, birthday, nickname, phoneNumber, gender)
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

@Database(entities = [UserProfile::class], version = 2)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao

    companion object{
        private var Instance: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase? {
            if (Instance == null){
                synchronized(UserDatabase::class){
                    Instance = Room.databaseBuilder(context,UserDatabase::class.java,"user-database").addMigrations(
                        MIGRATION_1_TO_2).fallbackToDestructiveMigration().build()
                }
            }
            return Instance
        }
    }
}

val MIGRATION_1_TO_2: Migration = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.run {
            execSQL("ALTER TABLE UserProfile ADD birthday VARCHAR")
            execSQL("ALTER TABLE UserProfile ADD nickname VARCHAR")
            execSQL("ALTER TABLE UserProfile ADD phoneNumber VARCHAR")
            execSQL("ALTER TABLE UserProfile ADD gender VARCHAR")
        }
    }
}