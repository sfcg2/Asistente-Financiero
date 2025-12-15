package com.example.asistentefinanciero

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.asistentefinanciero.data.repository.IngresoRepository
import com.example.asistentefinanciero.data.repository.UsuarioRepository
import com.example.asistentefinanciero.viewmodel.IngresoViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@OptIn(ExperimentalCoroutinesApi::class)
class IngresoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var ingresoRepository: IngresoRepository
    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var viewModel: IngresoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        ingresoRepository = mockk()
        usuarioRepository = mockk(relaxed = true)

        viewModel = IngresoViewModel(
            ingresoRepository = ingresoRepository,
            usuarioRepository = usuarioRepository
        )
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
        Dispatchers.resetMain()
    }


    // TEST 1: REGISTRO DE INGRESO CORRECTO
    @Test
    fun `guarda ingreso correctamente y actualiza saldo`() = runTest {

        // GIVEN (datos válidos)
        viewModel.actualizarCantidad("1000")
        viewModel.actualizarCategoria("Salario")
        viewModel.actualizarFecha("10/09/2024")
        viewModel.actualizarHora("08:30")
        viewModel.actualizarNombre("Pago mensual")

        coEvery {
            ingresoRepository.guardarIngreso(any(), any())
        } returns true

        // WHEN
        viewModel.guardarIngreso("user1")
        advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) {
            ingresoRepository.guardarIngreso(
                "user1",
                match { it.monto == 1000.0 && it.categoria == "Salario" }
            )
        }

        coVerify(exactly = 1) {
            usuarioRepository.incrementarSaldo("user1", 1000.0)
        }

        assert(viewModel.mensajeExito.value != null)
        assert(viewModel.cantidad.value.isEmpty())
        assert(viewModel.isLoading.value == false)
    }


    // TEST 2: VERIFICA SI LA CANTIDAD ES VACÍA
    @Test
    fun `muestra error si cantidad esta vacia`() = runTest {

        viewModel.actualizarCantidad("")
        viewModel.guardarIngreso("user1")
        advanceUntilIdle()

        assert(viewModel.mensaje.value == "La cantidad es requerida")

        coVerify(exactly = 0) {
            ingresoRepository.guardarIngreso(any(), any())
        }
    }


    // TEST 3: VERIFICA SI LA CANTIDAD ES INVALIDA
    @Test
    fun `muestra error si cantidad no es valida`() = runTest {

        viewModel.actualizarCantidad("-50")
        viewModel.actualizarCategoria("Salario")

        viewModel.guardarIngreso("user1")
        advanceUntilIdle()

        assert(viewModel.mensaje.value == "Ingresa una cantidad válida")
    }


    // TEST 4: FALLA EL GUARDADO
    @Test
    fun `muestra error si falla guardar ingreso`() = runTest {

        viewModel.actualizarCantidad("500")
        viewModel.actualizarCategoria("Extra")
        viewModel.actualizarFecha("12/09/2024")
        viewModel.actualizarHora("10:00")

        coEvery {
            ingresoRepository.guardarIngreso(any(), any())
        } returns false

        viewModel.guardarIngreso("user1")
        advanceUntilIdle()

        assert(viewModel.mensaje.value == "Error al guardar el ingreso")

        coVerify(exactly = 0) {
            usuarioRepository.incrementarSaldo(any(), any())
        }
    }
}

