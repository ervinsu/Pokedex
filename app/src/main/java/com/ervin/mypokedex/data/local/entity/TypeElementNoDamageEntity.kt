package com.ervin.mypokedex.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TableTypeElementNoDamage

@Entity(
    tableName = TableTypeElementNoDamage.TypeNoDamageTable,
    primaryKeys = [TableTypeElementNoDamage.TypeID,
        TableTypeElementNoDamage.TypeIDNoDamage],
    indices = [Index(
        TableTypeElementNoDamage.TypeIDNoDamage,
        TableTypeElementNoDamage.TypeID
    )]
)
data class TypeElementNoDamageEntity(
    @ColumnInfo(name = TableTypeElementNoDamage.TypeID)
    val ck_typeId: Int,
    @ColumnInfo(name = TableTypeElementNoDamage.TypeIDNoDamage)
    val ck_typeNoDamageId: Int
)