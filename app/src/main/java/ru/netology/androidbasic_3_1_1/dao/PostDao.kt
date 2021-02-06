package ru.netology.androidbasic_3_1_1.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.androidbasic_3_1_1.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun save(post: PostEntity)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)

    @Query("""
        UPDATE PostEntity SET 
            likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END, 
            likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END 
        WHERE ID = :id;
    """)
    fun likeById(id: Long)

//    @Query("UPDATE PostEntity SET shares = shares + 1 WHERE id = :id")
    @Query("UPDATE PostEntity SET content = content + 1 WHERE id = :id")
    fun shareById(id: Long)

    @Query("UPDATE PostEntity SET content = :postContent WHERE id = :id")
    fun updatePostContent(id: Long, postContent: String)
}