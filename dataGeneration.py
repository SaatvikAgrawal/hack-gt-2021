import random

n = 30
firstNames = ["John", "Jack", "Bill", "Richard", "Frank", "Joe", "Donald", "Dan", "Brick"]
lastNames = ["Smith", "Obama", "Johnson", "Roosevelt", "Ali", "Washington"]
f = open("data.csv", "a")
for i in range(n):
  name = firstNames[random.randint(0, len(firstNames) - 1)] + " " + lastNames[random.randint(0, len(lastNames) - 1)]
  f.write("'")
  f.write(name)
  f.write("'")
  f.write(",")
  for i in range(5):
		f.write(str(random.randint(0, 101)))
		if i != 4:
			f.write(",")
  f.write("\n")
