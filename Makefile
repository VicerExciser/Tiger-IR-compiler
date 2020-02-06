JAVAC=javac

#sources = $(wildcard **/*.java)
sources = \
	$(wildcard src/ir/datatype/IRType.java) \
	$(wildcard src/ir/datatype/*.java) \
	$(wildcard src/ir/operand/IROperand.java) \
	$(wildcard src/ir/operand/*.java) \
	$(wildcard src/ir/IRFunction.java) \
	$(wildcard src/ir/IRInstruction.java) \
	$(wildcard src/ir/cfg/BasicBlockBase.java) \
	$(wildcard src/ir/cfg/*.java) \
	$(wildcard src/ir/*.java) \
	$(wildcard src/*.java)
classes = $(sources:.java=.class)

#subdirs := $(wildcard */)
#sources := $(wildcard $(addsuffix *.java,$(subdirs)))
#classes := $(patsubst %.java,%.class,$(sources))


all: $(classes)

clean :
	rm -f **/*.class

%.class : %.java
	$(JAVAC) $<
