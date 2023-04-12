package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao        //Puedes tener varios DAO.

    companion object {
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null

        fun getDatabase(context: Context): ItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }

}


/**
 * La base de datos necesita saber sobre el DAO. Dentro del cuerpo de la clase, declara una función abstracta que muestre el ItemDao. Puedes tener varios DAO.
abstract fun itemDao(): ItemDao

Debajo de la función abstracta, define un objeto companion. El objeto complementario permite el acceso a los métodos para crear u obtener la base de datos con el nombre de clase como calificador.
companion object {}

Dentro del objeto companion, declara una variable anulable privada INSTANCE para la base de datos y, luego, inicializala en null. La variable INSTANCE mantendrá una referencia a la base de datos, cuando se cree una. Esto ayuda a mantener una sola instancia de la base de datos abierta en un momento determinado, que es un recurso costoso para crear y mantener.
Anota INSTANCE con @Volatile. El valor de una variable volátil nunca se almacenará en caché, y todas las operaciones de escritura y lectura se realizarán desde y hacia la memoria principal. Esto ayuda a garantizar que el valor de INSTANCE esté siempre actualizado y sea el mismo para todos los subprocesos de ejecución. Eso significa que los cambios realizados por un subproceso en INSTANCE son visibles de inmediato para todos los demás subprocesos.

@Volatile
private var INSTANCE: ItemRoomDatabase? = null

Debajo de INSTANCE, mientras estás dentro del objeto companion, define un método getDatabase() con un parámetro Context que necesite el compilador de bases de datos. Muestra un tipo ItemRoomDatabase. Verás un error porque getDatabase() todavía no muestra nada.

fun getDatabase(context: Context): ItemRoomDatabase {}

Es posible que varios subprocesos se ejecuten en una condición de carrera y soliciten una instancia de base de datos al mismo tiempo, lo que genera dos bases de datos en lugar de una. Unir el código para obtener la base de datos dentro de un bloque synchronized significa que solo un subproceso de ejecución a la vez puede ingresar este bloque de código, lo que garantiza que la base de datos solo se inicialice una vez.
Dentro de getDatabase(), muestra la variable INSTANCE o, si INSTANCE es nula, inicialízala dentro de un bloque synchronized{}. Para ello, usa el operador elvis (?:). Pasa el objeto complementario this que deseas bloquear dentro del bloque de función. Solucionarás el error en los pasos posteriores.

return INSTANCE ?: synchronized(this) { }

Dentro del bloque sincronizado, crea una variable de instancia val y usa el generador de bases de datos para obtener la base de datos. Seguirás teniendo errores que solucionarás en los próximos pasos.

val instance = Room.databaseBuilder()
Al final del bloque synchronized, muestra instance.

return instance
Dentro del bloque synchronized, inicializa la variable instance y usa el compilador de bases de datos para obtener una base de datos. Pasa a Room.databaseBuilder() el contexto de la aplicación, la clase de la base de datos y un nombre para la base de datos, item_database.

val instance = Room.databaseBuilder(
context.applicationContext,
ItemRoomDatabase::class.java,
"item_database"
)
Android Studio generará un error de tipo de coincidencia. Para quitarlo, deberás agregar una estrategia de migración y build() en los pasos siguientes.

Agrega la estrategia de migración necesaria al compilador. Utiliza .fallbackToDestructiveMigration().
Normalmente, tendrías que proporcionar un objeto de migración con una estrategia para cuando cambie el esquema. Un objeto de migración es un objeto que define cómo tomas todas las filas con el esquema anterior y las conviertes en filas en el esquema nuevo para que no se pierdan datos. La migración no se incluye en este codelab. Una solución simple es destruir y volver a compilar la base de datos, lo que significa que los datos se pierden.


.fallbackToDestructiveMigration()
Para crear la instancia de base de datos, llama a .build(). Esto debería quitar los errores de Android Studio.

.build()
Dentro del bloque synchronized, asigna INSTANCE = instance.

INSTANCE = instance

Al final del bloque synchronized, muestra instance. El código final debería verse así:

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {

abstract fun itemDao(): ItemDao

companion object {
@Volatile
private var INSTANCE: ItemRoomDatabase? = null
fun getDatabase(context: Context): ItemRoomDatabase {
return INSTANCE ?: synchronized(this) {
val instance = Room.databaseBuilder(
context.applicationContext,
ItemRoomDatabase::class.java,
"item_database"
)
.fallbackToDestructiveMigration()
.build()
INSTANCE = instance
return instance
}
}
}
}
 * */