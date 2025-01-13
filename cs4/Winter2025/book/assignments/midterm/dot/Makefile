FMT = svg
DEST = ../images

default: draw draw_rot

all: default install

draw:
	dot -T${FMT} t0.dot	   -o t0.${FMT}
	dot -T${FMT} t1.dot	   -o t1.${FMT}
	dot -T${FMT} t2.dot	   -o t2.${FMT}
	dot -T${FMT} t3.dot	   -o t3.${FMT}
	dot -T${FMT} t4_n.dot  -o t4_n.${FMT}
	dot -T${FMT} t4.dot	   -o t4.${FMT}
	dot -T${FMT} t5.dot	   -o t5.${FMT}
	dot -T${FMT} t6.dot	   -o t6.${FMT}
	dot -T${FMT} t7_n.dot  -o t7_n.${FMT}
	dot -T${FMT} t7.dot	   -o t7.${FMT}
	dot -T${FMT} t8.dot	   -o t8.${FMT}
	dot -T${FMT} t9.dot	   -o t9.${FMT}
	dot -T${FMT} t10.dot   -o t10.${FMT}
	dot -T${FMT} t11_n.dot -o t11_n.${FMT}
	dot -T${FMT} t11.dot   -o t11.${FMT}
	dot -T${FMT} t12_n.dot -o t12_n.${FMT}
	dot -T${FMT} t12.dot   -o t12.${FMT}

draw_rot:
	dot -T${FMT} left_single_rotation1.dot  -o left_single_rotation1.${FMT}
	dot -T${FMT} left_single_rotation2.dot  -o left_single_rotation2.${FMT}
	dot -T${FMT} right_single_rotation1.dot -o right_single_rotation1.${FMT}
	dot -T${FMT} right_single_rotation2.dot -o right_single_rotation2.${FMT}
	dot -T${FMT} left_double_rotation1.dot  -o left_double_rotation1.${FMT}
	dot -T${FMT} left_double_rotation2.dot  -o left_double_rotation2.${FMT}
	dot -T${FMT} right_double_rotation1.dot -o right_double_rotation1.${FMT}
	dot -T${FMT} right_double_rotation2.dot -o right_double_rotation2.${FMT}

clean:
	rm -f *.${FMT}

install:
	mv *.${FMT} ${DEST}
