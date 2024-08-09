package com.mertg.gelirgidertakip.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mertg.gelirgidertakip.model.GelirModel
import com.mertg.gelirgidertakip.model.GiderModel
import java.text.SimpleDateFormat
import java.util.*

class GelirGiderViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    var gelirlerListesi = mutableStateListOf<GelirModel>()
        private set

    var giderlerListesi = mutableStateListOf<GiderModel>()
        private set

    var finansalListe = mutableStateListOf<Any>()
        private set

    var toplamBakiye = mutableStateOf(0)
        private set

    var kategoriYuzdeleri = mutableStateListOf<Pair<String, Float>>()
        private set

    var giderKategoriYuzdeleri = mutableStateListOf<Pair<String, Float>>()
        private set

    var filtre = mutableStateOf("Bu Ay")
        private set

    var finansalListeFiltered = mutableStateListOf<Any>()
        private set

    var toplamGelir = mutableStateOf(0)
        private set

    var toplamGider = mutableStateOf(0)
        private set

    init {
        fetchFinancialData()
    }

    fun fetchFinancialData() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("users").document(uid).collection("finansal_veriler")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Tarihe göre sıralıyoruz
            .get()
            .addOnSuccessListener { documents ->
                val gelirler = mutableListOf<GelirModel>()
                val giderler = mutableListOf<GiderModel>()

                for (document in documents) {
                    val type = document.getString("type")
                    if (type == "Gelir") {
                        val gelir = document.toObject(GelirModel::class.java)
                        gelirler.add(gelir)
                    } else if (type == "Gider") {
                        val gider = document.toObject(GiderModel::class.java)
                        giderler.add(gider)
                    }
                }

                gelirlerListesi.clear()
                gelirlerListesi.addAll(gelirler)

                giderlerListesi.clear()
                giderlerListesi.addAll(giderler)

                // Finansal listeyi güncellerken sıralı olarak ekleyelim
                finansalListe.clear()
                finansalListe.addAll(gelirler + giderler)

                applyFilter(filtre.value) // Filtreyi uygula
            }
    }


    fun gelirEkle(gelir: GelirModel) {
        val uid = auth.currentUser?.uid ?: return
        val gelirWithPrefix = gelir.copy(amount = "+${gelir.amount}", type = "Gelir", date = getCurrentDateTime())
        gelirlerListesi.add(gelirWithPrefix)
        finansalListe.add(gelirWithPrefix)
        hesaplaToplamBakiye()

        firestore.collection("users").document(uid).collection("gelirler").add(gelirWithPrefix)
        firestore.collection("users").document(uid).collection("finansal_veriler").add(gelirWithPrefix)
    }

    fun giderEkle(gider: GiderModel) {
        val uid = auth.currentUser?.uid ?: return
        val giderWithPrefix = gider.copy(amount = "-${gider.amount}", type = "Gider", date = getCurrentDateTime())
        giderlerListesi.add(giderWithPrefix)
        finansalListe.add(giderWithPrefix)
        hesaplaToplamBakiye()

        firestore.collection("users").document(uid).collection("giderler").add(giderWithPrefix)
        firestore.collection("users").document(uid).collection("finansal_veriler").add(giderWithPrefix)
    }

    fun gelirSil(gelir: GelirModel) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).collection("finansal_veriler")
            .whereEqualTo("type", "Gelir")
            .whereEqualTo("title", gelir.title)
            .whereEqualTo("category", gelir.category)
            .whereEqualTo("amount", gelir.amount)
            .whereEqualTo("date", gelir.date)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    firestore.collection("users").document(uid).collection("finansal_veriler").document(document.id).delete()
                }
                gelirlerListesi.remove(gelir)
                finansalListe.remove(gelir)
                hesaplaToplamBakiye()
                applyFilter(filtre.value) // Sayfayı güncelle
            }
    }

    fun giderSil(gider: GiderModel) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).collection("finansal_veriler")
            .whereEqualTo("type", "Gider")
            .whereEqualTo("title", gider.title)
            .whereEqualTo("category", gider.category)
            .whereEqualTo("amount", gider.amount)
            .whereEqualTo("date", gider.date)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    firestore.collection("users").document(uid).collection("finansal_veriler").document(document.id).delete()
                }
                giderlerListesi.remove(gider)
                finansalListe.remove(gider)
                hesaplaToplamBakiye()
                applyFilter(filtre.value) // Sayfayı güncelle
            }
    }

    fun gelirGuncelle(gelir: GelirModel, newTitle: String, newCategory: String, newAmount: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).collection("finansal_veriler")
            .whereEqualTo("type", "Gelir")
            .whereEqualTo("title", gelir.title)
            .whereEqualTo("category", gelir.category)
            .whereEqualTo("amount", gelir.amount)
            .whereEqualTo("date", gelir.date)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val updatedData = hashMapOf(
                        "title" to newTitle,
                        "category" to newCategory,
                        "amount" to newAmount
                    )
                    firestore.collection("users").document(uid).collection("finansal_veriler").document(document.id).update(updatedData as Map<String, Any>)
                }
                fetchFinancialData()
            }
    }

    fun giderGuncelle(gider: GiderModel, newTitle: String, newCategory: String, newAmount: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).collection("finansal_veriler")
            .whereEqualTo("type", "Gider")
            .whereEqualTo("title", gider.title)
            .whereEqualTo("category", gider.category)
            .whereEqualTo("amount", gider.amount)
            .whereEqualTo("date", gider.date)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val updatedData = hashMapOf(
                        "title" to newTitle,
                        "category" to newCategory,
                        "amount" to newAmount
                    )
                    firestore.collection("users").document(uid).collection("finansal_veriler").document(document.id).update(updatedData as Map<String, Any>)
                }
                fetchFinancialData()
            }
    }




    private fun hesaplaToplamBakiye() {
        val toplamGelir = gelirlerListesi.sumOf { it.amount.toInt() }
        val toplamGider = giderlerListesi.sumOf { it.amount.toInt() }
        toplamBakiye.value = toplamGelir + toplamGider
    }

    fun hesaplaToplamlar() {
        toplamGelir.value = gelirlerListesi.sumOf { it.amount.toInt() }
        toplamGider.value = giderlerListesi.sumOf { it.amount.toInt() }
    }

    private fun hesaplaGelirKategorileriYuzdeleri() {
        val toplamGelir = gelirlerListesi.sumOf { it.amount.toInt() }
        val kategoriToplamlari = gelirlerListesi.groupBy { it.category }
            .mapValues { it.value.sumOf { gelir -> gelir.amount.toInt() } }

        kategoriYuzdeleri.clear()
        kategoriToplamlari.forEach { (kategori, toplam) ->
            val yuzde = (toplam.toFloat() / toplamGelir) * 100
            kategoriYuzdeleri.add(kategori to yuzde)
        }
    }

    private fun hesaplaGiderKategorileriYuzdeleri() {
        val toplamGider = giderlerListesi.sumOf { it.amount.toInt() }
        val kategoriToplamlari = giderlerListesi.groupBy { it.category }
            .mapValues { it.value.sumOf { gider -> gider.amount.toInt() } }

        giderKategoriYuzdeleri.clear()
        kategoriToplamlari.forEach { (kategori, toplam) ->
            val yuzde = (toplam.toFloat() / toplamGider) * 100
            giderKategoriYuzdeleri.add(kategori to yuzde)
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    fun applyFilter(filter: String) {
        filtre.value = filter

        val now = Calendar.getInstance()
        val filteredGelirler = when (filter) {
            "Bu Ay" -> gelirlerListesi.filter {
                val date = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(it.date)
                val calendar = Calendar.getInstance().apply { time = date }
                calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH)
            }
            "Geçen Ay" -> gelirlerListesi.filter {
                val date = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(it.date)
                val calendar = Calendar.getInstance().apply { time = date }
                calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH) - 1
            }
            "Daha Eski" -> gelirlerListesi.filter {
                val date = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(it.date)
                val calendar = Calendar.getInstance().apply { time = date }
                calendar.before(now.apply { add(Calendar.MONTH, -1) })
            }
            "Tümü" -> gelirlerListesi
            else -> gelirlerListesi
        }

        val filteredGiderler = when (filter) {
            "Bu Ay" -> giderlerListesi.filter {
                val date = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(it.date)
                val calendar = Calendar.getInstance().apply { time = date }
                calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH)
            }
            "Geçen Ay" -> giderlerListesi.filter {
                val date = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(it.date)
                val calendar = Calendar.getInstance().apply { time = date }
                calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH) - 1
            }
            "Daha Eski" -> giderlerListesi.filter {
                val date = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(it.date)
                val calendar = Calendar.getInstance().apply { time = date }
                calendar.before(now.apply { add(Calendar.MONTH, -1) })
            }
            "Tümü" -> giderlerListesi
            else -> giderlerListesi
        }

        finansalListeFiltered.clear()
        finansalListeFiltered.addAll(filteredGelirler + filteredGiderler)
        finansalListeFiltered.sortByDescending {
            when (it) {
                is GelirModel -> SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(it.date).time
                is GiderModel -> SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(it.date).time
                else -> 0L
            }
        }

        hesaplaToplamBakiye()
        hesaplaGelirKategorileriYuzdeleri()
        hesaplaGiderKategorileriYuzdeleri()
    }

}
