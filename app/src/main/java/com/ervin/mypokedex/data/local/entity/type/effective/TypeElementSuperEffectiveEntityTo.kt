package com.ervin.mypokedex.data.local.entity.type.effective

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TableTypeElementSuperEffectiveTo

@Entity(
    tableName = TableTypeElementSuperEffectiveTo.TypeSuperEffectiveTableTo,
    primaryKeys = [TableTypeElementSuperEffectiveTo.TypeIDSuperEffectiveTo,
        TableTypeElementSuperEffectiveTo.TypeID],
    indices = [Index(
        TableTypeElementSuperEffectiveTo.TypeIDSuperEffectiveTo,
        TableTypeElementSuperEffectiveTo.TypeID
    )]
)
data class TypeElementSuperEffectiveEntityTo(
    @ColumnInfo(name = TableTypeElementSuperEffectiveTo.TypeID, index = true)
    val ck_typeId: Int,
    @ColumnInfo(name = TableTypeElementSuperEffectiveTo.TypeIDSuperEffectiveTo, index = true)
    val ck_typeEffectiveId: Int
)