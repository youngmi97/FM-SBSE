import numpy as np
import matplotlib.pyplot as plt



with open('FUN_total') as f:
    lines = f.readlines()
    x = [float(line.split()[0]) for line in lines]
    y = [float(line.split()[1]) for line in lines]



plt.ylim(0,0.1)
plt.xlim(0.5,1)

plt.title("Pareto Front - JAMES") 
plt.xlabel('Mutation Score')
plt.ylabel('Size Score')

plt.plot(x,y,'o', c='r', label='Individual')
plt.show()