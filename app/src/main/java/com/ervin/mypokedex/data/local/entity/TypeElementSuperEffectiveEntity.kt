package com.ervin.mypokedex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TableTypeSuperElementEffective

@Entity(
    tableName = TableTypeSuperElementEffective.TypeSuperEffectiveTable,
    primaryKeys = [TableTypeSuperElementEffective.TypeIDSuperEffective,
        TableTypeSuperElementEffective.TypeID],
    indices = [Index(
        TableTypeSuperElementEffective.TypeIDSuperEffective,
        TableTypeSuperElementEffective.TypeID
    )]
)
data class TypeElementSuperEffectiveEntity(
    @ColumnInfo(name = TableTypeSuperElementEffective.TypeID, index = true)
    val ck_typeId: Int,
    @ColumnInfo(name = TableTypeSuperElementEffective.TypeIDSuperEffective, index = true)
    val ck_typeEffectiveId: Int
)