//package com.grig.restapirecipes.favorite.model
//
//import com.grig.restapirecipes.model.Recipe
//import com.grig.restapirecipes.user.model.User
//import jakarta.persistence.*
//import java.util.Optional
//
//@Entity
//@Table(name = "user_favorite")
//class Favorite(
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @EmbeddedId
//    val id: Long? = null,
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    val user: User,
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "recipe_id", nullable = false)
//    val recipe: Recipe
//)