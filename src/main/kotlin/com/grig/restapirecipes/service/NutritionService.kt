package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.response.IngredientWithAmountDto
import com.grig.restapirecipes.dto.response.UnitDto
import com.grig.restapirecipes.inregretion.openFoodFacts.OpenFoodFactsClient
import com.grig.restapirecipes.inregretion.openFoodFacts.UnitConverter
import com.grig.restapirecipes.model.Ingredient
import com.grig.restapirecipes.model.UnitEntity
import com.grig.restapirecipes.repository.IngredientRepository
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

//  Для калорий
@Service
class NutritionService(
    private val ingredientRepository: IngredientRepository,
    private val openFoodFactsClient: OpenFoodFactsClient,
    private val unitConverter: UnitConverter
) {

    fun getCaloriesForIngredient(
        ingredient: Ingredient,
        amount: Double,
        unit: UnitDto
    ) : Int? {

        val grams = unitConverter.toGram(amount, unit, ingredient.name) ?: return null

        println("Kcal_100: NutritionService: калории для ${ingredient.name}, grams=$grams")

//        "nutriments": {..."energy-kcal_100g": 400,..}
//        сначала в БД, за тем OFF - ru, если нет eng
        val kcal100 = ingredient.energyKcal100g?.also {
            println("Kcal_100: NutritionService: Calories from DB: $it kcal/100g")
        }
            ?: ingredient.name
                ?.substringBefore("(")
                ?.lowercase()?.
                let {
                    println("Kcal_100: NutritionService: Trying OFF RU: $it")
                    openFoodFactsClient.getCaloriesPer100g(it)
                }
            ?: ingredient.nameEng
                ?.substringBefore("(")
                ?.lowercase()
                ?.let {
                    println("Kcal_100: NutritionService: Trying OFF ENG: $it")
                    openFoodFactsClient.getCaloriesPer100g(it)
                }
            ?: null

        return kcal100?.let {
            ((it * grams) / 100).roundToInt()
        }
    }

    fun getCaloriesForIngredientWithAmountDto(
        ingredient: IngredientWithAmountDto
    ) : Int? {

        println("Kcal_100: NutritionService: название: ${ingredient.name}")

        val grams = unitConverter.toGram(ingredient.amount?.toDouble() ?: 0.0, ingredient.unit, ingredient.name) ?: return null

        println("Kcal_100: NutritionService: калории для ${ingredient.name}, grams=$grams")

//        "nutriments": {..."energy-kcal_100g": 400,..}
//        сначала в БД, за тем OFF - ru, если нет eng
        val kcal100 = ingredient.energyKcal100g?.also {
            println("Kcal_100: NutritionService: Calories from DB: $it kcal/100g")
        }
        // OFF RU
            ?: ingredient.name
                ?.substringBefore("(")
                ?.lowercase()?.
                let {
                    println("Kcal_100: NutritionService: Trying OFF RU: $it")
                    openFoodFactsClient.getCaloriesPer100g(it)
                }
//                OFF ENG fallback
            ?: ingredient.nameEng
                ?.substringBefore("(")
                ?.lowercase()
                ?.let {
                    println("Kcal_100: NutritionService: Trying OFF ENG: $it")
                    openFoodFactsClient.getCaloriesPer100g(it)
                }
            ?: null

        println("Kcal_100: NutritionService: START сохраняем в БД, если нашли")
        // сохраняем в БД, если нашли
        if (kcal100 != null) {
            ingredientRepository.findById(requireNotNull(ingredient.id))?.ifPresent { ingrEntity ->
                ingrEntity.energyKcal100g = kcal100
                ingredientRepository.save(ingrEntity)
            }
        }
        println("Kcal_100: NutritionService: END сохранили в БД, если нашли")
        println("Kcal_100: NutritionService: название: ${ingredient.name}, колории расчет: ${kcal100?.let { ((it * grams) / 100).roundToInt() }}")

        return kcal100?.let {
            ((it * grams) / 100).roundToInt()
        }
    }


    fun calculateCaloriesForIngredient(
        iningredientDto: IngredientWithAmountDto,
        amount: Double,
        unit: UnitDto
    ) : Int?{

        val grams = unitConverter.toGram(amount, unit, iningredientDto.name) ?: return null

        val kcal100 = iningredientDto.energyKcal100g
            ?: iningredientDto.name
                ?.substringBefore("")
                ?.lowercase()
                ?.let { openFoodFactsClient.getCaloriesPer100g(it) }
            ?: iningredientDto.nameEng
                ?.substringBefore("")
                ?.lowercase()
                ?.let { openFoodFactsClient.getCaloriesPer100g(it) }

        return kcal100?.let { ((it * grams) / 100).roundToInt() }
//            .also {
//// 🔹 lazy cache: если нашли kcal от OFF, сохраним в БД
//                if (it != null && iningredientDto.energyKcal100g == null) {
//                    ingredientService.updateKcal(iningredientDto.id, it)
//                }
//            }
    }
}