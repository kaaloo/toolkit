def trimWords(lines: List[String]) = lines flatMap { line =>
   "[a-zA-Z]+".r findAllIn line map (_.toLowerCase)
}

def treeify(arr: Array[Int], num: Int, max: Int, pos: Int) : Array[Int] = {
   val quotient = num / max
   val remainder = num % max

   arr(pos) = remainder
   if (quotient < max) { arr(pos+1) = quotient }
   if (quotient >= max) { treeify(arr, quotient, max, pos+1) }
   return arr
}

//-------------------------------
// subsegment will take an input.txt -> matrix of indexs of occurances
// matrix -> permutate all combos to -> results -> filter to -> answers
// Written by: Kyle Dinh @ https://github.com/kyledinh/toolkit/tree/master/problems/subsegment
// scala 2.10.1
//-------------------------------

val file = io.Source.fromFile("input.txt","utf-8")
val lines = file.getLines.toList
val depth = lines(0).toInt
val target = lines.slice(1, depth+1)
val src = trimWords(lines(depth +1).split(" ").toList)
val breath = target.map(t => src.count(_ == t)).max     

//val matrix = Array.ofDim[Int](depth, breath) 
val matrix = Array.fill(depth, breath)(-1)

println("Target " + target.mkString(" "))
println("depth " + depth + ", breath " + breath)
println("Src " + src.length)

for { x <- 0 to depth-1 } yield {
   var y = 0;
   // fill the array with -1 as initial value
   //for { k <- 0 to breath-1 } yield { matrix(x)(k) = -1 }

   for { i <- 0 to src.length-1 } yield {
      if (src(i) == target(x)) {
         println("Found @ " + i + " " + src(i) + " for " + target(x))
         matrix(x)(y) = i
         y = y+1
      }
   }
}

for { x <- 0 to depth-1 } yield {
   println(x + " row of matrix " + matrix(x).deep.mkString(" "))
}

/*
matrix(0) 0, 4, 9
      (1) 2, 6, 11
      (2) 3, 8, 13
      (3) 7, 12

combos:
0,2,3,7
4,2,3,7
9,2,3,7
...
*/

// Now, permutate through the matrix for each combonation; a num from each row(depth)

val comboArray = Array.fill(depth)(0)
var results = List[Array[Int]]()

for { x <- 0 to math.pow(breath, depth).toInt -1 } yield {
   var b = Array.fill(depth)(-1)
   var combo = treeify(comboArray, x, breath, 0)

   for { i <- 0 to b.length-1 } yield {
      b(i) = matrix(i)(combo(i))
   }
   val distance = b.max - b.min
   b = b :+ distance
   println(b.mkString(" "))

   // reject combos with a -1 from results; incomplete combos
   if (!b.contains(-1)) { results = results :+ b }  
   //println("For  " + x +" : " + combo.mkString(" ")  + "   " + b.mkString(" ")  + "  : " + distance ) 
}

println("Number of possible combos:  " + results.length) 
val answers = results.sortWith(_(depth) < _(depth))
val m = answers.head(depth) 
println("Your answer(s) for shortest substring with the length of : " + m)
def printList(l: Array[Int]): Unit = { println(l.mkString(" "))}
answers.filter(_(depth) == m).foreach(printList)