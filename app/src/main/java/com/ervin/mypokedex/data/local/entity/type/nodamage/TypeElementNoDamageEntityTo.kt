package com.ervin.mypokedex.data.local.entity.type.nodamage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TableTypeElementNoDamageTo

@Entity(
    tableName = TableTypeElementNoDamageTo.TypeNoDamageTableTo,
    primaryKeys = [TableTypeElementNoDamageTo.TypeID,
        TableTypeElementNoDamageTo.TypeIDNoDamageTo],
    indices = [Index(
        TableTypeElementNoDamageTo.TypeIDNoDamageTo,
        TableTypeElementNoDamageTo.TypeID
    )]
)
data class TypeElementNoDamageEntityTo(
    @ColumnInfo(name = TableTypeElementNoDamageTo.TypeID)
    val ck_typeId: Int,
    @ColumnInfo(name = TableTypeElementNoDamageTo.TypeIDNoDamageTo)
    val ck_typeNoDamageId: Int
)