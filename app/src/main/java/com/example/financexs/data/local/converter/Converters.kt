package com.example.financexs.data.local.converter

import androidx.room.TypeConverter
import com.example.financexs.data.local.enums.PeriodoPresupuesto
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.toBigDecimalOrNull()

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): Long? =
        value?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    @TypeConverter
    fun toLocalDate(value: Long?): LocalDate? =
        value?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? =
        value?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? =
        value?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
        }

    @TypeConverter
    fun fromPeriodoPresupuesto(value: PeriodoPresupuesto?): String? = value?.name

    @TypeConverter
    fun toPeriodoPresupuesto(value: String?): PeriodoPresupuesto? =
        value?.let { PeriodoPresupuesto.valueOf(it) }
}
