reset
width = 1000
hist(x,width) = width*floor(x/width) + width/2.0
set yrange [0:*]
set boxwidth 0.5 relative
set style fill solid 1
set term postscript
set output "histogram-berlin52.ps"
plot "random-lenghts.dat" u (hist($1,width)):(1.0) smooth freq w boxes lc rgb "black" notitle
