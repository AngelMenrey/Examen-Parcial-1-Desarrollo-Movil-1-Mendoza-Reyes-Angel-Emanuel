package com.example.examenparcial1mendozareyesangelemanuel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var cotizaciones: Array<Cotizacion>
    private lateinit var titulo: TextView
    private lateinit var folio: EditText
    private lateinit var marca: EditText
    private lateinit var modelo: EditText
    private lateinit var tipotransimision: EditText
    private lateinit var plazo: EditText

    private lateinit var registrar: Button
    private lateinit var cotizar: Button
    private lateinit var limpiar: Button

    private lateinit var mostrar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cotizaciones = Array(20) { Cotizacion("", "", "", "", "", 0, 0, 0) }

        titulo = findViewById(R.id.idTitulo)
        folio = findViewById(R.id.idFolio)
        marca = findViewById(R.id.idMarca)
        modelo = findViewById(R.id.idModelo)
        tipotransimision = findViewById(R.id.idTipoTransmision)
        plazo = findViewById(R.id.idPlazo)

        registrar = findViewById(R.id.btnRegistrar)
        cotizar = findViewById(R.id.btnCotizar)
        limpiar = findViewById(R.id.btnLimpiar)

        mostrar = findViewById(R.id.idMostrar)

        registrar.setOnClickListener {
            registrarCotizacion()
        }

        cotizar.setOnClickListener {
            cotizarCotizacion()
        }

        limpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun registrarCotizacion() {
        val folioText = folio.text.toString()
        val marcaText = marca.text.toString()
        val modeloText = modelo.text.toString()
        val tipoTransmisionText = tipotransimision.text.toString()
        val plazoText = plazo.text.toString()

        if (folioText.isEmpty() || marcaText.isEmpty() || modeloText.isEmpty() ||
            tipoTransmisionText.isEmpty() || plazoText.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val enganche = calcularEnganche(tipoTransmisionText)
        val mensualidad = calcularMensualidad(cotizaciones.size, tipoTransmisionText)
        val comision = calcularComision(tipoTransmisionText)

        val nuevaCotizacion = Cotizacion(folioText, marcaText, modeloText, tipoTransmisionText,
            plazoText, enganche, mensualidad, comision)

        val posicionDisponible = obtenerPosicionDisponible()
        if (posicionDisponible != -1) {
            cotizaciones[posicionDisponible] = nuevaCotizacion
            Toast.makeText(this, "Cotización registrada correctamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No hay posiciones disponibles en el arreglo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cotizarCotizacion() {
        val folioText = folio.text.toString()
        if (folioText.isEmpty()) {
            Toast.makeText(this, "El campo Folio no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        val cotizacion = buscarCotizacion(folioText)
        if (cotizacion != null) {
            mostrar.text = "Marca: ${cotizacion.marca}\n" +
                    "Modelo: ${cotizacion.modelo}\n" +
                    "Tipo de Transmisión: ${cotizacion.tipoTransmision}\n" +
                    "Plazo: ${cotizacion.plazo}\n" +
                    "Enganche: ${cotizacion.enganche}\n" +
                    "Mensualidad: ${cotizacion.mensualidad}\n" +
                    "Comisión: ${cotizacion.comision}"
        } else {
            mostrar.text = "Cotización no encontrada"
        }
    }

    private fun limpiarCampos() {
        folio.text.clear()
        marca.text.clear()
        modelo.text.clear()
        tipotransimision.text.clear()
        plazo.text.clear()
        mostrar.text = ""
    }

    private fun calcularEnganche(tipoTransmision: String): Int {
        return if (tipoTransmision == "Manual") {
            350000 * 30 / 100
        } else {
            400000 * 30 / 100
        }
    }

    private fun calcularMensualidad(plazo: Int, tipoTransmision: String): Int {
        val costoBase = if (tipoTransmision == "Manual") 350000 else 400000
        val porcentajeRestante = (100 - 30) / 100.0
        return (costoBase * porcentajeRestante / plazo).toInt()
    }

    private fun calcularComision(tipoTransmision: String): Int {
        return if (tipoTransmision == "Manual") {
            350000 * 2 / 100
        } else {
            400000 * 2 / 100
        }
    }

    private fun obtenerPosicionDisponible(): Int {
        for (i in cotizaciones.indices) {
            if (cotizaciones[i].folio.isEmpty()) {
                return i
            }
        }
        return -1
    }

    private fun buscarCotizacion(folio: String): Cotizacion? {
        for (cotizacion in cotizaciones) {
            if (cotizacion.folio == folio) {
                return cotizacion
            }
        }
        return null
    }
}
