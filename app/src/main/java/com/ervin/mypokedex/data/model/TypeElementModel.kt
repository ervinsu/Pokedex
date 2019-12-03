package com.ervin.mypokedex.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ervin.mypokedex.data.local.entity.TypeElementEntity
import com.ervin.mypokedex.data.local.entity.TypeElementNoDamageEntity
import com.ervin.mypokedex.data.local.entity.TypeElementNotEffectiveEntity
import com.ervin.mypokedex.data.local.entity.TypeElementSuperEffectiveEntity
import com.ervin.mypokedex.utils.TableTypeElement
import com.ervin.mypokedex.utils.TableTypeElementNoDamage
import com.ervin.mypokedex.utils.TableTypeElementNotEffective
import com.ervin.mypokedex.utils.TableTypeSuperElementEffective

class TypeElementModel {
    @Embedded
    lateinit var typeEntity:TypeElementEntity

    @Relation(
        parentColumn = TableTypeElement.TypeID,
        entity = TypeElementEntity::class,
        entityColumn = TableTypeElement.TypeID,
        associateBy = Junction(value = TypeElementSuperEffectiveEntity::class,
            parentColumn = TableTypeSuperElementEffective.TypeID,
            entityColumn = TableTypeSuperElementEffective.TypeIDSuperEffective)
    )
    var typeSuperEffectiveEntity: List<TypeElementEntity> = arrayListOf()


    @Relation(
        parentColumn = TableTypeElement.TypeID,
        entity = TypeElementEntity::class,
        entityColumn = TableTypeElement.TypeID,
        associateBy = Junction(value = TypeElementNotEffectiveEntity::class,
            parentColumn = TableTypeElementNotEffective.TypeID,
            entityColumn = TableTypeElementNotEffective.TypeIDNotEffective)
    )
    var typeNotSuperEffectiveEntity: List<TypeElementEntity> = arrayListOf()

    @Relation(
        parentColumn = TableTypeElement.TypeID,
        entity = TypeElementEntity::class,
        entityColumn = TableTypeElement.TypeID,
        associateBy = Junction(value = TypeElementNoDamageEntity::class,
            parentColumn = TableTypeElementNoDamage.TypeID,
            entityColumn = TableTypeElementNoDamage.TypeIDNoDamage)
    )
    var typeNoDamageEntity: List<TypeElementEntity> = arrayListOf()

}