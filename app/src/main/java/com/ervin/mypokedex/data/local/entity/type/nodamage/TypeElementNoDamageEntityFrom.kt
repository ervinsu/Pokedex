package com.ervin.mypokedex.data.local.entity.type.nodamage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TableTypeElementNoDamageFrom

@Entity(
    tableName = TableTypeElementNoDamageFrom.TypeNoDamageTableFrom,
    primaryKeys = [TableTypeElementNoDamageFrom.TypeID,
        TableTypeElementNoDamageFrom.TypeIDNoDamageFrom],
    indices = [Index(
        TableTypeElementNoDamageFrom.TypeIDNoDamageFrom,
        TableTypeElementNoDamageFrom.TypeID
    )]
)
data class TypeElementNoDamageEntityFrom(
    @ColumnInfo(name = TableTypeElementNoDamageFrom.TypeID)
    val ck_typeId: Int,
    @ColumnInfo(name = TableTypeElementNoDamageFrom.TypeIDNoDamageFrom)
    val ck_typeNoDamageId: Int
)