package fr.floz.epicurian.data.services.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.Relation

@Entity(
    tableName = "elements_cuisines",
    primaryKeys = ["element_id", "cuisine_id"],
    foreignKeys = [
        ForeignKey(
            entity = ElementEntity::class,
            parentColumns = ["id"],
            childColumns = ["element_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CuisineEntity::class,
            parentColumns = ["id"],
            childColumns = ["cuisine_id"]
        )
    ]
)
data class ElementCuisineCrossRef(
    @ColumnInfo("element_id")
    val elementId: Long,
    @ColumnInfo("cuisine_id")
    val cuisineId: Long
)

data class ElementWithCuisines(
    @Embedded
    val element: ElementEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            ElementCuisineCrossRef::class,
            parentColumn = "element_id",
            entityColumn = "cuisine_id"
        )
    )
    val cuisines: List<CuisineEntity>
)
