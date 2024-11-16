package fr.floz.epicurean.domain.entities

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.Ignore
import fr.floz.epicurean.R


enum class ElementType(
    @Ignore
    @StringRes
    val label: Int,
    @Ignore
    @DrawableRes
    val icon: Int
) {
    UNKNOWN(R.string.element_type_unknown, R.drawable.baseline_unknown_24),
    BAR(R.string.element_type_bar, R.drawable.baseline_local_bar_24),
    CAFE(R.string.element_type_cafe, R.drawable.baseline_local_cafe_24),
    FAST_FOOD(R.string.element_type_fast_food, R.drawable.baseline_fast_food_24),
    FOOD_COURT(R.string.element_type_food_court, R.drawable.baseline_restaurant_24),
    ICE_CREAM(R.string.element_type_ice_cream, R.drawable.baseline_icecream_24),
    PUB(R.string.element_type_pub, R.drawable.baseline_pub_24),
    RESTAURANT(R.string.element_type_restaurant, R.drawable.baseline_restaurant_24),
}


fun String?.toElementType(): ElementType = when (this?.lowercase()) {
    "bar" -> ElementType.BAR
    "cafe" -> ElementType.CAFE
    "fast_food" -> ElementType.FAST_FOOD
    "food_court" -> ElementType.FOOD_COURT
    "ice_cream" -> ElementType.ICE_CREAM
    "pub" -> ElementType.PUB
    "restaurant" -> ElementType.RESTAURANT
    else -> ElementType.UNKNOWN
}