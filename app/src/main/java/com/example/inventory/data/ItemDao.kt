package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) //La estrategia OnConflictStrategy.IGNORE omite un elemento nuevo si ya es clave primaria en la base de datos.
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM item WHERE id= :id") //   :id Usa la notación de dos puntos en la consulta para hacer referencia a argumentos en la función.
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * FROM item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>

    /*
    * sar Flow o LiveData como tipo de datos garantizará que se te notifique cuando cambien los
    *  datos de la base de datos. Se recomienda usar Flow en la capa de persistencia. Room mantiene
    *  este Flow actualizado por ti, lo que significa que solo necesitas obtener los datos de forma
    *  explícita una vez. Esto es útil para actualizar la lista de inventario, que implementarás en
    *  el siguiente codelab. Debido al tipo de datos que se muestra para Flow, Room también ejecuta
    *  la búsqueda en el subproceso en segundo plano. No necesitas convertirla de manera explícita
    *  en una función suspend ni llamar dentro del alcance de la corrutina.*/
}