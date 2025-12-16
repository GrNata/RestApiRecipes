package com.grig.restapirecipes.service

//@ExtendWith(MockitoExtension::class)
//class RecipeServiceTest {
//
//    @Mock
//    lateinit var recipeRepository: RecipeRepository
//
//    @Mock
//    lateinit var categoryRepository: CategoryRepository
//
//    @Mock
//    lateinit var ingredientRepository: IngredientRepository
//
//    @Mock
//    lateinit var recipeIngredientRepository: RecipeIngredientRepository
//
//    @Mock
//    lateinit var stepRepository: StepRepository
//
//    @InjectMocks
//    lateinit var recipeService: RecipeService
//
//    @Test
//    fun `createRecipe should save recipe`() {
//        val category = Category(id = 1, name = "Desserts")
//        val ingredient = Ingredient(id = 1, name = "Sugar", unit = "2")
//
//        val request = CreateRecipeRequest(
//            name = "Cake",
//            description = "Sweet cake",
//            image = null,
//            categoryIds = listOf(1),
//            ingredients = listOf(CreateRecipeIngredientRequest(1, "200g")),
//            steps = listOf("Mix", "Bake")
//        )
//
//        whenever(categoryRepository.findAllById(listOf(1))).thenReturn(listOf(category))
//        whenever(ingredientRepository.findById(1)).thenReturn(Optional.of(ingredient))
//        whenever(recipeRepository.save(any())).thenAnswer { invocation ->
//            val recipe = invocation.arguments[0] as Recipe
//            // имитируем поведение Hibernate
//            Recipe(
//                id = 1L,
//                name = recipe.name,
//                description = recipe.description,
//                image = recipe.image,
//                categories = recipe.categories,
////                ingredients = recipe.ingredients,
//                steps = recipe.steps,
//                recipeIngredients = recipe.recipeIngredients
//            )
//        }
//
//        val result = recipeService.createRecipe(request)
//
//        assertEquals("Cake", result.name)
//        assertEquals(1, result.categories.size)
//        assertEquals(1, result.recipeIngredients.size)
//        assertEquals(2, result.steps.size)
//    }
//
//    @Test
//    fun `deleteRecipe should throw is not found`() {
//        whenever(recipeRepository.findById(99)).thenReturn(Optional.empty())
//
//        assertThrows<RecipeNotFoundException> {
//            recipeService.deleteRecipe(99)
//        }
//    }
//}