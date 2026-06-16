package com.example.financexs.domain.model

data class Moneda(
    val codigo: String,
    val simbolo: String,
    val nombre: String
)

val monedasDisponibles = listOf(
    Moneda("COP", "$", "Peso Colombiano"),
    Moneda("USD", "$", "Dólar Estadounidense"),
    Moneda("EUR", "€", "Euro"),
    Moneda("MXN", "$", "Peso Mexicano"),
    Moneda("ARS", "$", "Peso Argentino"),
    Moneda("BRL", "R$", "Real Brasileño"),
    Moneda("PEN", "S/", "Sol Peruano"),
    Moneda("CLP", "$", "Peso Chileno"),
    Moneda("GBP", "£", "Libra Esterlina"),
    Moneda("JPY", "¥", "Yen Japonés")
)
