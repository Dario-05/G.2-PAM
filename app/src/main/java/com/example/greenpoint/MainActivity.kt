package com.example.greenpoint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.greenpoint.ui.theme.GreenPointTheme

// ─── Navigation ──────────────────────────────────────────────────────────────

enum class Screen { Login, Register, Dashboard, Reservation, Menu, Cart }

// ─── Data models ─────────────────────────────────────────────────────────────

data class MenuItem(
    val name: String,
    val price: String,
    val imageUrl: String,
    val category: String
)

data class CartItem(
    val menuItem: MenuItem,
    var quantity: Int = 1
)

// ─── Menu data ────────────────────────────────────────────────────────────────

val menuData: List<MenuItem> = listOf(
    // HAMBURGUESAS
    MenuItem("Hamb. Clásica Simple",  "S/ 17.90", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=500", "HAMBURGUESAS"),
    MenuItem("Hamb. Clásica Doble",   "S/ 23.90", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=500", "HAMBURGUESAS"),
    MenuItem("Cheeseburger Simple",   "S/ 18.90", "https://images.unsplash.com/photo-1550547660-d9450f859349?q=80&w=500", "HAMBURGUESAS"),
    MenuItem("Cheeseburger Doble",    "S/ 24.90", "https://images.unsplash.com/photo-1550547660-d9450f859349?q=80&w=500", "HAMBURGUESAS"),
    MenuItem("Bacon Burger",          "S/ 22.90", "https://images.unsplash.com/photo-1553979459-d2229ba7433b?q=80&w=500", "HAMBURGUESAS"),
    MenuItem("BBQ Burger",            "S/ 24.90", "https://images.unsplash.com/photo-1553979459-d2229ba7433b?q=80&w=500", "HAMBURGUESAS"),
    MenuItem("Mushroom Swiss",        "S/ 25.90", "https://images.unsplash.com/photo-1609167830220-7164aa360951?q=80&w=500", "HAMBURGUESAS"),
    MenuItem("Crispy Chicken Burger", "S/ 21.90", "https://images.unsplash.com/photo-1606755962773-d324e0a13086?q=80&w=500", "HAMBURGUESAS"),
    // PAPAS
    MenuItem("Papas Fritas Pequeñas", "S/ 8.90",  "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?q=80&w=500", "PAPAS"),
    MenuItem("Papas Fritas Medianas", "S/ 10.90", "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?q=80&w=500", "PAPAS"),
    MenuItem("Papas Fritas Grandes",  "S/ 12.90", "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?q=80&w=500", "PAPAS"),
    MenuItem("Papas Cargadas",        "S/ 15.90", "https://images.unsplash.com/photo-1628294895950-9805252327bc?q=80&w=500", "PAPAS"),
    MenuItem("Wedges de Papa",        "S/ 13.90", "https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?q=80&w=500", "PAPAS"),
    // BEBIDAS
    MenuItem("Gaseosa Personal",      "S/ 5.90",  "https://images.unsplash.com/photo-1581098365948-6a5a912b7a49?q=80&w=500", "BEBIDAS"),
    MenuItem("Agua Mineral",          "S/ 4.90",  "https://images.unsplash.com/photo-1548839140-29a749e1cf4d?q=80&w=500", "BEBIDAS"),
    MenuItem("Jugo Natural",          "S/ 8.90",  "https://images.unsplash.com/photo-1621506289937-a8e4df240d0b?q=80&w=500", "BEBIDAS"),
    MenuItem("Milkshake Vainilla",    "S/ 12.90", "https://images.unsplash.com/photo-1572490122747-3968b75cc699?q=80&w=500", "BEBIDAS"),
    MenuItem("Milkshake Chocolate",   "S/ 12.90", "https://images.unsplash.com/photo-1572490122747-3968b75cc699?q=80&w=500", "BEBIDAS"),
    // POSTRES
    MenuItem("Brownie con Helado",    "S/ 11.90", "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?q=80&w=500", "POSTRES"),
    MenuItem("Cheesecake",            "S/ 12.90", "https://images.unsplash.com/photo-1533134242443-d4fd215305ad?q=80&w=500", "POSTRES"),
    MenuItem("Helado 2 Bolas",        "S/ 9.90",  "https://images.unsplash.com/photo-1580915411954-282cb1b0d780?q=80&w=500", "POSTRES"),
)

// ─── Activity ─────────────────────────────────────────────────────────────────

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreenPointTheme {
                MainApp()
            }
        }
    }
}

// ─── App root ─────────────────────────────────────────────────────────────────

@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf(Screen.Login) }
    var cart by remember { mutableStateOf<List<CartItem>>(emptyList()) }

    when (currentScreen) {
        Screen.Login -> LoginScreen(
            onLogin    = { currentScreen = Screen.Dashboard },
            onRegister = { currentScreen = Screen.Register }
        )
        Screen.Register -> RegisterScreen(
            onRegisterSuccess = { currentScreen = Screen.Login },
            onBack            = { currentScreen = Screen.Login }
        )
        Screen.Dashboard -> DashboardScreen(
            onNavigateToReserva = { currentScreen = Screen.Reservation },
            onNavigateToCarta   = { currentScreen = Screen.Menu },
            onLogout            = { currentScreen = Screen.Login }
        )
        Screen.Reservation -> ReservationScreen(
            onBack = { currentScreen = Screen.Dashboard }
        )
        Screen.Menu -> MenuScreen(
            cartSize   = cart.sumOf { it.quantity },
            onAddToCart = { item ->
                val existing = cart.find { it.menuItem == item }
                cart = if (existing != null) {
                    cart.map { if (it.menuItem == item) it.copy(quantity = it.quantity + 1) else it }
                } else {
                    cart + CartItem(item)
                }
            },
            onViewCart = { currentScreen = Screen.Cart },
            onBack     = { currentScreen = Screen.Dashboard }
        )
        Screen.Cart -> CartScreen(
            items      = cart,
            onRemove   = { item ->
                cart = cart.filter { it.menuItem != item.menuItem }
            },
            onCheckout = {
                cart = emptyList()
                currentScreen = Screen.Dashboard
            },
            onBack     = { currentScreen = Screen.Menu }
        )
    }
}

// ─── Shared components ────────────────────────────────────────────────────────

@Composable
fun LogoComponent(modifier: Modifier = Modifier) {
    Column(
        modifier        = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF4CAF50), Color(0xFF1B5E20))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = Icons.Default.Eco,
                contentDescription = "Logo",
                tint               = Color.White,
                modifier           = Modifier.size(44.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text       = "GreenPoint",
            style      = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color      = Color(0xFF2E7D32)
        )
        Text(
            text  = "Burger & More",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF66BB6A)
        )
    }
}

@Composable
fun GreenTextField(
    label          : String,
    value          : String,
    onValueChange  : (String) -> Unit,
    modifier       : Modifier = Modifier,
    isPassword     : Boolean = false,
    keyboardType   : KeyboardType = KeyboardType.Text,
    leadingIcon    : ImageVector? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label         = { Text(label) },
        modifier      = modifier.fillMaxWidth(),
        singleLine    = true,
        shape         = RoundedCornerShape(12.dp),
        leadingIcon   = leadingIcon?.let { { Icon(it, null, tint = Color(0xFF4CAF50)) } },
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon  = if (isPassword) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                }
            }
        } else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = Color(0xFF4CAF50),
            focusedLabelColor    = Color(0xFF4CAF50),
            cursorColor          = Color(0xFF4CAF50)
        )
    )
}

@Composable
fun GreenButton(
    text     : String,
    onClick  : () -> Unit,
    modifier : Modifier = Modifier,
    enabled  : Boolean = true
) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape  = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ─── Login Screen ─────────────────────────────────────────────────────────────

@Composable
fun LoginScreen(onLogin: () -> Unit, onRegister: () -> Unit) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBF9))
    ) {
        LazyColumn(
            modifier            = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                LogoComponent()
                Spacer(Modifier.height(40.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors   = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Iniciar Sesión",
                            style      = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFF1B5E20)
                        )

                        GreenTextField(
                            label         = "Correo electrónico",
                            value         = email,
                            onValueChange = { email = it; showError = false },
                            keyboardType  = KeyboardType.Email,
                            leadingIcon   = Icons.Default.Email
                        )
                        GreenTextField(
                            label         = "Contraseña",
                            value         = password,
                            onValueChange = { password = it; showError = false },
                            isPassword    = true,
                            leadingIcon   = Icons.Default.Lock
                        )

                        if (showError) {
                            Text(
                                "Completa todos los campos",
                                color  = MaterialTheme.colorScheme.error,
                                style  = MaterialTheme.typography.bodySmall
                            )
                        }

                        GreenButton("Ingresar", onClick = {
                            if (email.isBlank() || password.isBlank()) showError = true
                            else onLogin()
                        })

                        TextButton(
                            onClick  = onRegister,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "¿No tienes cuenta? Regístrate",
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Register Screen ──────────────────────────────────────────────────────────

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onBack: () -> Unit) {
    var name            by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError       by remember { mutableStateOf(false) }
    var errorMsg        by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBF9))
    ) {
        LazyColumn(
            modifier            = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(48.dp))
                LogoComponent()
                Spacer(Modifier.height(32.dp))

                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            "Crear cuenta",
                            style      = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFF1B5E20)
                        )

                        GreenTextField("Nombre completo", name, { name = it; showError = false },
                            leadingIcon = Icons.Default.Person)
                        GreenTextField("Correo electrónico", email, { email = it; showError = false },
                            keyboardType = KeyboardType.Email, leadingIcon = Icons.Default.Email)
                        GreenTextField("Contraseña", password, { password = it; showError = false },
                            isPassword = true, leadingIcon = Icons.Default.Lock)
                        GreenTextField("Confirmar contraseña", confirmPassword,
                            { confirmPassword = it; showError = false },
                            isPassword = true, leadingIcon = Icons.Default.Lock)

                        if (showError) {
                            Text(errorMsg, color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall)
                        }

                        GreenButton("Registrarse", onClick = {
                            when {
                                name.isBlank() || email.isBlank() || password.isBlank() -> {
                                    errorMsg = "Completa todos los campos"; showError = true
                                }
                                password != confirmPassword -> {
                                    errorMsg = "Las contraseñas no coinciden"; showError = true
                                }
                                password.length < 6 -> {
                                    errorMsg = "La contraseña debe tener al menos 6 caracteres"; showError = true
                                }
                                else -> onRegisterSuccess()
                            }
                        })

                        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                            Text("¿Ya tienes cuenta? Inicia sesión", color = Color(0xFF4CAF50))
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ─── Dashboard Screen ─────────────────────────────────────────────────────────

@Composable
fun DashboardScreen(
    onNavigateToReserva : () -> Unit,
    onNavigateToCarta   : () -> Unit,
    onLogout            : () -> Unit
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("GreenPoint", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, "Salir", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier            = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FBF9))
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Greeting banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("¡Bienvenido! 👋", color = Color.White,
                            style = MaterialTheme.typography.titleMedium)
                        Text("¿Qué deseas hacer hoy?", color = Color(0xFFA5D6A7),
                            style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text("Accesos rápidos", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                Spacer(Modifier.height(4.dp))
            }

            item {
                Row(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(
                        text    = "Hacer Reserva",
                        icon    = Icons.Default.EventAvailable,
                        onClick = onNavigateToReserva,
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        text    = "Ver Carta",
                        icon    = Icons.Default.MenuBook,
                        onClick = onNavigateToCarta,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(
                        text     = "Mis Pedidos",
                        icon     = Icons.Default.ReceiptLong,
                        onClick  = { /* future */ },
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        text     = "Promociones",
                        icon     = Icons.Default.LocalOffer,
                        onClick  = { /* future */ },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    text     : String,
    icon     : ImageVector,
    onClick  : () -> Unit,
    modifier : Modifier = Modifier
) {
    Card(
        modifier  = modifier
            .height(110.dp)
            .clickable(onClick = onClick),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        border    = BorderStroke(1.dp, Color(0xFFE8F5E9))
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(8.dp))
            Text(text, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium, color = Color(0xFF2E7D32))
        }
    }
}

// ─── Reservation Screen ───────────────────────────────────────────────────────

@Composable
fun ReservationScreen(onBack: () -> Unit) {
    var nombre   by remember { mutableStateOf("") }
    var dni      by remember { mutableStateOf("") }
    var fecha    by remember { mutableStateOf("") }
    var hora     by remember { mutableStateOf("") }
    var celular  by remember { mutableStateOf("") }
    var personas by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Hacer Reserva", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (submitted) {
            // Success state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF9FBF9)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(32.dp)) {
                    Icon(Icons.Default.CheckCircle, null,
                        tint = Color(0xFF4CAF50), modifier = Modifier.size(80.dp))
                    Text("¡Reserva Confirmada!", style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    Text("Tu reserva para $nombre ha sido registrada.\nTe esperamos el $fecha a las $hora.",
                        textAlign = TextAlign.Center, color = Color.Gray)
                    GreenButton("Volver al Inicio", onBack)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9FBF9))
                    .padding(padding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors    = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text("Datos de la Reserva", style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))

                            GreenTextField("Nombre completo", nombre, { nombre = it },
                                leadingIcon = Icons.Default.Person)
                            GreenTextField("DNI", dni, { dni = it },
                                keyboardType = KeyboardType.Number, leadingIcon = Icons.Default.Badge)
                            GreenTextField("Celular", celular, { celular = it },
                                keyboardType = KeyboardType.Phone, leadingIcon = Icons.Default.Phone)
                            GreenTextField("Fecha (DD/MM/AAAA)", fecha, { fecha = it },
                                leadingIcon = Icons.Default.CalendarToday)
                            GreenTextField("Hora (HH:MM)", hora, { hora = it },
                                leadingIcon = Icons.Default.Schedule)
                            GreenTextField("N° de personas", personas, { personas = it },
                                keyboardType = KeyboardType.Number, leadingIcon = Icons.Default.People)

                            Spacer(Modifier.height(4.dp))
                            GreenButton("Confirmar Reserva", onClick = {
                                if (nombre.isNotBlank() && fecha.isNotBlank() &&
                                    hora.isNotBlank() && personas.isNotBlank()
                                ) submitted = true
                            })
                        }
                    }
                }
            }
        }
    }
}

// ─── Menu Screen ──────────────────────────────────────────────────────────────

@Composable
fun MenuScreen(
    cartSize   : Int,
    onAddToCart: (MenuItem) -> Unit,
    onViewCart : () -> Unit,
    onBack     : () -> Unit
) {
    val categories = remember { menuData.map { it.category }.distinct() }
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    val filtered = menuData.filter { it.category == selectedCategory }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Nuestra Carta", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = Color(0xFF2E7D32),
                    titleContentColor = Color.White
                ),
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartSize > 0) Badge { Text("$cartSize") }
                        }
                    ) {
                        IconButton(onClick = onViewCart) {
                            Icon(Icons.Default.ShoppingCart, "Carrito", tint = Color.White)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FBF9))
                .padding(padding)
        ) {
            // Category tabs
            LazyRow(
                modifier            = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = cat == selectedCategory,
                        onClick  = { selectedCategory = cat },
                        label    = { Text(cat, fontSize = 12.sp) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF4CAF50),
                            selectedLabelColor     = Color.White
                        )
                    )
                }
            }

            LazyColumn(
                modifier            = Modifier.fillMaxSize(),
                contentPadding      = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered) { item ->
                    MenuItemRow(item = item, onAddToCart = onAddToCart)
                }
            }
        }
    }
}

@Composable
fun MenuItemRow(item: MenuItem, onAddToCart: (MenuItem) -> Unit) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model             = item.imageUrl,
                contentDescription = item.name,
                contentScale      = ContentScale.Crop,
                modifier          = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge, color = Color(0xFF1B5E20))
                Spacer(Modifier.height(4.dp))
                Text(item.price, color = Color(0xFF4CAF50),
                    style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }
            IconButton(
                onClick = { onAddToCart(item) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
                    .size(36.dp)
            ) {
                Icon(Icons.Default.Add, "Agregar", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

// ─── Cart Screen ──────────────────────────────────────────────────────────────

@Composable
fun CartScreen(
    items     : List<CartItem>,
    onRemove  : (CartItem) -> Unit,
    onCheckout: () -> Unit,
    onBack    : () -> Unit
) {
    val total = items.sumOf {
        val price = it.menuItem.price.removePrefix("S/ ").toDoubleOrNull() ?: 0.0
        price * it.quantity
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = Color(0xFF2E7D32),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (items.isNotEmpty()) {
                Surface(
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold)
                            Text("S/ ${"%.2f".format(total)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                        }
                        Spacer(Modifier.height(12.dp))
                        GreenButton("Realizar Pedido", onCheckout)
                    }
                }
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF9FBF9)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ShoppingCartCheckout, null,
                        modifier = Modifier.size(72.dp), tint = Color(0xFFBDBDBD))
                    Spacer(Modifier.height(12.dp))
                    Text("Tu carrito está vacío", color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = onBack) {
                        Text("Ver la carta", color = Color(0xFF4CAF50))
                    }
                }
            }
        } else {
            LazyColumn(
                modifier       = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9FBF9))
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items) { cartItem ->
                    CartItemRow(item = cartItem, onRemove = { onRemove(cartItem) })
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier          = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model              = item.menuItem.imageUrl,
                contentDescription = item.menuItem.name,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.menuItem.name, fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium)
                Text(item.menuItem.price, color = Color(0xFF4CAF50),
                    style = MaterialTheme.typography.bodySmall)
                Text("Cantidad: ${item.quantity}", color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, "Eliminar", tint = Color(0xFFE53935))
            }
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    GreenPointTheme { LoginScreen({}, {}) }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    GreenPointTheme { DashboardScreen({}, {}, {}) }
}

@Preview(showBackground = true)
@Composable
fun ReservationPreview() {
    GreenPointTheme { ReservationScreen({}) }
}

@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    GreenPointTheme { MenuScreen(0, {}, {}, {}) }
}
