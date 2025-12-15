package com.example.asistentefinanciero

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.asistentefinanciero.viewmodel.EstadisticasViewModel
import com.example.asistentefinanciero.data.model.Egreso
import com.example.asistentefinanciero.data.model.Ingreso
import com.example.asistentefinanciero.data.repository.EgresoRepository
import com.example.asistentefinanciero.data.repository.IngresoRepository
import com.example.asistentefinanciero.viewmodel.FiltroEstadisticas
import com.google.firebase.Timestamp
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class EstadisticasViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var ingresoRepository: IngresoRepository
    private lateinit var egresoRepository: EgresoRepository
    private lateinit var viewModel: EstadisticasViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        ingresoRepository = mockk()
        egresoRepository = mockk()

        viewModel = EstadisticasViewModel(
            ingresoRepository = ingresoRepository,
            egresoRepository = egresoRepository,
            dispatcher = dispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // TEST 1: INGRESOS → porcentajes y datos para gráfica
    @Test
    fun `ingresos se agrupan y calculan porcentajes correctamente`() = runTest {

        val ingresos = listOf(
            ingreso(1000.0, "Salario", Calendar.MAY),
            ingreso(500.0, "Salario", Calendar.MAY),
            ingreso(500.0, "Freelance", Calendar.MAY)
        )

        mockIngresos(ingresos)
        mockEgresos(emptyList())

        viewModel.inicializarCargaDeDatos("user1")
        dispatcher.scheduler.advanceUntilIdle()

        val datos = viewModel.datosGrafico.value

        // Datos enviados a la gráfica
        Assert.assertEquals(2, datos.size)

        val salario = datos.first { it.categoria == "Salario" }
        val freelance = datos.first { it.categoria == "Freelance" }

        // Montos
        Assert.assertEquals(1500.0, salario.montoTotal, 0.0)
        Assert.assertEquals(500.0, freelance.montoTotal, 0.0)

        // Porcentajes
        Assert.assertEquals(75f, salario.porcentajeTotal, 0.1f)
        Assert.assertEquals(25f, freelance.porcentajeTotal, 0.1f)
    }

    // TEST 2: EGRESOS → porcentajes correctos
    @Test
    fun `egresos se agrupan y calculan porcentajes correctamente`() = runTest {

        val egresos = listOf(
            egreso(600.0, "Comida", Calendar.JUNE),
            egreso(400.0, "Transporte", Calendar.JUNE)
        )

        mockIngresos(emptyList())
        mockEgresos(egresos)

        viewModel.inicializarCargaDeDatos("user1")
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.cambiarFiltro(FiltroEstadisticas.EGRESOS)
        dispatcher.scheduler.advanceUntilIdle()

        val datos = viewModel.datosGrafico.value

        Assert.assertEquals(2, datos.size)

        val comida = datos.first { it.categoria == "Comida" }
        val transporte = datos.first { it.categoria == "Transporte" }

        Assert.assertEquals(60f, comida.porcentajeTotal, 0.1f)
        Assert.assertEquals(40f, transporte.porcentajeTotal, 0.1f)
    }

    // TEST 3: Verifica que el filtro decide ingresos/egresos
    @Test
    fun `filtro controla si se muestran ingresos o egresos`() = runTest {

        mockIngresos(
            listOf(ingreso(1000.0, "Salario", Calendar.MAY))
        )
        mockEgresos(
            listOf(egreso(300.0, "Comida", Calendar.MAY))
        )

        viewModel.inicializarCargaDeDatos("user1")
        dispatcher.scheduler.advanceUntilIdle()

        // INGRESOS
        Assert.assertEquals(
            "Salario",
            viewModel.datosGrafico.value.first().categoria
        )

        // EGRESOS
        viewModel.cambiarFiltro(FiltroEstadisticas.EGRESOS)
        dispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(
            "Comida",
            viewModel.datosGrafico.value.first().categoria
        )
    }

    // Helpers

    private fun mockIngresos(lista: List<Ingreso>) {
        every {
            ingresoRepository.obtenerIngresos(any(), captureLambda())
        } answers {
            lambda<(List<Ingreso>) -> Unit>().invoke(lista)
        }
    }

    private fun mockEgresos(lista: List<Egreso>) {
        every {
            egresoRepository.obtenerEgresos(any(), captureLambda())
        } answers {
            lambda<(List<Egreso>) -> Unit>().invoke(lista)
        }
    }

    private fun ingreso(
        monto: Double,
        categoria: String,
        mes: Int
    ): Ingreso {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2024)
            set(Calendar.MONTH, mes)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        return Ingreso(
            id = "id",
            usuarioId = "user1",
            monto = monto,
            nombre = "Ingreso test",
            categoria = categoria,
            fecha = Timestamp(cal.time)
        )
    }

    private fun egreso(
        monto: Double,
        categoria: String,
        mes: Int
    ): Egreso {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2024)
            set(Calendar.MONTH, mes)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        return Egreso(
            id = "id",
            usuarioId = "user1",
            monto = monto,
            nombre = "Egreso test",
            categoria = categoria,
            fecha = Timestamp(cal.time)
        )
    }
}
