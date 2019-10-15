set terminal png size 1200,800
set output "resultReg.png"
set title "Comparaison de l'algorithme des automate et la commande egrep"
set auto x
#set yrange [0:1000]
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set boxwidth 0.9
set xtics   ("S(a|g|r)*on" 0," Ba*o*n " 1, "W.st*.i.i*c" 2,"ab*.eng" 3,"(a|k|l)*d" 4)  # label des histogrammes
plot "result/resultEgrepExprReg.txt" using 2, "result/resultAutomateExprReg.txt" using 2 lc 3
