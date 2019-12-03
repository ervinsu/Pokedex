package com.ervin.mypokedex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TableTypeElementNotEffective


@Entity(
    tableName = TableTypeElementNotEffective.TypeNotEffectiveTable,
    primaryKeys = [TableTypeElementNotEffective.TypeID,
        TableTypeElementNotEffective.TypeIDNotEffective],
    indices = [Index(
        TableTypeElementNotEffective.TypeIDNotEffective,
        TableTypeElementNotEffective.TypeID
    )]
)
data class TypeElementNotEffectiveEntity(
    @ColumnInfo(name = TableTypeElementNotEffective.TypeID)
    val ck_typeId: Int,
    @ColumnInfo(name = TableTypeElementNotEffective.TypeIDNotEffective)
    val ck_typeNotEffectiveId: Int
)