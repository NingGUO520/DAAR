set terminal png size 1200,800
set output "result2.png"
set grid
set xlabel "Motifs"
set ylabel "Temp d'execution"
set title "Recherche d'un motif dans un texte"
set style line 11 lt 1 lc rgb "orange" lw 3
set style line 10 linetype 1 linecolor rgb "#5080cb" lw 3
set style line 9 lt rgb "red" lw 3 pt 6

plot "result/resultKMP.txt" using 2 with lines linestyle 10,\
 "result/resultEgrep.txt" using 2 with lines linestyle 11, \
    "result/resultRadixTree.txt" using 2 with lines linestyle 9
  