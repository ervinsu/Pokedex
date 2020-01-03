package com.ervin.mypokedex.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ervin.mypokedex.data.local.entity.type.TypeElementEntity
import com.ervin.mypokedex.data.local.entity.type.effective.TypeElementSuperEffectiveEntityFrom
import com.ervin.mypokedex.data.local.entity.type.effective.TypeElementSuperEffectiveEntityTo
import com.ervin.mypokedex.data.local.entity.type.halfeffective.TypeElementNotEffectiveEntityFrom
import com.ervin.mypokedex.data.local.entity.type.halfeffective.TypeElementNotEffectiveEntityTo
import com.ervin.mypokedex.data.local.entity.type.nodamage.TypeElementNoDamageEntityFrom
import com.ervin.mypokedex.data.local.entity.type.nodamage.TypeElementNoDamageEntityTo
import com.ervin.mypokedex.utils.*

class TypeElementModel {
    @Embedded
    lateinit var typeEntity: TypeElementEntity

    @Relation(
        parentColumn = TableTypeElement.TypeID,
        entity = TypeElementEntity::class,
        entityColumn = TableTypeElement.TypeID,
        associateBy = Junction(value = TypeElementSuperEffectiveEntityTo::class,
            parentColumn = TableTypeElementSuperEffectiveTo.TypeID,
            entityColumn = TableTypeElementSuperEffectiveTo.TypeIDSuperEffectiveTo)
    )
    var typeSuperEffectiveEntityTo: List<TypeElementEntity> = arrayListOf()


    @Relation(
        parentColumn = TableTypeElement.TypeID,
        entity = TypeElementEntity::class,
        entityColumn = TableTypeElement.TypeID,
        associateBy = Junction(value = TypeElementNotEffectiveEntityTo::class,
            parentColumn = TableTypeElementNotEffectiveTo.TypeID,
            entityColumn = TableTypeElementNotEffectiveTo.TypeIDNotEffectiveTo)
    )
    var typeNotSuperEffectiveEntityTo: List<TypeElementEntity> = arrayListOf()

    @Relation(
        parentColumn = TableTypeElement.TypeID,
        entity = TypeElementEntity::class,
        entityColumn = TableTypeElement.TypeID,
        associateBy = Junction(value = TypeElementNoDamageEntityTo::class,
            parentColumn = TableTypeElementNoDamageTo.TypeID,
            entityColumn = TableTypeElementNoDamageTo.TypeIDNoDamageTo)
    )
    var typeNoDamageEntityTo: List<TypeElementEntity> = arrayListOf()


    @Relation(
        parentColumn = TableTypeElement.TypeID,
        entity = TypeElementEntity::class,
        entityColumn = TableTypeElement.TypeID,
        associateBy = Junction(value = TypeElementSuperEffectiveEntityFrom::class,
            parentColumn = TableTypeElementSuperEffectiveFrom.TypeID,
            entityColumn = TableTypeElementSuperEffectiveFrom.TypeIDSuperEffectiveFrom)
    )
    var typeSuperEffectiveEntityFrom: List<TypeElementEntity> = arrayListOf()


    @Relation(
        parentColumn = TableTypeElement.TypeID,
        entity = TypeElementEntity::class,
        entityColumn = TableTypeElement.TypeID,
        associateBy = Junction(value = TypeElementNotEffectiveEntityFrom::class,
            parentColumn = TableTypeElementNotEffectiveFrom.TypeID,
            entityColumn = TableTypeElementNotEffectiveFrom.TypeIDNotEffectiveFrom)
    )
    var typeNotSuperEffectiveEntityFrom: List<TypeElementEntity> = arrayListOf()

    @Relation(
        parentColumn = TableTypeElement.TypeID,
        entity = TypeElementEntity::class,
        entityColumn = TableTypeElement.TypeID,
        associateBy = Junction(value = TypeElementNoDamageEntityFrom::class,
            parentColumn = TableTypeElementNoDamageFrom.TypeID,
            entityColumn = TableTypeElementNoDamageFrom.TypeIDNoDamageFrom)
    )
    var typeNoDamageEntityFrom: List<TypeElementEntity> = arrayListOf()

}