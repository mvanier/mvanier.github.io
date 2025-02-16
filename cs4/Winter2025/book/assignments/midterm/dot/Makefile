.RECIPEPREFIX = >

FILES = 0 1 2 3 4 5 6 7 8 9 10
FMT = svg

compile:
> for file in ${FILES}; do dot -T${FMT} -o test$$file.${FMT} test$$file.dot; done

install:
> mv *.${FMT} ..

clean:
> rm -f *.${FMT}
