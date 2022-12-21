@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import lesson4.task1.isPalindrome
import ru.spbstu.wheels.defaultCompareTo
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.reflect.typeOf

// Урок 6: разбор строк, исключения
// Максимальное количество баллов = 13
// Рекомендуемое количество баллов = 11
// Вместе с предыдущими уроками (пять лучших, 2-6) = 40/54

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun dateStrToDigit(str: String): String {
    val ans = StringBuilder()
    val months = mapOf(
        "января" to 1, "февраля" to 2,
        "марта" to 3, "апреля" to 4, "мая" to 5,
        "июня" to 6, "июля" to 7,
        "августа" to 8,
        "сентября" to 9, "октября" to 10, "ноября" to 11, "декабря" to 12
    )
    try {
        val parts = str.split(" ")
        val day = parts[0].toInt()
        return if (months.containsKey(parts[1])) {
            val month = months[parts[1]]
            val year = parts[2].toInt()
            if (day > daysInMonth(month!!, year)) return ""
            ans.append("${twoDigitStr(day)}." + twoDigitStr(months[parts[1]]!!) + "." + year)
            ans.toString()
        } else ""
    } catch (e: NumberFormatException) {
        return ""
    } catch (e: IndexOutOfBoundsException) {
        return ""
    }
}


/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    val ans = StringBuilder()
    val months = mapOf(
        "01" to "января", "02" to "февраля",
        "03" to "марта", "04" to "апреля", "05" to "мая",
        "06" to "июня", "07" to "июля",
        "08" to "августа",
        "09" to "сентября", "10" to "октября", "11" to "ноября", "12" to "декабря"
    )
    try {
        val parts = digital.split(".")
        val day = parts[0].toInt()
        if (months.containsKey(parts[1])) {
            val month = months[parts[1]]
            val monthInt = parts[1].toInt()
            val year = parts[2].toInt()
            if (parts.size > 3) return ""
            if (day > daysInMonth(monthInt, year)) return ""
            else {
                ans.append("$day ")
                if (months.containsKey(parts[1])) ans.append("$month ") else return ""
                ans.append(year)
                return ans.toString()
            }
        } else return ""
    } catch (e: NumberFormatException) {
        return ""
    } catch (e: IndexOutOfBoundsException) {
        return ""
    }
}

/**
 * Средняя (4 балла)
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String = TODO()

/**
 * Средняя (5 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (!Regex("""([0-9%-]+\s?)*""").matches(jumps) ||
        !Regex("""\d+""").containsMatchIn(jumps)) return -1
    var maxJump = -1
    val s = jumps.split(" ")
    for (jump in s) {
        if (Regex("""[0-9]*""").matches(jump)) {
            if (jump.toInt() > maxJump) maxJump = jump.toInt()
        }
    }
    return maxJump
}


/**
 * Сложная (6 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (!Regex("""(\d+ [+%-]*\s?)*""").matches(jumps) || "+" !in jumps) return -1
    val s = jumps.split(" ")
    var maxJump = -1
    for (i in s.indices) {
        if (Regex("""\d+""").matches(s[i]) && "+" in s[i + 1]) {
            if (s[i].toInt() > maxJump) maxJump = s[i].toInt()
        }
    }
    return maxJump
}

/**
 * Сложная (6 баллов)
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int = TODO()

/**
 * Сложная (6 баллов)
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int = TODO()


/**
 * Сложная (6 баллов)
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше нуля либо равны нулю.
 */
fun mostExpensive(description: String): String {
    if (!Regex("""\S+ \d+\.?\d*(; \S+ \d+\.?\d*)*""").matches(description)) return ""
    val productsAndPrices = description.split("; ")
    var max = -1.0
    var maxProduct = ""
    for (product in productsAndPrices) {
        val prod = product.split(" ")
        if (prod[1].toDouble() > max) {
            max = prod[1].toDouble()
            maxProduct = prod[0]
        }
    }
    return maxProduct
}

/**
 * Сложная (6 баллов)
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int = TODO()

/**
 * Очень сложная (7 баллов)
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> = TODO()





/**
//зенит - 3,
fun myFun(text: String, teams: List<String>): List<String> {
    if (!Regex("""([a-zA-Zа-яА-я0-9]+ \d+:\d+ [a-zA-Zа-яА-я0-9]+;?\s?)*""").matches(text))
        throw IllegalArgumentException()
    val ans = mutableListOf<String>()
    val map = mutableMapOf<String, List<Int>>()
    val spisok = text.split("; ")
    val list1Team = mutableListOf<Int>()
    val list2Team = mutableListOf<Int>()
    for (el in spisok) {
        val a = el.split(":")
        val b = a[0].split(" ")
        val team1Name = b[0]
        val goalsTeam1 = b[1].toInt()
        val c = a[1].split(" ")
        val team2name = c[1]
        val goalsTeam2 = c[0].toInt()
        if (goalsTeam1 > goalsTeam2) {
            list1Team.add(3)
            map[team1Name] = list1Team
            map[team2name] = list2Team
        }
        if (goalsTeam1 == goalsTeam2) {
            list1Team.add(1)
            list2Team.add(1)
            map[team1Name] = list1Team
            map[team2name] = list2Team
        }
        if (goalsTeam2 > goalsTeam1) {
            list2Team.add(3)
            map[team2name] = list2Team
            map[team1Name] = list2Team
        }
    }
    val f = mutableMapOf<String, Int>()
    for (team in teams) {
        for ((key, value) in map) {
            if (team == key) {
                f[team] = value.sum()
            }
        }
        if (!map.containsKey(team)) f[team] = 0
    }
    /**
    f.toList().sortedBy { (key, value) -> value }.reversed()
    for ((key, value) in f) {
        ans.add(key)
    }
    return ans
    */

    var c = -1
    while (f.isNotEmpty()) {
        for ((key, value) in f) {
            if (value >= c) c = value
        }
        for ((key, value) in f) {
            if (f[key] == c) {
                ans.add(key)
                f.remove(key)
            }
        }
        c = -1
    }
    return ans
}
*/